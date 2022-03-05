package spring.security.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetail {

    private Date timestamp;

    private String message;

    private Object detail;

    private String path;

}
