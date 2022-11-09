package yonam.attendence.domain.lyceum;

import yonam.attendence.domain.AbstractRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LyceumRepository extends AbstractRepository {

    public LyceumRepository(DataSource dataSource) {
        super(dataSource);
    }

    public Lyceum save(Lyceum lyceum) throws SQLException {
        String sql = "INSERT INTO lyceum(name, address, phone) VALUES(?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, lyceum.getName());
            pstmt.setString(2, lyceum.getAddress());
            pstmt.setString(3, lyceum.getPhone());
            pstmt.executeUpdate();
            return lyceum;
        } catch (SQLException e) {
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Lyceum findById(Long id) {
        String sql = "SELECT * FROM lyceum WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Lyceum lyceum = new Lyceum();
                lyceum.setId(id);
                lyceum.setAddress(rs.getString("address"));
                lyceum.setName(rs.getString("name"));
                lyceum.setPhone(rs.getString("phone"));
                return lyceum;
            }
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, rs);
        }
        return null;
    }

    public void update(Lyceum lyceum) {
        String sql = "UPDATE lyceum SET name = ?, address = ?, phone = ? WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, lyceum.getName());
            pstmt.setString(2, lyceum.getAddress());
            pstmt.setString(3, lyceum.getPhone());
            pstmt.setLong(4, lyceum.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, null);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM lyceum WHERE id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            close(con, pstmt, null);
        }
    }
}
