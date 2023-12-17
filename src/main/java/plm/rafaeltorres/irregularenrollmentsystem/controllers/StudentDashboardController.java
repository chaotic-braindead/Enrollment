package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Schedule;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;
import plm.rafaeltorres.irregularenrollmentsystem.utils.TableViewUtils;

import java.io.IOException;
import java.lang.reflect.Field;
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
    @FXML
    private Pane enrollContainer;
    @FXML
    private Pane scheduleContainer;
    @FXML
    private Pane gradesContainer;
    @FXML
    private Pane tuitionContainer;
    @FXML
    private TableView tblSubjects;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView tblSchedule;
    @FXML
    private Button btnRemove;
    @FXML
    private Button btnSubmit;
    private Pane currentPane;
    private String currentSY = "2023-2024";
    private String currentSem = "1";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = Database.connect();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
        btnDashboard.setSelected(true);
        currentPane = dashboardContainer;

    }
    public void setStudentNo(String studentNo){
        this.studentNo = studentNo;
        lblStudentNo.setText(studentNo);
    }
    @FXML
    protected void onBtnLogoutAction(ActionEvent event) throws IOException {
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to log out?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;

        SceneSwitcher.switchScene(event, "Login.fxml");
    }

    private void onBtnClick(ActionEvent event) throws IllegalAccessException{
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field f : fields){
            if(f.getType().equals(ToggleButton.class) && !event.getSource().equals(f.get(this))){
                ToggleButton btn = (ToggleButton)f.get(this);
                btn.setSelected(false);
            }
        }
    }
    private void displayAvailableScheds() {
        try{
            ps = conn.prepareStatement("select sc.subject_code, su.description, sc.section, sc.time_slot, sc.room, su.units, sc.total_students from schedule sc" +
                    " inner join subject su on sc.subject_code = su.SUBJECT_CODE" +
                    " where" +
                    "((su.semester in(?, 'Any')) and sc.SY = ?)" +
                    "and" +
                    "(" +
                    "    sc.subject_code in (" +
                    "    select p.subject_code" +
                    "     from prerequisite p" +
                    "       where prerequisite_code in" +
                    "            (select subject_code" +
                    "             from vwgradereport" +
                    "             where student_number = ?" +
                    "               and COURSE_GRADE <> '5.00')" +
                    "         and subject_code not in" +
                    "            (select subject_code from studentschedule where student_no = ?)" +
                    "     )" +
                    "    or (sc.subject_code not in(select subject_code from prerequisite)" +
                    "        and sc.subject_code not in(select subject_code from grade where student_no = ?))" +
                    "        and sc.subject_code not in (select subject_code from studentschedule where student_no = ?)" +
                    ")");
            ps.setString(1, currentSem);
            ps.setString(2, currentSY);
            ps.setString(3, studentNo);
            ps.setString(4, studentNo);
            ps.setString(5, studentNo);
            ps.setString(6, studentNo);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblSubjects, rs);
        }catch (Exception e){
            System.out.println(e);
            AlertMessage.showErrorAlert("An error occurred while displaying subjects.");
        }
    }
    @FXML
    protected void onBtnDashboardAction(ActionEvent event) throws IllegalAccessException{
        currentPane.setVisible(false);
        currentPane = dashboardContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
    }
    @FXML
    protected void onBtnEnrollAction(ActionEvent event) throws Exception{
        currentPane.setVisible(false);
        currentPane = enrollContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
        displayAvailableScheds();
    }
    private void displaySched() {
        try{
            ps = conn.prepareStatement("select sched.subject_code, s.description, sched.section, sched.time_slot, sched.room, s.units, sched.total_students from schedule sched " +
                    "right join studentschedule ss on sched.subject_code = ss.subject_code " +
                    "and sched.section = ss.section " +
                    "inner join subject s on sched.subject_code = s.SUBJECT_CODE where ss.student_no = ?" +
                    " group by sched.subject_code, s.description, sched.section, sched.time_slot, sched.room, s.units, sched.total_students");
            ps.setString(1, studentNo);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblSchedule, rs);

        }catch (Exception e) {
            System.out.println(e);
            AlertMessage.showErrorAlert("An error has occured while displaying your schedule: "+e.toString());
        }
    }
    @FXML
    protected void onBtnScheduleAction(ActionEvent event) throws IllegalAccessException{
        currentPane.setVisible(false);
        currentPane = scheduleContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
        displaySched();
    }
    @FXML
    protected void onTblScheduleMouseClicked(MouseEvent event) {
        btnRemove.setDisable(tblSchedule.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    protected void onBtnSubmitAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnRemoveAction(ActionEvent event) {
        Schedule sched = new Schedule(tblSchedule.getSelectionModel().getSelectedItem());
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to remove "+sched.getSubjectCode()+" from your schedule?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;
        try{
            ps = conn.prepareStatement("delete from studentschedule where student_no = ? and subject_code = ?");
            ps.setString(1, studentNo);
            ps.setString(2, sched.getSubjectCode());
            ps.executeUpdate();
            tblSchedule.getItems().clear();
            tblSchedule.getColumns().clear();
            displaySched();
            AlertMessage.showInformationAlert("Successfully deleted " + sched.getSubjectCode());
        }catch(Exception e) {
            System.out.println(e);
            AlertMessage.showErrorAlert("An error occurred while removing "+sched.getSubjectCode()+" from your schedule: "+e.toString());
        }
    }
    @FXML
    protected void onBtnTuitionAction(ActionEvent event) throws IllegalAccessException{
        currentPane.setVisible(false);
        currentPane = tuitionContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
    }
    @FXML
    protected void onBtnGradesAction(ActionEvent event) throws IllegalAccessException{
        currentPane.setVisible(false);
        currentPane = gradesContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
    }
    @FXML
    protected void onTblSubjectsMouseClicked(MouseEvent event) {
        btnAdd.setDisable(tblSubjects.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnAddAction(ActionEvent event) {
        Schedule sched = new Schedule(tblSubjects.getSelectionModel().getSelectedItem());
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to add "+sched.getSubjectCode()+" to your schedule?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;

        try{
            ps = conn.prepareStatement("INSERT INTO STUDENTSCHEDULE VALUES(?, ?, ?, ?)");
            ps.setString(1, currentSY);
            ps.setString(2, studentNo);
            ps.setString(3, sched.getSubjectCode());
            ps.setString(4, sched.getSection());
            ps.executeUpdate();
            tblSubjects.getItems().clear();
            tblSubjects.getColumns().clear();
            displayAvailableScheds();
            AlertMessage.showInformationAlert("Successfully added "+sched.getSubjectCode());
        } catch(Exception e){
            System.out.println(e);
            AlertMessage.showErrorAlert("An error as occurred while adding subject: "+e.toString());
        }
    }
}
