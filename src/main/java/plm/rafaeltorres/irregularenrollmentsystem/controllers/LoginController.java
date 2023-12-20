package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.security.crypto.bcrypt.BCrypt;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import plm.rafaeltorres.irregularenrollmentsystem.model.Employee;
import plm.rafaeltorres.irregularenrollmentsystem.model.Student;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;

import java.sql.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private TextField txtStudentNo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblDateNow;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conn = Database.connect();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
    }

    @FXML
    protected void onBtnLoginAction(ActionEvent event) {
        try {
            ps = conn.prepareStatement(Database.Query.getAccount);
            ps.setString(1, txtStudentNo.getText());
            rs = ps.executeQuery();

            if(!rs.next() || !BCrypt.checkpw(txtPassword.getText(), rs.getString("password"))){
                AlertMessage.showErrorAlert("Incorrect student number/password.");
                return;
            }
            String query = Database.Query.getStudentAccount;
            boolean isAdmin = false;
            if(rs.getString("type").equals("A")){
                query = Database.Query.getEmployeeAccount;
                isAdmin = true;
            }

            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentNo.getText());
            rs = ps.executeQuery();
            rs.next();


            if(!isAdmin)
                SceneSwitcher.switchScene(event, "StudentDashboard.fxml",
                    new Student(rs));
            else
                SceneSwitcher.switchScene(event, "AdminDashboard.fxml",
                        new Employee(rs));


        } catch(Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void onTxtFieldAction(KeyEvent event) {
        btnLogin.setDisable(txtStudentNo.getText().isBlank() || txtPassword.getText().isBlank());
    }

}