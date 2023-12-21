package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Employee;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;
import plm.rafaeltorres.irregularenrollmentsystem.utils.TableViewUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;


public class AdminDashboardController extends Controller {
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private Employee employee;
    private String currentSY = "2023-2024";
    private String currentSem = "1";
    private Pane currentPane;
    @FXML
    private Label lblDateNow;
    @FXML
    private Label lblCurrentMasterlist;
    @FXML
    private ToggleButton btnEnrollment;
    @FXML
    private Pane enrollContainer;
    @FXML
    private Pane studentContainer;
    @FXML
    private Pane dashboardContainer;
    @FXML
    private Label lblFirstName;
    @FXML
    private Label lblFullName;
    @FXML
    private Label lblLastName;
    @FXML
    private Label lblEmployeeID;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblEmail1;
    @FXML
    private Label lblGender;
    @FXML
    private Label lblCellphoneNumber;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblBirthday;
    @FXML
    private Label lblAge;
    @FXML
    private ToggleButton btnApproval;
    @FXML
    private ToggleButton btnStudent;
    @FXML
    private Button btnAddStudent;
    @FXML
    private ToggleButton btnEditStudent;
    @FXML
    private Button btnDeleteStudent;
    @FXML
    private ToggleButton btnClass;
    @FXML
    private ToggleButton btnSchedule;
    @FXML
    private ToggleButton btnGrades;
    @FXML
    private ToggleButton btnSYandSem;
    @FXML
    private ToggleButton btnEmployee;
    @FXML
    private ToggleButton btnDashboard;
    @FXML
    private ToggleButton btnLogout;
    @FXML
    private TableView tblStudents;
    @FXML
    private TextField txtStudentSearch;
    @FXML
    private Circle imgContainer;
    @FXML
    private Label lblWelcome;
    @FXML
    private ComboBox<String> comboBoxCourse;
    @FXML
    private ComboBox<String> comboBoxStudentNo;
    @FXML
    private Label txtName;
    @FXML
    private ComboBox<String> comboBoxCollege;
    @FXML
    private Label txtCurrentSYandSem;
    @FXML
    private ComboBox<String> comboBoxBlock;
    @FXML
    private TableView<ObservableList<ObservableList<String>>> tblSubjects;
    @FXML
    private Button btnEnrollStudent;
    @FXML
    private Button btnPrintPreview;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = Database.connect();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy");
        lblDateNow.setText("Today is "+ formatter.format(new Date()));
        btnDashboard.setSelected(true);
        currentPane = dashboardContainer;

        // display default image
        File f = new File(MainScene.class.getResource("assets/img/md-person-2.png").getPath());
        Image defaultImage = new Image(f.toURI().toString(), false);
        ImagePattern pattern = new ImagePattern(defaultImage);
        imgContainer.setFill(pattern);


