package spring.security.oauth2.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "enable")
    private boolean enable;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Số điện thoại chỉ chứa 10 chữ số")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email sai định dạng")
    @Column(name = "email")
    private String email;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate = new Date();

    @Column(name = "modified_date")
    @LastModifiedDate
    private Date modifiedDate = new Date();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

