package spring.security.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import spring.security.oauth2.model.User;
import spring.security.oauth2.util.PasswordUtil;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${rest.services.user}")
    private String userEndpoint;

    private final HttpHeadersService httpHeadersService;

    private static Map<String, Object> buildCredentialsMap(String password) {
        Map<String, Object> credentialsMap = new HashMap<>();
        credentialsMap.put("type", "password");
        credentialsMap.put("value", password);
        credentialsMap.put("temporary", false);
        return credentialsMap;
    }

    private static User buildUser(Map<String, Object> map) {
        return User.builder()
                .id(Optional.ofNullable(map.getOrDefault("id", null)).map(Objects::toString).orElse(null))
                .username(Optional.ofNullable(map.getOrDefault("username", null)).map(Objects::toString).orElse(null))
                .firstName(Optional.ofNullable(map.getOrDefault("firstName", null)).map(Objects::toString).orElse(null))
                .lastName(Optional.ofNullable(map.getOrDefault("lastName", null)).map(Objects::toString).orElse(null))
                .email(Optional.ofNullable(map.getOrDefault("email", null)).map(Objects::toString).orElse(null))
                .enabled(Optional.ofNullable(map.getOrDefault("enabled", null)).map(obj -> Boolean.valueOf(obj.toString())).orElse(false))
                .createdDate(Optional.ofNullable(map.getOrDefault("createdTimestamp", null)).map(obj -> new Date(Long.parseLong(obj.toString()))).orElse(null))
                .build();
    }

    private static Map buildUserMap(User user) {
        ObjectMapper mapper = new ObjectMapper();
        Map userMap = mapper.convertValue(user, Map.class);
        userMap.remove("createdDate");
        return userMap;
    }

    public Optional<User> findByQuery(String query, String value) {
        try {
            HttpHeaders headers = httpHeadersService.buildHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(userEndpoint)
                    .queryParam("first", 0)
                    .queryParam("max", 1)
                    .queryParam("exact", true)
                    .query("\"" + query + "\"=" + value);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.build(false).toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            JsonParser parser = JsonParserFactory.getJsonParser();
            return parser.parseList(response.getBody())
                    .stream()
                    .findFirst()
                    .map(obj -> buildUser((Map<String, Object>) obj));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    public void create(User user) {
        try {
            Map<String, Object> userMap = buildUserMap(user);
            String password = PasswordUtil.generatePassword();
            userMap.put("credentials", Collections.singletonList(buildCredentialsMap(password)));
            HttpHeaders headers = httpHeadersService.buildHeaders();
            HttpEntity<?> entity = new HttpEntity<>(userMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    userEndpoint,
                    HttpMethod.POST,
                    entity,
                    String.class);
            if (response.getStatusCode() == HttpStatus.CREATED && this.findByQuery("username", user.getUsername()).isPresent()) {
                this.findByQuery("username", user.getUsername()).get();
            }
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw exception;
        }
    }

}
