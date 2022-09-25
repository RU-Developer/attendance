package yonam.attendence.domain.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.student.Student;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ParentRepository {

    private final DataSource dataSource;

    public Parent save(Parent parent) throws SQLException {
        String sql = "INSERT INTO parent(parent_name, parent_phone, student_student_id) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, parent.getName());
            pstmt.setString(2, parent.getPhone());
            pstmt.setLong(3, parent.getStudentId());
            pstmt.executeUpdate();

            return parent;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Parent findById(Long id) {
        String sql = "SELECT * FROM parent WHERE parent_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Parent parent = new Parent();
                parent.setId(rs.getLong("parent_id"));
                parent.setName(rs.getString("parent_name"));
                parent.setPhone(rs.getString("parent_phone"));
                parent.setStudentId(rs.getLong("student_student_id"));
                return parent;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public Parent findByNamePhone(String name, String phone) {
        String sql = "SELECT * FROM parent WHERE parent_name = ? AND parent_phone = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Parent parent = new Parent();
                parent.setId(rs.getLong("parent_id"));
                parent.setName(rs.getString("parent_name"));
                parent.setPhone(rs.getString("parent_phone"));
                parent.setStudentId(rs.getLong("student_student_id"));
                return parent;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Parent parent) {
        String sql = "UPDATE parent SET parent_name = ?, parent_phone = ?, student_student_id = ? WHERE parent_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, parent.getName());
            pstmt.setString(2, parent.getPhone());
            pstmt.setLong(3, parent.getStudentId());
            pstmt.setLong(4, parent.getId());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt ,null);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM parent WHERE parent_id = ?";

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
