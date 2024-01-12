package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.transform.Scale;
import org.springframework.security.crypto.bcrypt.BCrypt;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    @FXML
    private CheckBox showPassword;
    @FXML
    private TextField txtShowPassword;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        conn = Database.connect();
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
    }

    @FXML
    protected void onBtnLoginAction(ActionEvent event) {
        String strPassword = (showPassword.isSelected()) ? txtShowPassword.getText() : txtPassword.getText();
        if(btnLogin.isDisable()){
            return;
        }
        try {
            ps = conn.prepareStatement(Database.Query.getAccount);
            ps.setString(1, txtStudentNo.getText());
            rs = ps.executeQuery();

            if(!rs.next() || !BCrypt.checkpw(strPassword, rs.getString("password"))){
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
    protected void onTxtPasswordKeyTyped(KeyEvent event){
        txtShowPassword.setText(txtPassword.getText());
        onTxtFieldAction(event);
    }
    @FXML
    protected void onTxtShowPasswordKeyTyped(KeyEvent event){
        txtPassword.setText(txtShowPassword.getText());
        onTxtFieldAction(event);
    }
    @FXML
    protected void onTxtFieldAction(KeyEvent event) {
        btnLogin.setDisable(txtStudentNo.getText().isBlank() || txtPassword.getText().isBlank() || txtShowPassword.getText().isBlank());
    }
    @FXML
    protected void onShowPasswordSelectedAction(ActionEvent event){
        if(showPassword.isSelected())
            txtShowPassword.setText(txtPassword.getText());
        else
            txtPassword.setText(txtShowPassword.getText());
        txtShowPassword.setVisible(showPassword.isSelected());
        txtPassword.setVisible(!showPassword.isSelected());
    }

}