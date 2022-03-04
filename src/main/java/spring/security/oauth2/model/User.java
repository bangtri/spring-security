package spring.security.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;

    @NotBlank
    private String username;

    private String firstName;

    private String lastName;

    @NotNull
    @Email
    private String email;

    private boolean enabled;

    private Date createdDate;

}
