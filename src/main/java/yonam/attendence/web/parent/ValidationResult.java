package yonam.attendence.web.parent;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidationResult implements Serializable {
    private boolean success;

    public ValidationResult(boolean success) {
        this.success = success;
    }
}
