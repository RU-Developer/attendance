package yonam.attendence.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginResult {

    @NotEmpty
    private boolean success;

    public LoginResult(boolean success) {
        this.success = success;
    }
}