package spring.security.oauth2.exception;

public class QueryValidationException extends Exception {

    public QueryValidationException(String message) {
        super(message);
    }

    public QueryValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
