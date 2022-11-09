package yonam.attendence.domain.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.AbstractRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
public class LoginRepository extends AbstractRepository {
    public LoginRepository(DataSource dataSource) {
        super(dataSource);
    }

    public LoginForm save(LoginForm loginForm) throws SQLException {
        String sql = "INSERT INTO login(id, password) VALUES(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginForm.getId());
            pstmt.setString(2, loginForm.getPassword());
            pstmt.executeUpdate();

            return loginForm;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public LoginForm findById(String id) {
        String sql = "SELECT * FROM login WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                LoginForm loginForm = new LoginForm();
                loginForm.setId(rs.getString("id"));
                loginForm.setPassword(rs.getString("password"));
                return loginForm;
            }
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public void update(LoginForm loginForm) {
        String sql = "UPDATE loginForm SET password = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginForm.getPassword());
            pstmt.setString(2, loginForm.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(String id) {
        String sql = "DELETE FROM login WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, null);
        }
    }
}
