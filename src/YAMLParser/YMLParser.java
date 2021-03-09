package YAMLParser;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import YAMLParser.Token.TokenType;

/**
 * This class represents the YML-Dataformat as a tree structure and offers
 * the ability to load the datastructure from a given YML-File.
 * 
 * @author jonas.franz
 *
 */
public class YMLParser {
	
			/* ---< NESTED >--- */
	/**
	 * Represents the top-level datastructure, that holds multiple
	 * sections.
	 * 
	 * @author jonas.franz
	 *
	 */
	public static class YMLTree {
		
				/* ---< FIELDS >--- */
		/**
		 * The sections contained in this yml-file.
		 */
		private List<YMLSection> sections = new ArrayList();
		
		
				/* ---< CONSTRUCTORS >--- */
		/**
		 * Default constructor, create a new, empty yml-tree.
		 */
		public YMLTree() {
			
		}
		
		/**
		 * Create a new yml-tree with the given sections.
		 * @param sections The sections to be added to the tree.
		 */
		public YMLTree(List<YMLSection> sections) {
			this.sections = sections;
		}
		
		
				/* ---< METHODS >--- */
		/**
		 * Adds the given section as the new last section.
		 * @param section The section to add.
		 */
		public void addSection(YMLSection section) {
			this.sections.add(section);
		}
		
		/**
		 * Returns all contained sections in this yml-tree.
		 * @return The contained sections as a list.
		 */
		public List<YMLSection> getSections() {
			return this.sections;
		}
		
		/**
		 * Prints out this yml-tree by printing out all contained
		 * sections.
		 * @param ps The printstream the tree should be printed to
		 */
		public void print(PrintStream ps) {
			for (int i = 0; i < this.sections.size(); i++) {
				this.sections.get(i).print(ps);
				if (i < this.sections.size() - 1) ps.println("---");
			}
		}
		
	}
	
	/**
	 * A sections contains multiple top-level-scoped yml-nodes.
	 * 
	 * @author jonas.franz
	 *
	 */
	public static class YMLSection {
		
		/**
		 * The top-nodes contained in this section. Top nodes means
		 * that these node are the root nodes, and these nodes will contain
		 * all other non-top-level-scoped nodes as children.
		 */
		private List<YMLNode> nodes = new ArrayList();
		
		
				/* ---< CONSTRUCTORS >--- */
		/**
		 * Default constructor, create a new, empty yml-section.
		 */
		public YMLSection() {
			
		}
		
		/**
		 * Create a new yml-section with the given nodes.
		 * @param nodes The nodes to be added to the section.
		 */
		public YMLSection(List<YMLNode> nodes) {
			this.nodes = nodes;
		}
		
		
				/* ---< METHODS >--- */
		/**
		 * Adds the given node to this section.
		 * @param node The node to add.
		 */
		public void addNode(YMLNode node) {
			this.nodes.add(node);
		}
		
		/**
		 * Returns all contained nodes in this yml-section.
		 * @return The contained nodes as a list.
		 */
		public List<YMLNode> getNodes() {
			return this.nodes;
		}
		
		/**
		 * Prints out this yml-tree by printing out all contained
		 * nodes.
		 * @param ps The printstream this section should be printed to
		 */
		public void print(PrintStream ps) {
			this.nodes.forEach(x -> x.print(ps));
		}
	}
	
			/* ---< NESTED >--- */
	/**
	 * 
	 * A yml node contains either a value or children. The
	 * node also contains an id.
	 * 
	 * @author jonas.franz
	 *
	 */
	public static class YMLNode {
		
				/* ---< FIELDS >--- */
		/**
		 * The ID or key of this node.
		 */
		private String ID;
		
		/**
		 * The value of this node. May be null
		 * if this node has no value but child nodes.
		 */
		private String value;
		
		/**
		 * The childs of this node. A node can either have a value or childs.
		 */
		private List<YMLNode> children = new ArrayList();
		
		
				/* ---< CONSTRUCTORS >--- */
		/**
		 * Parses a YML-Node from the given token stream. This call may be recursive, and
		 * build the YML-Subtree defined in the token stream.
		 * @param tokens The Tokens that represent the YML-File.
		 * @param depth The current parsing depth, or indentation depth for this node.
		 * @throws MalformedYMLException Thrown if the YML-Input does not follow YML-Conventions.
		 */
		public YMLNode(List<Token> tokens, int depth) throws MalformedYMLException {
			
			/* Accept indentation depth */
			for (int i = 0; i < depth; i++) 
				accept(tokens, TokenType.INDENT);
			
			/* Accept ID */
			this.ID = accept(tokens, TokenType.STRING).getSpelling();
			
			accept(tokens, TokenType.COLON);
			
			/* Accept value if value is present */
			if (tokens.get(0).type != TokenType.NEWLINE) {
				String value = "";
				
				accept(tokens, TokenType.INDENT);
				
				while (!tokens.isEmpty() && tokens.get(0).type != TokenType.NEWLINE) {
					value += accept(tokens).getSpelling();
				}
				
				/* Value */
				this.value = value;
			}
			
			/* Accept new line */
			accept(tokens, TokenType.NEWLINE);
			
			/* If node has no value, parse child nodes */
			if (this.value == null) {
				while (!tokens.isEmpty()) {
					int indents = this.countIndent(tokens);
					if (indents <= depth) break;
					
					this.children.add(new YMLNode(tokens, depth + 1));
				}
			}
		}
		
		
				/* ---< METHODS >--- */
		/**
		 * Counts the amount of indentation-tokens that are back to back
		 * from the start of the list.
		 * @param tokens The list containing the tokens.
		 * @return The amount of indentation tokens.
		 */
		private int countIndent(List<Token> tokens) {
			for (int i = 0; i < tokens.size(); i++)
				if (tokens.get(i).type != TokenType.INDENT)
					return i;
			
			return tokens.size();
		}
		
