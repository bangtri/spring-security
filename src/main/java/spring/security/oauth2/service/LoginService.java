package spring.security.oauth2.service;

import com.fasterxml.jackson.core.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import spring.security.oauth2.exception.RestTemplateException;
import spring.security.oauth2.model.Login;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String client_id;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String client_secret;

    @Value("${rest.services.token}")
    private String tokenUri;

    private final GenericService genericService;

    public String login(Login login) throws RestTemplateException {
        try {
            HttpHeaders headers = new HttpHeaders();
            RestTemplate restTemplate = new RestTemplate();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("client_id", client_id);
            multiValueMap.add("client_secret", client_secret);
            multiValueMap.add("username", login.getUsername());
            multiValueMap.add("password", login.getPassword());
            multiValueMap.add("grant_type", "password");
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, headers);
            return restTemplate.exchange(tokenUri, HttpMethod.POST, entity, String.class).getBody();
        }catch (Exception exception) {
            throw new RestTemplateException(genericService.message("invalid.credentials"));
        }
    }

}
