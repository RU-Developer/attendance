package yonam.attendence.web.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class PhoneValidationForm implements Serializable {
    @NotEmpty
    private String phone;
}
