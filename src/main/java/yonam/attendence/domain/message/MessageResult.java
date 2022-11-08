package yonam.attendence.domain.message;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class MessageResult implements Serializable {
    @NotEmpty
    private boolean success;
    @NotEmpty
    private String message;

    public MessageResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
