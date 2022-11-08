package yonam.attendence.domain.message;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class ValidationForm implements Serializable {
    @NotEmpty
    private String validationCode;

    public ValidationForm(String validationCode) {
        this.validationCode = validationCode;
    }
}
