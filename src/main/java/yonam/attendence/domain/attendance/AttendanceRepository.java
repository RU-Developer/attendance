package yonam.attendence.domain.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final DataSource dataSource;

    public Attendance save(Attendance attendance) {
        String sql = "INSERT INTO attendance(date_attendance, in_time, out_time, student_id) VALUES(?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));

            pstmt.setTimestamp(2, attendance.getInTime() == null ? null :
                    Timestamp.valueOf(attendance.getInTime()));
            pstmt.setTimestamp(3, attendance.getOutTime() == null ? null :
                    Timestamp.valueOf(attendance.getOutTime()));
            pstmt.setLong(4, attendance.getStudentId());
            pstmt.executeUpdate();

            return attendance;
        } catch (SQLException e) {
            log.error("attendance db error", e);
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
                attendance.setInTime(rs.getTimestamp("in_time") == null ? null :
                        rs.getTimestamp("in_time").toLocalDateTime());
                attendance.setOutTime(rs.getTimestamp("out_time") == null ? null :
                        rs.getTimestamp("out_time").toLocalDateTime());
                attendance.setStudentId(rs.getLong("student_id"));
                return attendance;
            }
        } catch (SQLException e) {
            log.error("db error", e);
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
                attendance.setInTime(rs.getTimestamp("in_time") == null ? null :
                        rs.getTimestamp("in_time").toLocalDateTime());
                attendance.setOutTime(rs.getTimestamp("out_time") == null ? null :
                        rs.getTimestamp("out_time").toLocalDateTime());
                attendance.setStudentId(rs.getLong("student_id"));
                attendances.add(attendance);
            }

            return attendances;
        } catch (SQLException e) {
            log.error("db error", e);
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
                attendance.setInTime(rs.getTimestamp("in_time") == null ? null :
                        rs.getTimestamp("in_time").toLocalDateTime());
                attendance.setOutTime(rs.getTimestamp("out_time") == null ? null :
                        rs.getTimestamp("out_time").toLocalDateTime());
                attendance.setStudentId(rs.getLong("student_id"));
                return attendance;
            }
        } catch (SQLException e) {
            log.error("attendance find by dateAttendance db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Attendance attendance) {
        String sql = "UPDATE attendance SET date_attendance = ?, in_time = ?, out_time = ?, student_id = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setTimestamp(2, attendance.getInTime() == null ? null :
                    Timestamp.valueOf(attendance.getInTime()));
            pstmt.setTimestamp(3, attendance.getOutTime() == null ? null :
                    Timestamp.valueOf(attendance.getOutTime()));
            pstmt.setLong(4, attendance.getStudentId());
            pstmt.setLong(5, attendance.getId());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void updateInTime(Attendance attendance) {
        String sql = "UPDATE attendance SET in_time = ? WHERE date_attendance = ? AND student_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, attendance.getInTime() == null ? null :
                    Timestamp.valueOf(attendance.getInTime()));
            pstmt.setDate(2, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setLong(3, attendance.getStudentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("updateInTime error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void updateOutTime(Attendance attendance) {
        String sql = "UPDATE attendance SET out_time = ? WHERE date_attendance = ? AND student_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, attendance.getOutTime() == null ? null :
                    Timestamp.valueOf(attendance.getOutTime()));
            pstmt.setDate(2, attendance.getDateAttendance() == null ? null :
                    Date.valueOf(attendance.getDateAttendance()));
            pstmt.setLong(3, attendance.getStudentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("update OutTime error", e);
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
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
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
            log.error("delete By StudentId attendance db error");
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get Connection={}, class={}", con, con.getClass());
        return con;
    }
}
