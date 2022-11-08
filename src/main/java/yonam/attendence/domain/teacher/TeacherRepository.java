package yonam.attendence.domain.teacher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.AbstractRepository;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Repository
public class TeacherRepository extends AbstractRepository {

    public TeacherRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Teacher save(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO teacher(id, password, name) VALUES (?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, teacher.getId());
            pstmt.setString(2, teacher.getPassword());
            pstmt.setString(3, teacher.getName());
            pstmt.executeUpdate();

            return teacher;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Teacher findByLesson(Long lesson) {
        String sql = "SELECT * FROM teacher WHERE lesson = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, lesson);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setLesson(rs.getLong("lesson"));
                teacher.setId(rs.getString("id"));
                teacher.setPassword(rs.getString("password"));
                teacher.setName(rs.getString("name"));
                return teacher;
            }
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public Teacher findById(String id) {
        String sql = "SELECT * FROM teacher WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setLesson(rs.getLong("lesson"));
                teacher.setId(rs.getString("id"));
                teacher.setPassword(rs.getString("password"));
                teacher.setName(rs.getString("name"));
                return teacher;
            }

        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Teacher teacher) {
        String sql = "UPDATE teacher SET id = ?, password = ?, name = ? WHERE lesson = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, teacher.getId());
            pstmt.setString(2, teacher.getPassword());
            pstmt.setString(3, teacher.getName());
            pstmt.setLong(4, teacher.getLesson());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long lesson) {
        String sql = "DELETE FROM teacher WHERE lesson = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, lesson);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }
}
