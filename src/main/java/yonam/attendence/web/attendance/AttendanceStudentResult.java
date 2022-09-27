package yonam.attendence.web.attendance;

import lombok.Data;
import yonam.attendence.domain.attendance.Attendance;
import yonam.attendence.domain.student.Student;

import java.util.List;

@Data
public class AttendanceStudentResult {
    private Student student;
    private List<Attendance> attendances;

    public AttendanceStudentResult(Student student, List<Attendance> attendances) {
        this.student = student;
        this.attendances = attendances;
    }
}
