package pdfutils.protection.exception;

public class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 7820654930706447568L;

	public InvalidPasswordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidPasswordException(String arg0) {
		super(arg0);
	}

}
