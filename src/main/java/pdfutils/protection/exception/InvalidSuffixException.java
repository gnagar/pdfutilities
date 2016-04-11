package pdfutils.protection.exception;

public class InvalidSuffixException extends RuntimeException {

	private static final long serialVersionUID = -2602743435287162643L;

	public InvalidSuffixException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSuffixException(String message) {
		super(message);
	}

}
