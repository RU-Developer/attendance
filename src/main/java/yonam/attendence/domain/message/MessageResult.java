package yonam.attendence.domain.message;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MessageResult {
    @NotEmpty
    private boolean success;
    @NotEmpty
    private String message;

    public MessageResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
