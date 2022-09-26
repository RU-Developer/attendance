package yonam.attendence.domain.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final DataSource dataSource;

    public Attendance save(Attendance attendance) throws SQLException {
        String sql = "INSERT INTO attendance(date_attendance, in_time, out_time, student_id) VALUES(?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(attendance.getDateAttendance()));
            pstmt.setTimestamp(2, Timestamp.valueOf(attendance.getInTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(attendance.getOutTime()));
            pstmt.setLong(4, attendance.getStudent_id());
            pstmt.executeUpdate();

            return attendance;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
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
                attendance.setDateAttendance(rs.getDate("date_attendance").toLocalDate());
                attendance.setInTime(rs.getTimestamp("in_time").toLocalDateTime());
                attendance.setOutTime(rs.getTimestamp("out_time").toLocalDateTime());
                attendance.setStudent_id(rs.getLong("student_id"));
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
                attendance.setDateAttendance(rs.getDate("date_attendance").toLocalDate());
                attendance.setInTime(rs.getTimestamp("in_time").toLocalDateTime());
                attendance.setOutTime(rs.getTimestamp("out_time").toLocalDateTime());
                attendance.setStudent_id(rs.getLong("student_id"));
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

    public void update(Attendance attendance) {
        String sql = "UPDATE attendance SET date_attendance = ?, in_time = ?, out_time = ?, student_id = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(attendance.getDateAttendance()));
            pstmt.setTimestamp(2, Timestamp.valueOf(attendance.getInTime()));
            pstmt.setTimestamp(3, Timestamp.valueOf(attendance.getOutTime()));
            pstmt.setLong(4, attendance.getStudent_id());
            pstmt.setLong(5, attendance.getId());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
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
