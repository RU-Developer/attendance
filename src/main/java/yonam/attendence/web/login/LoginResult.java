package yonam.attendence.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginResult {

    @NotEmpty
    private boolean isSuccess;

    public LoginResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
