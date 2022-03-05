package spring.security.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    private String access_token;

    private Integer expires_in;

    private Integer refresh_expires_in;

    private String refresh_token;

    private String token_type;

    private String session_state;

    private String scope;

}
