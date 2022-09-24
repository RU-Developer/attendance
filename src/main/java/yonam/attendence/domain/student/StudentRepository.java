package yonam.attendence.domain.student;

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
public class StudentRepository {

    private final DataSource dataSource;

    public Student save(Student student) throws SQLException {
        String sql = "INSERT INTO student(id, parent_id, phone, name) VALUES (?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, student.getId());
            pstmt.setLong(2, student.getParentId());
            pstmt.setString(3, student.getPhone());
            pstmt.setString(4, student.getName());
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
                student.setParentId(rs.getLong("parent_id"));
                student.setPhone(rs.getString("phone"));
                student.setName(rs.getString("name"));
                return student;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public Student findByPhone(String phone) {
        String sql = "SELECT * FROM student WHERE phone = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setPhone(rs.getString("phone"));
                student.setParentId(rs.getLong("parent_id"));
                student.setName(rs.getString("name"));
                return student;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public List<Student> findByParendId(Long parentId) {
        String sql = "SELECT * FROM student WHERE parent_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, parentId);
            rs = pstmt.executeQuery();
            List<Student> students = new LinkedList<>();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setParentId(rs.getLong("parent_id"));
                student.setPhone(rs.getString("phone"));
                student.setName(rs.getString("name"));
                students.add(student);
            }

            return students;
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Student student) {
        String sql = "UPDATE student SET parent_id = ?, phone = ?, name = ?,  WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, student.getParentId());
            pstmt.setString(2, student.getPhone());
            pstmt.setString(3, student.getName());
            pstmt.setLong(4, student.getId());
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
