package yonam.attendence.web.teacher;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TeacherAddResult {

    @NotEmpty
    private boolean success;

    public TeacherAddResult(boolean success) {
        this.success = success;
    }
}
