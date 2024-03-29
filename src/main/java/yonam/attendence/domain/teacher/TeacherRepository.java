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
        String sql = "INSERT INTO teacher(name, belong, phone, login_id, lyceum_id) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, teacher.getName());
            pstmt.setString(2, teacher.getBelong());
            pstmt.setString(3, teacher.getPhone());
            pstmt.setString(4, teacher.getLoginId());
            pstmt.setLong(5, teacher.getLyceumId());
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
                teacher.setName(rs.getString("name"));
                teacher.setBelong(rs.getString("belong"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setLoginId(rs.getString("login_id"));
                teacher.setLyceumId(rs.getLong("lyceum_id"));
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
        String sql = "SELECT * FROM teacher WHERE login_id = ?";

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
                teacher.setName(rs.getString("name"));
                teacher.setBelong(rs.getString("belong"));
                teacher.setPhone(rs.getString("phone"));
                teacher.setLoginId(rs.getString("login_id"));
                teacher.setLyceumId(rs.getLong("lyceum_id"));
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
        String sql = "UPDATE teacher SET name = ?, belong = ?, phone = ?, login_id = ?, lyceum_id = ? WHERE lesson = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, teacher.getName());
            pstmt.setString(2, teacher.getBelong());
            pstmt.setString(3, teacher.getLoginId());
            pstmt.setLong(4, teacher.getLyceumId());
            pstmt.setLong(5, teacher.getLesson());
            pstmt.executeUpdate();
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
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }
    }
}
