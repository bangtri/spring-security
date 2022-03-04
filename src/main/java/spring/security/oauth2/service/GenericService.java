package spring.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.security.oauth2.config.MessageTemplate;
import spring.security.oauth2.exception.EntityValidationException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenericService {

    private final MessageTemplate messageTemplate;

    public String message(String key, String... value) {
        return messageTemplate.message(key, value);
    }

    public void fieldValidation(String field, String errorKey) throws EntityValidationException {
        Map<String, String> errors = new HashMap<>();
        errors.put(field, message(errorKey));
        throw new EntityValidationException(message("error.validation"), errors);
    }

}
