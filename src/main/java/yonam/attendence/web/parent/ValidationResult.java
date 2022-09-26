package yonam.attendence.web.parent;

import lombok.Data;

@Data
public class ValidationResult {
    private boolean success;

    public ValidationResult(boolean success) {
        this.success = success;
    }
}
