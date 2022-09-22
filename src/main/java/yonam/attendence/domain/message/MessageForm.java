package yonam.attendence.domain.message;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MessageForm {
    @NotEmpty
    private String message;
    @NotEmpty
    private String to;

    public MessageForm(String message, String to) {
        this.message = message;
        this.to = to;
    }
}