        tblSubjects.setPlaceholder(new Label("Please select a student to enroll."));
        tblSubjects.getSelectionModel().setSelectionMode(null);
        tblStudents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);


        // initialize comboboxes
        try{
            ps = conn.prepareStatement("SELECT college_code from college");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCollege.getItems().add(rs.getString(1));
            }

            ps = conn.prepareStatement("SELECT course_code from course");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCourse.getItems().add(rs.getString(1));
            }

            ps = conn.prepareStatement("select student_no from student");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxStudentNo.getItems().add(rs.getString(1));
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void setUser(User user){
        Employee employee = (Employee) user;
        this.employee = employee;
        lblWelcome.setText(employee.getFirstName());
        lblFullName.setText(employee.getFirstName() + " " + employee.getLastName());
        lblFirstName.setText(employee.getFirstName());
        lblLastName.setText(employee.getLastName());
        lblGender.setText(employee.getGender());
        lblEmail.setText(employee.getEmail());
        lblCellphoneNumber.setText((employee.getCellphoneNumber() == null) ? "-" : employee.getCellphoneNumber());
        lblEmail1.setText(employee.getEmail());
        lblAge.setText((employee.getAge() == 0) ? "-" : employee.getAge()+"");
        lblEmployeeID.setText(employee.getEmployeeID());
        lblAddress.setText((employee.getAddress() == null) ? "-" : employee.getAddress());

        if(employee.getBirthday() != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
            lblBirthday.setText(format.format(employee.getBirthday()));
        }else{
            lblBirthday.setText("-");
        }
    }


    @FXML
    protected void onTblStudentsMouseClicked(MouseEvent event){
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
    }

    private void displayTable(String query, TableView tbl){
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tbl, rs, new String[]{"STUDENT", "STUDENT_NO"});
        }catch(Exception e) {
            AlertMessage.showErrorAlert("An error has occurred while displaying the table: "+e);
        }
    }

    @FXML
    protected void onComboBoxCollegeAction(Event event){
        if(comboBoxCollege.getSelectionModel().getSelectedItem() == null)
            return;

        ObservableList<String> o = FXCollections.observableArrayList();
        ObservableList<String> ob = FXCollections.observableArrayList();

        try{
            ps = conn.prepareStatement("SELECT course_code from course where college_code = ?");
            ps.setString(1, comboBoxCollege.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                o.add(rs.getString(1));
            }
            comboBoxCourse.setItems(o);

            ps = conn.prepareStatement("SELECT s.student_no from student s inner join course c on s.course_code = c.course_code where c.college_code = ?");
            ps.setString(1, comboBoxCollege.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                ob.add(rs.getString(1));
            }
            comboBoxStudentNo.setItems(ob);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onComboBoxCourseAction(Event event) {
        if(comboBoxCourse.getSelectionModel().getSelectedItem() == null)
            return;
        ObservableList<String> o = FXCollections.observableArrayList();
        ObservableList<String> ob = FXCollections.observableArrayList();

        try{
            ps = conn.prepareStatement("SELECT college_code from course where course_code = ? ");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollege.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("SELECT student_no from student where course_code = ? ");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                o.add(rs.getString(1));
            }
            comboBoxStudentNo.setItems(o);

            ps = conn.prepareStatement("select distinct section from vwSubjectSchedules where course_code = ?");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                ob.add(rs.getString(1));
            }
            comboBoxBlock.setItems(ob);
            comboBoxBlock.setPromptText("Select a block/section");
            comboBoxBlock.setDisable(false);

        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onComboBoxStudentNoAction(Event event){

        if(comboBoxStudentNo.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ps = conn.prepareStatement("SELECT c.college_code from student s inner join course c on s.course_code = c.course_code where student_no = ?");
            ps.setString(1, comboBoxStudentNo.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollege.getSelectionModel().select(rs.getString(1));
            System.out.println("here");
            ps = conn.prepareStatement("SELECT course_code from student where student_no = ?");
            ps.setString(1, comboBoxStudentNo.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCourse.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("select lastname, firstname from student where student_no = ?");
            ps.setString(1, comboBoxStudentNo.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                txtName.setText(rs.getString(1) + ", " + rs.getString(2));


        }catch (Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onComboBoxBlockAction(Event event) {
        if(comboBoxBlock.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ps = conn.prepareStatement("select subject_code, description, section, SCHEDULE, credits, PROFESSOR from vwSubjectSchedules where course_code = ? and semester = ? and sy = ? and section = ?");
            ps.setString(1,comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSem);
            ps.setString(3, currentSY);
            ps.setString(4,comboBoxBlock.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblSubjects, rs);
            btnEnrollStudent.setDisable(false);
            btnPrintPreview.setDisable(false);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying the schedules: "+e);
        }
    }

    @FXML
    protected void onBtnEnrollStudentAction(Event event){
        try{
            for(int i = 0; i < tblSubjects.getItems().size(); ++i){
                ObservableList<ObservableList<String>> subs = tblSubjects.getItems().get(i);
                ps = conn.prepareStatement("INSERT INTO STUDENT_SCHEDULE VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, comboBoxStudentNo.getSelectionModel().getSelectedItem());
                ps.setString(4,  subs.get(0).toString());
                ps.setString(5, comboBoxCollege.getSelectionModel().getSelectedItem());
                ps.setString(6, comboBoxBlock.getSelectionModel().getSelectedItem());
                ps.executeUpdate();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onBtnEnrollAction(ActionEvent event) throws IllegalAccessException {
        currentPane.setVisible(false);
        currentPane = enrollContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
    }
    @FXML
    protected void onBtnApprovalAction(ActionEvent event) {

    }

    @FXML
    protected void onBtnStudentAction(ActionEvent event) throws IllegalAccessException {
        txtStudentSearch.clear();
        currentPane.setVisible(false);
        currentPane = studentContainer;
        currentPane.setVisible(true);
        tblStudents.getSelectionModel().clearSelection();
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
        onBtnClick(event);
        displayTable(Database.Query.getAllStudents, tblStudents);
    }

    @FXML
    protected void onTxtStudentSearchKeyTyped(KeyEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable(Database.Query.getAllStudents, tblStudents);
            return;
        }
        String query = Database.Query.getAllStudents + " where student_number regexp(?) or first_name regexp(?) or last_name regexp(?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentSearch.getText());
            ps.setString(2, txtStudentSearch.getText());
            ps.setString(3, txtStudentSearch.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"STUDENT", "STUDENT_NO"});
        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying student masterlist:"+e);
        }
    }

    @FXML
    protected void onBtnEditStudentAction(ActionEvent event) {
//        tblStudents.setEditable(btnEditStudent.isSelected());
    }

    private List<String> fetch(String query){
        List<String> res = new ArrayList<>();
        try{
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()){
                res.add(rs.getString(1));
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return res;
    }

    @FXML
    protected void onBtnAddStudentAction(ActionEvent event) throws IOException{
        List<String> courses = fetch("SELECT COURSE_CODE FROM COURSE");
        StringProperty name = new SimpleStringProperty("");
        Form addStudent = Form.of(
                Group.of(
                        Field.ofStringType("")
                                .required("Student must have a student number.")
                                .label("Student Number")
                                .validate(StringLengthValidator.exactly(10, "Student number must be 10 characters long")),
                        Field.ofStringType("")
                                .required("Student must have a last name.")
                                .label("Last Name"),
                        Field.ofStringType("")
                                .required("Student must have a first name.")
                                .label("First Name"),
                        Field.ofSingleSelectionType(List.of("M", "F"))
                                .required("Student must have a gender.")
                                .label("Gender")
                                .render(new SimpleRadioButtonControl<>()),
                        Field.ofDate(LocalDate.now())
                                .required("Student must have a birthdate.")
                                .label("Birthday"),
                        Field.ofStringType("")
                                .label("Personal Email")
                                .validate(RegexValidator.forEmail("Must be a valid email address.")),
                        Field.ofStringType("")
                                .label("Cellphone Number")
                                .validate(RegexValidator.forPattern("^(09|\\+639)\\d{9}$", "Must be a valid phone number.")),
                        Field.ofStringType("")
                                .label("Address"),
                        Field.ofSingleSelectionType(courses)
                                .required("Student must have a course.")
                                .label("Course")
                )
        ).title("Add Student");

        Pane root = new Pane();
        root.getChildren().add(new FormRenderer(addStudent));
        Button submit = new Button("Submit");
        root.getChildren().add(submit);
        Stage stage = new Stage();
        stage.setTitle("Add Student");
        stage.setScene(new Scene(root));
        stage.show();



//       ((Node)(event.getSource())).getScene().getWindow().hide())
    }
    @FXML
    protected void onBtnScheduleAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnGradesAction(ActionEvent event) {

    }

    @FXML
    protected void onBtnSYandSemAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnEmployeeAction(ActionEvent event) {

    }

    @FXML
    protected void onBtnClassAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnDashboardAction(ActionEvent event) throws IllegalAccessException {
        currentPane.setVisible(false);
        currentPane = dashboardContainer;
        currentPane.setVisible(true);
        onBtnClick(event);
    }
    @FXML
    protected void onTblSubjectsMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onTblScheduleMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onBtnChangePasswordAction(ActionEvent event) {

    }
    @FXML
    protected void btnDownloadOnMouseClicked(MouseEvent event) {

    }
    @FXML
    protected void onChangePictureAction(ActionEvent event) {

    }

}
