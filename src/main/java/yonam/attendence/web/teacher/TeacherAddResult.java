package yonam.attendence.web.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TeacherAddResult {

    @NotEmpty
    private boolean success;

    @NotEmpty
    private String message;

    public TeacherAddResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
