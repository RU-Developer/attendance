package yonam.attendence.domain.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.parent.Parent;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final DataSource dataSource;

    public Student save(Student student) {
        String sql = "INSERT INTO student(name, phone, tuition, reg_date, parent_id, teacher_lesson) VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getPhone());
            pstmt.setLong(3, student.getTuition());
            pstmt.setDate(4, student.getRegDate() == null ? null :
                    Date.valueOf(student.getRegDate()));
            pstmt.setLong(5, student.getParentId());
            pstmt.setLong(6, student.getTeacherLesson());
            pstmt.executeUpdate();
            return student;
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }

        return null;
    }

    public Student findById(Long id) {
        String sql = "SELECT * FROM student WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setName(rs.getString("name"));
                student.setPhone(rs.getString("phone"));
                student.setTuition(rs.getLong("tuition"));
                student.setRegDate(rs.getDate("reg_date") == null ? null :
                        rs.getDate("reg_date").toLocalDate());
                student.setParentId(rs.getLong("parent_id"));
                student.setTeacherLesson(rs.getLong("teacher_lesson"));
                return student;
            }
        } catch (SQLException e) {
            log.error("student db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public List<Student> findByTeacherLesson(Long teacherLesson) {
        String sql = "SELECT * FROM student WHERE teacher_lesson = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, teacherLesson);
            rs = pstmt.executeQuery();

            List<Student> students = new LinkedList<>();

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getLong("id"));
                student.setPhone(rs.getString("phone"));
                student.setName(rs.getString("name"));
                student.setParentId(rs.getLong("parent_id"));
                student.setTuition(rs.getLong("tuition"));
                student.setRegDate(rs.getDate("reg_date") == null ? null :
                        rs.getDate("reg_date").toLocalDate());
                student.setTeacherLesson(rs.getLong("teacher_lesson"));
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

    public List<Student> findByParentId(Long parentId) {
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
                student.setPhone(rs.getString("phone"));
                student.setName(rs.getString("name"));
                student.setParentId(rs.getLong("parent_id"));
                student.setTuition(rs.getLong("tuition"));
                student.setRegDate(rs.getDate("reg_date") == null ? null :
                        rs.getDate("reg_date").toLocalDate());
                student.setTeacherLesson(rs.getLong("teacher_lesson"));
                students.add(student);
            }

            return students;
        } catch (SQLException e) {
            log.error("student db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public List<StudentParent> studentParentByTeacherLesson(Long teacherLesson) {
        String sql = "SELECT s.id, s.phone, s.name, s.tuition, s.reg_date, s.teacher_lesson, p.id, p.phone, p.name " +
                "FROM student s, parent p " +
                "WHERE s.teacher_lesson = ? AND s.parent_id = p.id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, teacherLesson);
            rs = pstmt.executeQuery();

            List<StudentParent> studentParentAttendances = new LinkedList<>();

            while (rs.next()) {
                Student student = new Student();
                Parent parent = new Parent();

                student.setId(rs.getLong("s.id"));
                student.setName(rs.getString("s.name"));
                student.setPhone(rs.getString("s.phone"));
                student.setParentId(rs.getLong("p.id"));
                student.setTuition(rs.getLong("s.tuition"));
                student.setRegDate(rs.getDate("reg_date") == null ? null :
                        rs.getDate("reg_date").toLocalDate());
                student.setTeacherLesson(rs.getLong("s.teacher_lesson"));

                parent.setId(rs.getLong("p.id"));
                parent.setPhone(rs.getString("p.phone"));
                parent.setName(rs.getString("p.name"));

                StudentParent studentParentAttendance = new StudentParent(student, parent);
                studentParentAttendances.add(studentParentAttendance);
            }

            return studentParentAttendances;
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(Student student) {
        String sql = "UPDATE student SET name = ?, phone = ?, tuition = ?, reg_date = ?, parent_id = ?, teacher_lesson = ?  WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getPhone());
            pstmt.setLong(3, student.getTuition());
            pstmt.setDate(4, student.getRegDate() == null ? null :
                    Date.valueOf(student.getRegDate()));
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
