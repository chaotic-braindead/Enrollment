package plm.rafaeltorres.irregularenrollmentsystem.db;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.Map;
public final class Database {
    public static final class Query {
        public static final String getAllStudents = "SELECT * FROM STUDENT";
        public static final String getStudent = "SELECT * FROM STUDENT WHERE STUDENT_NO = ?";

    }
    public static Connection connect(){
        Connection conn = null;
        Map<String, String> env = System.getenv();
        String db = "oopdump";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,
                    env.get("mysqluser"),
                    env.get("mysqlpass"));
            System.out.println("[Database]: Successfully connected to "+db);
        } catch(Exception e) {
            System.out.println(e);
        }
        return conn;
    }
    public static void generatePassword(){
        Connection conn = connect();
        try {
            PreparedStatement ps = conn.prepareStatement(Query.getAllStudents);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                ps = conn.prepareStatement("UPDATE STUDENT SET PASSWORD = ? WHERE STUDENT_NO = ?");
                ps.setString(1, BCrypt.hashpw("password", BCrypt.gensalt()));
                ps.setString(2, rs.getString("STUDENT_NO"));
            }
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
