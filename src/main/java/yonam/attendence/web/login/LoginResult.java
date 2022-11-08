package yonam.attendence.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class LoginResult implements Serializable {

    @NotEmpty
    private boolean success;

    public LoginResult(boolean success) {
        this.success = success;
    }
}
