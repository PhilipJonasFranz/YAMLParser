package YAMLParser;

/**
 * This class represents a single building block of an YAML-File.
 * The contained token type dictates the type of the token, and
 * the spelling stores the concrete spelling in the source file.
 * 
 * @author jonas.franz
 */
public class Token {
		
			/* --- NESTED --- */
	/**
	 * Enum to describe the type of a token.
	 */
	public enum TokenType {
		
		/** Describes the character ':'. */
		COLON(":"),
		/** Describes an indentation step. */
		INDENT("\t"),
		/** Describes a newline. */
		NEWLINE(""),
		/** Describes a string value, for example a value or id. */
		STRING(""),
		/** Describes a dash character, for example when marking a new section. */
		DASH("-");
		
		/** Default spelling of this token type */
		private final String spelling;
		
		/**
		 * Define Token with custom spelling.
		 * @param spelling The spelling of this token.
		 */
		private TokenType(String spelling) {
			this.spelling = spelling;
		}
		
		/**
		 * Returns the spelling of this token as string.
		 * @return The spelling of the token.
		 */
		public String getSpelling() {
			return this.spelling;
		}
		
	}
	
	/**
	 * The type of the token.
	 */
	TokenType type;
	
	/**
	 * The spelling of the token. Can be used to get
	 * the actual String from a STRING-Token.
	 */
	String spelling;
	
	
			/* --- CONSTRUCTORS --- */
	/**
	 * Create a new token with a type and custom spelling.
	 * @param type The type of the new token.
	 * @param spelling The custom spelling of this token.
	 */
	public Token(TokenType type, String spelling) {
		this.type = type;
		this.spelling = spelling;
	}
	
	/**
	 * Create a new token with a type and the default spelling
	 * of the token type.
	 * @param type The type of the new token.
	 */
	public Token(TokenType type) {
		this.type = type;
		this.spelling = type.getSpelling();
	}
	
	
			/* --- METHODS --- */
	/**
	 * Returns the type of this token.
	 * @return The type of this token.
	 */
	public TokenType getType() {
		return this.type;
	}
	
	/**
	 * Returns the spelling of this token.
	 * @return Returns the spelling of this token.
	 */
	public String getSpelling() {
		return this.spelling;
	}
	
	@Override
	public String toString() {
		return "[" + this.type.toString() + ", " + this.spelling + "]\n";
	}
	
}