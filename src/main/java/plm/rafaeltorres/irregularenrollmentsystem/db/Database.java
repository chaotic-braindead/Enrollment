package plm.rafaeltorres.irregularenrollmentsystem.db;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Database {
    public static final class Query {
        public static final String getAllStudents = "SELECT * FROM STUDENT";
        public static final String getAllStudentInfo = "SELECT * FROM VWSTUDENT INFO";
        public static final String getStudent = "SELECT * FROM VWSTUDENTINFO WHERE STUDENT_NO = ?";
        public static final String getAccount = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NO = ?";
        public static final String getAllAccounts = "SELECT * FROM ACCOUNT";
        public static final String getAllEmployees = "SELECT * FROM EMPLOYEE";
        public static final String getEmployee = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
        public static final String updateImage = "UPDATE ACCOUNT SET IMAGE = ? WHERE ACCOUNT_NO = ?";
        public static final String getStudentAccount = "select s.student_number," +
                "s.first_name," +
                "s.last_name," +
                "s.gender," +
                "s.birthday," +
                "s.age," +
                "s.address," +
                "s.cellphone_number," +
                "s.personal_email," +
                "s.plm_email," +
                "s.college_code," +
                "s.course_code," +
                "s.status," +
                "s.registration_status," +
                "a.image " +
                "from account a " +
                "inner join vwstudentinfo s on a.account_no = s.student_number where s.student_number = ?";

        public static final String getEmployeeAccount = "SELECT * FROM VWEMPLOYEEACCOUNT WHERE EMPLOYEE_ID = ?";
    }
    public static Connection connect(){
        Connection conn = null;
        Map<String, String> env = System.getenv();
        String db = "enrollment_system";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db,
                    env.get("mysqluser"),
                    env.get("mysqlpass"));
//            System.out.println("[Database]: Successfully connected to "+db);
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
                ps = conn.prepareStatement("INSERT INTO ACCOUNT(account_no, password, type) VALUES (?, ?, ?)");
                ps.setString(1, rs.getString("student_no"));
                ps.setString(2, BCrypt.hashpw("password", BCrypt.gensalt()));
                ps.setString(3, "S");
                ps.executeUpdate();
            }

            ps = conn.prepareStatement(Query.getAllEmployees);
            rs = ps.executeQuery();
            while(rs.next()){
                ps = conn.prepareStatement("INSERT INTO ACCOUNT(account_no, password, type) VALUES (?, ?, ?)");
                ps.setString(1, rs.getString("employee_id"));
                ps.setString(2, BCrypt.hashpw("password", BCrypt.gensalt()));
                ps.setString(3, "A");
                ps.executeUpdate();
            }
            System.out.println("success");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public static void generateTableValues(){
        Connection conn = connect();
        List<String> groupmates = List.of("2022-34019", "2022-34115", "2022-34023", "2022-34037", "2022-34062", "2022-34078");
        List<String> cols = new ArrayList<>();
        Random random = new Random();
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT student_no, course_code from student");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                if(!rs.getString("student_no").equals("2022-34107") && rs.getString("course_code").equals("BSCS"))
                    cols.add(rs.getString("student_no"));
            }

            ps = conn.prepareStatement("SELECT sy, semester, subject_code, block_no FROM STUDENT_SCHEDULE WHERE STUDENT_NO = '2022-34107'");
            rs = ps.executeQuery();
            while(rs.next()){
                for(int i = 0; i < cols.size(); ++i){
                    ps = conn.prepareStatement("INSERT INTO STUDENT_SCHEDULE VALUES(?, ?, ?, ?, ?, ?)");
                    ps.setString(1, rs.getString("sy"));
                    ps.setString(2, rs.getString("semester"));
                    ps.setString(3, cols.get(i));
                    ps.setString(4, rs.getString("subject_code"));
                    ps.setString(5, "CET");
                    if(groupmates.contains(cols.get(i))){
                        ps.setString(6, rs.getString("block_no"));
                        ps.executeUpdate();
                        continue;
                    }

                    if(i%3 == 0)
                        ps.setString(6, "1");
                    else if(i%3 == 1)
                        ps.setString(6, "2");
                    else
                        ps.setString(6, "3");
                    ps.executeUpdate();
                }
            }
            System.out.println("Complete");

        } catch(Exception e){
            System.out.println(e);
        }
    }
    public static List<String> fetch(String query){
        List<String> res = new ArrayList<>();
        try{
            Connection conn = connect();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                res.add(rs.getString(1));
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return res;
    }
}