		/**
		 * Checks if the first token type is the given type. If this is the case, 
		 * the first token is removed from the list and returned.
		 * @param tokens The token list.
		 * @param type The expected type of the first token.
		 * @return The removed first token.
		 * @throws MalformedYMLException Thrown if the list is empty or the token type is not equal to the expected.
		 */
		protected static Token accept(List<Token> tokens, TokenType type) throws MalformedYMLException {
			if (tokens.isEmpty()) throw new MalformedYMLException();
			else {
				if (tokens.get(0).getType() == type) return accept(tokens);
				else throw new MalformedYMLException("Expected Token '" + type.toString() + "', but got '" + tokens.get(0).getType().toString() + "'");
			}
		}
		
		/**
		 * Removes the first token from the given list and returns it.
		 * @param tokens The token list.
		 * @return The removed first token.
		 * @throws MalformedYMLException Thrown if the list is empty.
		 */
		protected static Token accept(List<Token> tokens) throws MalformedYMLException {
			if (tokens.isEmpty()) throw new MalformedYMLException();
			else return tokens.remove(0);
		}
		
		/**
		 * Returns the node with given ID.
		 * @param ID The ID of the searched node.
		 * @return Returns the first match when the YML-tree is traversed inorder.
		 */
		public YMLNode getNode(String ID) {
			return this.getNodeRec(ID);
		}
		
		/**
		 * Recursiveley searches for the given ID in the YML-Tree and
		 * returns the first match or null, if no match is found.
		 * @param ID The searched ID
		 * @return The found node or null.
		 */
		private YMLNode getNodeRec(String ID) {
			if (this.ID.equals(ID)) return this;
			else {
				for (YMLNode n0 : this.children) {
					YMLNode n1 = n0.getNode(ID);
					if (n1 != null) return n1;
				}
				
				return null;
			}
		}
		
		/**
		 * Returns the value of this node. May be null.
		 * @return The value of this node.
		 */
		public String getValue() {
			return this.value;
		}
		
		/**
		 * Returns the value of the node with given ID. If this node has
		 * the given ID, the value of this node is returned. Otherwise, the
		 * subtree of this node is searched for the given ID.
		 * @param ID The ID of the searched node.
		 * @return The value that was found or null if no node exists with the given id.
		 */
		public String getValue(String ID) {
			if (this.getNode(ID) == null) return null;
			else return this.getNode(ID).value;
		}
		
		/**
		 * Returns the ID of this node.
		 * @return Returns the id of this node as string.
		 */
		public String getID() {
			return this.ID;
		}
		
		/**
		 * Returns the child nodes of this node as list.
		 * @return Returns the children of this node as list.
		 */
		public List<YMLNode> getChildren() {
			return this.children;
		}
		
		/**
		 * Prints out this yml-node and all of its children.
		 * @param ps The printstream the nodes should be printed to
		 */
		public void print(PrintStream ps) {
			this.printRec(ps, 0);
		}
		
		/**
		 * Prints out this node recursiveley.
		 * @param ps The printstream the nodes should be printed to
		 * @param d The current printing depth.
		 */
		private void printRec(PrintStream ps, int d) {
			for (int i = 0; i < d; i++) 
				ps.print(" ");
			
			ps.print(this.ID + ": ");
			
			if (this.value != null)
				ps.print(this.value);
			
			ps.println();
			
			for (YMLNode node : this.children)
				node.printRec(ps, d + 4);
		}
		
	}
	
	
			/* ---< CONSTRUCTORS >--- */
	/**
	 * Creates a new YML-Tree from the contents of the given file.
	 * @param f The file that contains the YML data.
	 * @return A parsed YML tree.
	 * @throws MalformedYMLException Thrown if the YML Data is malformed.
	 */
	public static YMLTree parse(File f) throws MalformedYMLException {
		if (f == null) return null;
		
		/* Read file */
		List<String> file = Util.readFile(f);
		
		return parse(file);
	}
	
	/**
	 * Creates a new YML-Tree from the contents of the strings
	 * @param yml The yml to be parsed as a list of strings
	 * @return A parsed YML tree
	 * @throws MalformedYMLException Thrown if the YML Data is malformed.
	 */
	public static YMLTree parse(List<String> yml) throws MalformedYMLException {
		if (yml == null) return null;
		
		/* Convert to token stream */
		List<Token> tokens = new Lexer().ymlToTokenStream(yml);
		
		YMLTree tree = new YMLTree();
		
		/* Parse YML Tree */
		while (!tokens.isEmpty()) {
			
			YMLSection section = new YMLSection();
			
			while (!tokens.isEmpty() && tokens.get(0).type != TokenType.DASH) {
				/* Parse a single node in the section */
				YMLNode node = new YMLNode(tokens, 0);
				section.addNode(node);
			}
			
			tree.addSection(section);
			
			/* Parsed all tokens, done. */
			if (tokens.isEmpty()) break;
			else {
				/* Accept new section marker */
				YMLNode.accept(tokens, TokenType.DASH);
				YMLNode.accept(tokens, TokenType.DASH);
				YMLNode.accept(tokens, TokenType.DASH);
				YMLNode.accept(tokens, TokenType.NEWLINE);
			}
		}
		
		return tree;
	}
	
} 
