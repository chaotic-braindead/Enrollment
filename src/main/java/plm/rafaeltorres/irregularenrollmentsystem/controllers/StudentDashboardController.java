package plm.rafaeltorres.irregularenrollmentsystem.controllers;
import java.awt.Desktop;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Schedule;
import plm.rafaeltorres.irregularenrollmentsystem.model.Student;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class StudentDashboardController extends Controller {
    private String currentSY = "2023-2024";
    private String currentSem = "1";
    private Student student;
    private Desktop desktop = Desktop.getDesktop();
    private Pane currentPane;

    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;

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
    private Label lblWelcome;
    @FXML
    private Label lblFullName;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblSemester;
    @FXML
    private Group enrolledGroup;
    @FXML
    private Group notEnrolledGroup;
    @FXML
    private Label lblFirstName;
    @FXML
    private Label lblLastName;
    @FXML
    private Label lblBirthday;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblAge;
    @FXML
    private Label lblStudentNoDash;
    @FXML
    private Label lblPersonalEmail;
    @FXML
    private Label lblPhoneNumber;
    @FXML
    private Label lblCollege;
    @FXML
    private Label lblCourse;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRegistrationStatus;
    @FXML
    private Pane enrollContainer;
    @FXML
    private Pane scheduleContainer;
    @FXML
    private Pane gradesContainer;
    @FXML
    private TableView tblGrades;
    @FXML
    private ComboBox<String> choiceSY;
    @FXML
    private ComboBox<String> choiceSemester;
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
    @FXML
    private Circle imgContainer;
    @FXML
    private Circle imgDashboardContainer;
    @FXML
    private ToggleGroup sideBar;
    @FXML
    private Button btnEnrollRegular;
    @FXML
    private Pane btnDownloadSER;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentSY = Maintenance.getInstance().getCurrentSY();
        currentSem = Maintenance.getInstance().getCurrentSem();

        sideBar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        conn = Database.connect();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
        btnDashboard.setSelected(true);
        currentPane = dashboardContainer;

        try{
            Integer.parseInt(currentSem);
            lblSemester.setText(StringUtils.integerToPlace(Integer.parseInt(currentSem)) + " Semester A.Y. " + currentSY);
        }catch(NumberFormatException e){
            lblSemester.setText("Summer Semester A.Y. " + currentSY);
        }

        // display default image
        File f = new File(MainScene.class.getResource("assets/img/md-person-2.png").getPath());
        Image defaultImage = new Image(f.toURI().toString(), false);
        ImagePattern pattern = new ImagePattern(defaultImage);
        imgContainer.setFill(pattern);
        imgDashboardContainer.setFill(pattern);



        // set default text for empty tables
        tblSchedule.setPlaceholder(new Label("You currently have no subjects in your schedule. Please enroll now."));
        tblGrades.setPlaceholder(new Label("Select a valid school year and semester from the drop down boxes provided."));

    }
    public void setUser(User user){
        Student student = (Student) user;
        this.student = student;
        if(student.getRegistrationStatus().equalsIgnoreCase("REGULAR")) {
            btnSubmit.setVisible(false);
            btnRemove.setVisible(false);
            btnSchedule.setDisable(true);
            tblSubjects.setPlaceholder(new Label("Please wait for your department chairperson to assign your schedule."));
        }
        else{
            btnSubmit.setVisible(true);
            btnRemove.setVisible(true);
            btnSchedule.setDisable(false);
            tblSubjects.setPlaceholder(new Label("No schedules to display."));
        }
        try{
            ps = conn.prepareStatement("select * from enrollment where student_no = ? and sy = ? and semester = ? and status = 'Enrolled'");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            boolean res = rs.next();
            btnSchedule.setDisable(!res);
            btnEnroll.setDisable(res);
            btnTuition.setDisable(!res);
            notEnrolledGroup.setVisible(!res);
            enrolledGroup.setVisible(res);
            btnDownloadSER.setDisable(!res);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while initializing the student dashboard controller: " + e);
        }
        lblWelcome.setText("Welcome, "+student.getFirstName()+"!");
        lblStudentNo.setText(student.getStudentNo());

        lblFullName.setText(student.getFirstName() +
                " " + student.getLastName());
        lblEmail.setText(student.getPLMEmail());
        lblFirstName.setText(student.getFirstName());
        lblLastName.setText(student.getLastName());
        lblGender.setText(student.getGender());

        SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");

        lblBirthday.setText(format.format(student.getBirthday()));

        lblAge.setText(student.getAge()+"");
        lblPersonalEmail.setText(student.getPersonalEmail());
        lblPhoneNumber.setText(student.getCellphoneNumber());
        lblStudentNoDash.setText(student.getStudentNo());
        lblCollege.setText(student.getCollege());
        lblCourse.setText(student.getCourse());
        lblYear.setText(String.valueOf(1+Integer.parseInt(currentSY.substring(0, 4))-Integer.parseInt(student.getStudentNo().substring(0, 4))));
        lblRegistrationStatus.setText(student.getRegistrationStatus());


        // initialize grade comboboxes
        try{
            ps = conn.prepareStatement("SELECT DISTINCT SY FROM GRADE WHERE STUDENT_NO = ?");
            ps.setString(1, student.getStudentNo());
            rs = ps.executeQuery();
            ObservableList<String> sy = FXCollections.observableArrayList();
            while(rs.next()){
                sy.add(rs.getString(1));
            }
            choiceSY.setItems(sy);
        }catch(Exception e){
            System.out.println(e);
        }


        if(student.getImage() != null){
            setImage(student.getImage());
        }
    }
    public void setImage(Blob img){;
        Image newImg = null;
        try{
            byte[] imgBytes = img.getBytes(1, (int)img.length());
            newImg = new Image((new ByteArrayInputStream(imgBytes)));
        } catch(Exception e){
            System.out.println(e);
        }

        if(newImg != null){
            ImagePattern ip = new ImagePattern(newImg);
            imgDashboardContainer.setFill(ip);
            imgContainer.setFill(ip);
        }
    }
    public void onChangePictureAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setSelectedExtensionFilter(
                new FileChooser.ExtensionFilter("Image formats",
                        "*.jpg", "*.jpeg", "*.png"));
        fc.setTitle("Select Image");
        File img = fc.showOpenDialog(stage);
        try{
            byte[] imgBytes = Files.readAllBytes(img.toPath());
            String b64Img = Base64.getEncoder().encodeToString(imgBytes);
            System.out.println(b64Img);
            ps = conn.prepareStatement(Database.Query.updateImage);
            Blob blob = conn.createBlob();
            blob.setBytes(1, imgBytes);
            ps.setBlob(1, blob);
            ps.setString(2, student.getStudentNo());
            ps.executeUpdate();
            setImage(blob);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onClickToEnrollMouseClicked(Event event) throws Exception {
        onBtnEnrollAction(event);
        btnEnroll.setSelected(true);
    }
    @FXML
    protected void onBtnChangePasswordAction(ActionEvent event) {

    }
    @FXML
    protected void btnDownloadOnMouseClicked(MouseEvent event) {

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
            ps.setString(3, student.getStudentNo());
            ps.setString(4, student.getStudentNo());
            ps.setString(5, student.getStudentNo());
            ps.setString(6, student.getStudentNo());
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
        try{
            ps = conn.prepareStatement("select * from enrollment where student_no = ? and sy = ? and semester = ? and status = 'Enrolled'");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            boolean res = rs.next();
            btnSchedule.setDisable(!res);
            btnEnroll.setDisable(res);
            btnTuition.setDisable(!res);
            notEnrolledGroup.setVisible(!res);
            enrolledGroup.setVisible(res);
            btnDownloadSER.setDisable(!res);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while initializing the student dashboard controller: " + e);
        }
    }
    @FXML
    protected void onBtnEnrollAction(Event event) throws Exception{
        currentPane.setVisible(false);
        currentPane = enrollContainer;
        currentPane.setVisible(true);
        if(student.getRegistrationStatus().equals("Irregular")){
            btnAdd.setVisible(true);
            displayAvailableScheds();
        }
        else {
            btnAdd.setVisible(false);
            try {
                ps = conn.prepareStatement("select v.subject_code, v.description, v.block, v.SCHEDULE, v.CREDITS, v.PROFESSOR from vwsubjectschedules v inner join student_schedule s on v.sy = s.sy and v.semester = s.semester and concat(v.course, v.year, v.block) = s.block_no and v.subject_code = s.subject_code "
                     +  "where s.student_no = ? and v.sy = ? and v.semester = ?");
                ps.setString(1, student.getStudentNo());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);
                TableViewUtils.generateTableFromResultSet(tblSubjects, ps.executeQuery());
            }catch(Exception e){
                AlertMessage.showErrorAlert("An error occurred while displaying your subjects: " + e);
            }
        }

    }

    @FXML
    protected void onBtnEnrollRegularAction(ActionEvent event){
        try{
            ps = conn.prepareStatement("UPDATE ENROLLMENT SET status = 'Enrolled' where sy = ? and semester = ? and student_no = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getStudentNo());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("You have been successfully enrolled! Click on 'Schedule' or 'Tuition' to view more details about your enrollment.");
            btnDashboard.setSelected(true);
            btnEnroll.setSelected(false);
            btnEnroll.setDisable(true);
            btnTuition.setDisable(false);
            btnSchedule.setDisable(false);
            btnDownloadSER.setDisable(false);
            onBtnDashboardAction(event);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while processing your enrollment: " + e);
        }
    }
    private void displaySched() {
        try{
            System.out.println("here");
            ps = conn.prepareStatement("select " +
                    "v.subject_code, " +
                    "v.description, " +
                    "v.block, " +
                    "v.SCHEDULE, " +
                    "v.CREDITS, " +
                    "v.professor "+
                    "from student_schedule s " +
                    "inner join vwSubjectSchedules v on " +
                    "s.subject_code = v.subject_code " +
                    "and s.sy = v.sy " +
                    "and s.semester = v.semester " +
                    "and s.block_no = concat(v.course, v.year, v.block) where s.student_no = ? and s.sy = ? and s.semester = ? ");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
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
        tblSchedule.getItems().clear();
        btnSchedule.setDisable(false);
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
            ps.setString(1, student.getStudentNo());
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
    }
    @FXML
    protected void onBtnGradesAction(ActionEvent event) throws IllegalAccessException{
        currentPane.setVisible(false);
        currentPane = gradesContainer;
        currentPane.setVisible(true);

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
            ps.setString(2, student.getStudentNo());
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

    @FXML
    protected void onSYComboAction(ActionEvent event){
        String sy = choiceSY.getSelectionModel().getSelectedItem();
        String semester = choiceSemester.getSelectionModel().getSelectedItem();
        if(sy == null)
            return;
        if(semester != null){
            try{
                ps = conn.prepareStatement("select " +
                        "ss.subject_code as `SUBJECT CODE`," +
                        "    ss.description as `SUBJECT DESCRIPTION`," +
                        "    ss.units as UNITS," +
                        "    ss.grade as GRADE " +
                        "from vwStudentGradeForSYAndSem ss where ss.student_no = ? and ss.sy = ? and ss.semester = ?");
                ps.setString(1, student.getStudentNo());
                ps.setString(2, choiceSY.getSelectionModel().getSelectedItem());
                ps.setString(3, semester);
                rs = ps.executeQuery();
                if(rs.getRow() == 0)
                    tblSubjects.getItems().clear();

                TableViewUtils.generateTableFromResultSet(tblGrades, rs);

            }catch(Exception e){
                AlertMessage.showErrorAlert("An error occurred while fetching your grades.");
            }
        }

        try{
            ps = conn.prepareStatement("SELECT DISTINCT SEMESTER FROM GRADE WHERE STUDENT_NO = ? AND SY = ?");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, sy);
            rs = ps.executeQuery();
            ObservableList<String> sem = FXCollections.observableArrayList();
            while(rs.next()){
                sem.add(rs.getString(1));
            }
            choiceSemester.setItems(sem);
        } catch(Exception e){
            System.out.println(e);
        }
        choiceSemester.setDisable(false);
    }
    @FXML
    protected void onSemesterComboAction(ActionEvent event){
        String semester = choiceSemester.getSelectionModel().getSelectedItem();
        if(semester == null)
            return;
        try{
            ps = conn.prepareStatement("select " +
                    "ss.subject_code as `SUBJECT CODE`," +
                    "    ss.description as `SUBJECT DESCRIPTION`," +
                    "    ss.units as UNITS," +
                    "    ss.grade as GRADE " +
                    "from vwStudentGradeForSYAndSem ss where ss.student_no = ? and ss.sy = ? and ss.semester = ?");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, choiceSY.getSelectionModel().getSelectedItem());
            ps.setString(3, semester);
            rs = ps.executeQuery();
            if(rs.getRow() == 0)
                tblSubjects.getItems().clear();

            TableViewUtils.generateTableFromResultSet(tblGrades, rs);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while fetching your grades.");
        }

    }
}
