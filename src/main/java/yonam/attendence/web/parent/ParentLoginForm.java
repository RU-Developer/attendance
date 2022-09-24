package yonam.attendence.web.parent;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ParentLoginForm {
    @NotEmpty
    private String parentPhone;

    @NotEmpty
    private String studentPhone;
}
