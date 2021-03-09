package YAMLParser;

import java.util.ArrayList;
import java.util.List;

import YAMLParser.Token.TokenType;

/**
 * The lexer is responsible for converting the passed yml in string format
 * into a stream of Tokens. The tokens will contain their spelling, as well
 * as a token type that denote their type.
 * 
 * @author jonas.franz
 *
 */
public class Lexer {
	
	/**
	 * Parses a list of tokens from the given string that 
	 * represents a flattened YML-File.
	 * @param lines The contents of the yml file, one string per line
	 * @return A list of tokens representing the yml file.
	 */
	public List<Token> ymlToTokenStream(List<String> lines) {
		List<Token> tokens = new ArrayList();
		
		while (!lines.isEmpty()) {
			String line = lines.remove(0);
			
			while (!line.isEmpty()) {
				if (line.startsWith(" ") || line.startsWith("\t")) {
					tokens.add(new Token(TokenType.INDENT));
					line = line.substring(1);
				}
				else if (line.startsWith(":")) {
					tokens.add(new Token(TokenType.COLON));
					line = line.substring(1);
				}
				else if (line.startsWith("-")) {
					tokens.add(new Token(TokenType.DASH));
					line = line.substring(1);
				}
				else {
					String value = "";
					while (!line.isEmpty() && !line.startsWith(":")) {
						value += line.charAt(0);
						line = line.substring(1);
					}
					
					tokens.add(new Token(TokenType.STRING, value));
				}
			}
			
			tokens.add(new Token(TokenType.NEWLINE));
		}
		
		//tokens.stream().forEach(x -> System.out.print(x.toString()));
		
		return tokens;
	}
	
}