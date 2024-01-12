package plm.rafaeltorres.irregularenrollmentsystem.db;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public final class Database {
    private static final String DB = "enrollment_system";
    public static Connection connect(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DB,
                    "oop",
                    "oop");
        } catch(Exception e) {
            AlertMessage.showInformationAlert("An error occurred while connecting to database: " + e);
        }
        return conn;
    }

    public static final class Query {
        public static final String getAccount = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NO = ?";
        public static final String updateImage = "UPDATE ACCOUNT SET IMAGE = ? WHERE ACCOUNT_NO = ?";
        public static final String getStudentAccount = "select s.student_no," +
                "s.firstname," +
                "s.lastname," +
                "s.gender," +
                "s.bday," +
                "s.age," +
                "s.address," +
                "s.cp_num," +
                "s.email," +
                "s.plm_email," +
                "s.college_code," +
                "s.course_code," +
                "s.status," +
                "s.registration_status," +
                "a.image " +
                "from account a " +
                "inner join vwstudentinfo s on a.account_no = s.student_no where s.student_no = ?";

        public static final String getEmployeeAccount = "SELECT * FROM VWEMPLOYEEACCOUNT WHERE EMPLOYEE_ID = ?";
    }
    public static List<String> fetch(String query){
        List<String> res = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = connect();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next())
                res.add(rs.getString(1));
        } catch(Exception e){
            System.out.println(e);
        }
        return res;
    }
}

