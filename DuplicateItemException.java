package redBlackTrees;;

// Simple Custom Runtime class for duplicate items
@SuppressWarnings("serial")
public class DuplicateItemException extends RuntimeException {

    // Default Constructor
    public DuplicateItemException() {}

    public DuplicateItemException(String message) {
        super(message);
    }
}