package yonam.attendence.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final DataSource dataSource;

    public Student save(Student student) throws SQLException {
        String sql = "INSERT INTO student(name, phone, tuition, reg_date, parent_id, teacher_lesson) VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getPhone());
            pstmt.setLong(3, student.getTuition());
            pstmt.setDate(4, Date.valueOf(student.getRegdate()));
            pstmt.setLong(5, student.getParentId());
            pstmt.setLong(6, student.getTeacherLesson());
            pstmt.executeUpdate();
            return student;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Student findById(Long id) {
        String sql = "SELECT * FROM student WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setPhone(rs.getString("phone"));
                student.setTuition(rs.getLong("tuition"));
                student.setRegdate(rs.getDate("regdate").toLocalDate());
                student.setParentId(rs.getLong("parent_id"));
                student.setTeacherLesson(rs.getLong("teacher_lesson"));
                return student;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Student student) {
        String sql = "UPDATE student SET name = ?, phone = ?, tuition = ?, regdate = ?, parent_id = ?, teacher_lesson = ?  WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getPhone());
            pstmt.setLong(3, student.getTuition());
            pstmt.setDate(4, Date.valueOf(student.getRegdate()));
            pstmt.setLong(5, student.getParentId());
            pstmt.setLong(6, student.getTeacherLesson());
            pstmt.setLong(7, student.getId());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM student WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
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
