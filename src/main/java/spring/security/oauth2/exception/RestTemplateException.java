package spring.security.oauth2.exception;

import java.io.IOException;

public class RestTemplateException extends IOException {

    public RestTemplateException(String message) {
        super(message);
    }

}
