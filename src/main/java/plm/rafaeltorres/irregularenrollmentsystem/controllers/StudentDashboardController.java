package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentDashboardController extends Controller {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private String studentNo;
    @FXML
    private Label lblStudentNo;
    @FXML
    private Label lblDateNow;
    @FXML
    private ToggleButton btnDashboard;
    @FXML
    private ToggleButton btnEnroll;
    @FXML
    private ToggleButton btnSchedule;
    @FXML
    private ToggleButton btnTuition;
    @FXML
    private ToggleButton btnGrades;
    @FXML
    private Pane dashboardContainer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = Database.connect();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
        btnDashboard.setSelected(true);

    }
    public void setStudentNo(String studentNo){
        this.studentNo = studentNo;
        lblStudentNo.setText(studentNo);
    }
    @FXML
    protected void onBtnLogoutAction(ActionEvent event) throws IOException {
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to log out?");
        if(!confirm.isPresent())
            return;

        if(confirm.get() == ButtonType.YES)
            SceneSwitcher.switchScene(event, "Login.fxml");
    }

    @FXML
    protected void onBtnClick(ActionEvent event){
        
    }

}
