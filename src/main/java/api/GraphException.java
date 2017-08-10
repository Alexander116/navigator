package api;

public class GraphException extends Exception {
    public GraphException() {
        super();
    }

    public GraphException(String s) {
        super(s);
    }

    public GraphException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GraphException(Throwable throwable) {
        super(throwable);
    }
}
