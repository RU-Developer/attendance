package yonam.attendence.domain.message;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ValidationForm {
    @NotEmpty
    private String validationCode;

    public ValidationForm(String validationCode) {
        this.validationCode = validationCode;
    }
}
