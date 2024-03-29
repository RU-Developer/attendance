package yonam.attendence.domain.parent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import yonam.attendence.domain.AbstractRepository;

import javax.sql.DataSource;
import java.sql.*;

@Slf4j
@Repository
public class ParentRepository extends AbstractRepository {


    public ParentRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Parent save(Parent parent) {
        String sql = "INSERT INTO parent(name, phone) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, parent.getName());
            pstmt.setString(2, parent.getPhone());
            pstmt.executeUpdate();

            return parent;
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt, null);
        }

        return null;
    }

    public Parent findById(Long id) {
        String sql = "SELECT * FROM parent WHERE id = ?";

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
                parent.setId(rs.getLong("id"));
                parent.setName(rs.getString("name"));
                parent.setPhone(rs.getString("phone"));
                return parent;
            }
        } catch (SQLException e) {
            log.error("parent db error", e);
        } finally {
            close(con, pstmt, rs);
        }

        return null;
    }

    public Parent findByPhone(String phone) {
        String sql = "SELECT * FROM parent WHERE phone = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, phone);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Parent parent = new Parent();
                parent.setId(rs.getLong("id"));
                parent.setName(rs.getString("name"));
                parent.setPhone(rs.getString("phone"));
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
        String sql = "UPDATE parent SET name = ?, phone = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, parent.getName());
            pstmt.setString(2, parent.getPhone());
            pstmt.setLong(3, parent.getId());
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
        } finally {
            close(con, pstmt ,null);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM parent WHERE id = ?";

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
}
