package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import org.springframework.security.crypto.bcrypt.BCrypt;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;

import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends Controller {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    @FXML
    private TextField txtStudentNo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = Database.connect();
    }

    @FXML
    protected void onBtnLoginAction(ActionEvent event) {
        try {
            ps = conn.prepareStatement(Database.Query.getStudent);
            ps.setString(1, txtStudentNo.getText());
            rs = ps.executeQuery();

            if(!rs.next() || !BCrypt.checkpw(txtPassword.getText(), rs.getString("password"))){
                AlertMessage.showErrorAlert("Incorrect student number/password.");
                return;
            }

            SceneSwitcher.switchScene(event, "StudentDashboard.fxml",
                    rs.getString("STUDENT_NO"));


        } catch(Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void onTxtFieldAction(KeyEvent event) {
        btnLogin.setDisable(txtStudentNo.getText().isBlank() || txtPassword.getText().isBlank());
    }



}