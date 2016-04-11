package pdfutils.protection.exception;

public class NotAFileException extends RuntimeException {

	private static final long serialVersionUID = 7381957011508162143L;

	public NotAFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAFileException(String message) {
		super(message);
	}
	
}
