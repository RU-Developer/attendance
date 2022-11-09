package yonam.attendence.domain.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginForm implements Serializable {

    @NotEmpty
    private String id;

    @NotEmpty
    private String password;
}
