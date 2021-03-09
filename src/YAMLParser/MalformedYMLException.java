package YAMLParser;

/**
 * This exception is thrown when it is attempted to barse bad YML input, 
 * or when the YML that is parsed does not follow the YML-Grammar.
 * 
 * @author jonas.franz
 */
public class MalformedYMLException extends Exception {

			/* --- FIELDS --- */
	/** Serialization UID */
	private static final long serialVersionUID = 4954963204648493440L;
	
	/** The message of this exception */
	private String message;
	
	
			/* --- CONSTRUCTORS --- */
	/**
	 * Create a new instance with a custom message.
	 * @param message The message of this exception.
	 */
	public MalformedYMLException(String message) {
		this.message = message;
	}
	
	/**
	 * Create a new instance with the default message.
	 */
	public MalformedYMLException() {
		this.message = "The given YAML file is malformed!";
	}

	
			/* --- METHODS --- */
	@Override
	public String getMessage() {
		return this.message;
	}
	
}
