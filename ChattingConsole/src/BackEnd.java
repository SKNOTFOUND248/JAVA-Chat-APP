import java.sql.*;
import java.util.Vector;
import javax.swing.JOptionPane;

public class BackEnd {
    public static void Connect(Vector<String> l) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = " INSERT INTO users (name,ip_address) VALUES (?,?)";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shreyash", "root", "shreekb2");
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, l.get(0));
            ps.setString(2, l.get(1));
            ps.executeUpdate();
        }
        JOptionPane.showConfirmDialog(null, "Information is Regestered successfully!!! ",
                "INFORMATION", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);

    }

    public static Vector<String> Fetch_Info() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String sql = "SELECT name FROM users";
        Vector<String> vcc = new Vector<>();

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shreyash", "root", "shreekb2");
                Statement ps = con.createStatement();) {

            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                vcc.add(rs.getString(1));
            }
        }
        return vcc;

    }
    public static String Fetch_IP(String Name) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String sql = "SELECT ip_address FROM users WHERE name = ?";
    
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/shreyash", "root", "shreekb2");
                    PreparedStatement ps = con.prepareStatement(sql)) {
    
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getString(1);
            }
        } catch (ClassNotFoundException | SQLException e) {
        }
        return null;
    }
}
