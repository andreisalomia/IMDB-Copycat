public class InformationIncompleteException extends Exception {
    public InformationIncompleteException() {
        super("Information is incomplete.");
    }

    public InformationIncompleteException(String message) {
        super(message);
    }
}
