package py.jere.agendate.controller.utils.archivos;

public class FileStorageException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7817383401904670716L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}