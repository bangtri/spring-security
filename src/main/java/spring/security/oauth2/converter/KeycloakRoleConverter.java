package spring.security.oauth2.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Stream<GrantedAuthority> resourceAccessStream = this.getGrantedAuthorities((Map<String, Object>) jwt.getClaims().get("resource_access"));
        Stream<GrantedAuthority> realmAccessStream = this.getGrantedAuthorities((Map<String, Object>) jwt.getClaims().get("realm_access"));
        return Stream.concat(resourceAccessStream, realmAccessStream).collect(Collectors.toList());
    }

    private Stream<GrantedAuthority> getGrantedAuthorities(Map<String, Object> map) {
        return map.values().stream()
                .map(v -> {
                    List<String> list;
                    if (v instanceof Map) {
                        list = (List<String>) ((Map<String, Object>) v).getOrDefault("roles", Collections.emptyList());
                    } else if (v instanceof List) {
                        list = (List<String>) v;
                        ;
                    } else {
                        list = new ArrayList<>();
                    }

                    return list;
                })
                .flatMap(List::stream)
                .map(SimpleGrantedAuthority::new);
    }
}
