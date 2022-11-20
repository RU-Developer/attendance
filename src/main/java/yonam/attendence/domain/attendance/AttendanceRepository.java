package yonam.attendence.domain.attendance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.AbstractRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
public class AttendanceRepository extends AbstractRepository {

    public AttendanceRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Attendance save(Attendance attendance) {
        String sql = "INSERT INTO attendance(date_attendance, student_id) VALUES(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setLong(2, attendance.getStudentId());
            pstmt.executeUpdate();

            return attendance;
        } catch (SQLException e) {
            log.error("AttendanceRepository.save() error : ", e);
        } finally {
            close(con, pstmt, null);
        }

        return null;
    }

    public Attendance findById(Long id) {
        String sql = "SELECT * FROM attendance WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getLong("id"));
                attendance.setDateAttendance(rs.getDate("date_attendance") == null ? null :
                        rs.getDate("date_attendance").toLocalDate());
                attendance.setStudentId(rs.getLong("student_id"));
                return attendance;
            }
        } catch (SQLException e) {
            log.error("AttendanceRepository.findById() error : ", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public List<Attendance> findByStudentId(Long studentId) {
        String sql = "SELECT * FROM attendance WHERE student_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, studentId);
            rs = pstmt.executeQuery();

            List<Attendance> attendances = new LinkedList<>();

            while (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getLong("id"));
                attendance.setDateAttendance(rs.getDate("date_attendance") == null ? null :
                        rs.getDate("date_attendance").toLocalDate());
                attendance.setStudentId(rs.getLong("student_id"));
                attendances.add(attendance);
            }

            return attendances;
        } catch (SQLException e) {
            log.error("AttendanceRepository.findByStudentId() error : ", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public Attendance findByDateAttendanceWithStudentId(LocalDate dateAttendance, Long studentId) {
        String sql = "SELECT * FROM attendance WHERE date_attendance = ? AND student_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, dateAttendance == null ? null :
                    Date.valueOf(dateAttendance));
            pstmt.setLong(2, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Attendance attendance = new Attendance();
                attendance.setId(rs.getLong("id"));
                attendance.setDateAttendance(rs.getDate("date_attendance") == null ? null :
                        rs.getDate("date_attendance").toLocalDate());
                attendance.setStudentId(rs.getLong("student_id"));
                return attendance;
            }
        } catch (SQLException e) {
            log.error("AttendanceRepository.findByDateAttendanceWithStudentId() error : ", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void updateDateAttendance(Attendance attendance) {
        String sql = "UPDATE attendance SET date_attendance = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setLong(2, attendance.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("AttendanceRepository.update() error : ", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void update(Attendance attendance) {
        String sql = "UPDATE attendance SET date_attendance = ?, student_id = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setLong(2, attendance.getStudentId());
            pstmt.setLong(3, attendance.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("AttendanceRepository.update() error : ", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM attendance WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("AttendanceRepository.delete() error : ", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void deleteByStudentId(Long studentId) {
        String sql = "DELETE FROM attendance WHERE student_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("AttendanceRepository.deleteByStudentId() error : ", e);
        } finally {
            close(con, pstmt, null);
        }
    }
}
