package yonam.attendence.web.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ParentLoginResult {

    @NotEmpty
    private boolean success;

    public ParentLoginResult(boolean success) {
        this.success = success;
    }
}
