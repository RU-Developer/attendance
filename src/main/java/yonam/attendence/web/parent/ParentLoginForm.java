package yonam.attendence.web.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ParentLoginForm implements Serializable {
    @NotEmpty
    private String phone;
}
