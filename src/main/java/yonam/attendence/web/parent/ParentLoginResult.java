package yonam.attendence.web.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ParentLoginResult implements Serializable {

    @NotEmpty
    private boolean success;

    public ParentLoginResult(boolean success) {
        this.success = success;
    }
}
