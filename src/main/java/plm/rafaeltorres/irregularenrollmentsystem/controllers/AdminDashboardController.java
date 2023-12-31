package plm.rafaeltorres.irregularenrollmentsystem.controllers;
import com.dlsc.formsfx.model.event.FieldEvent;
import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.table.TableFilter;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Employee;
import plm.rafaeltorres.irregularenrollmentsystem.model.StudentProperty;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.*;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;


public class AdminDashboardController extends Controller {
    private PreparedStatement ps;
    private ResultSet rs;
    private Employee employee;
    private String currentSY = "2023-2024";
    private String currentSem = "1";
    private ObservableList<String> selectedInTable;
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
    private Pane studentRecordsContainer;
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
    private ToggleButton btnStudentRecords;
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
    private TableView<ObservableList<String>> tblSubjects;
    @FXML
    private Button btnEnrollStudent;
    @FXML
    private Button btnPrintPreview;
    @FXML
    private TableView<ObservableList<String>> tblEnrollees;
    @FXML
    private ComboBox<String> comboBoxYear;
    @FXML
    private TableView<ObservableList<String>> tblIrregular;
    @FXML
    private Button btnApprove;
    @FXML
    private Button btnDisapprove;
    @FXML
    private ToggleButton btnStudentEntry;
    @FXML
    private ToggleButton btnEmployeeEntry;
    @FXML
    private ToggleButton btnGradesEntry;
    @FXML
    private ToggleButton btnSY;
    @FXML
    private ToggleButton btnSemester;
    @FXML
    private ToggleButton btnCollege;
    @FXML
    private ToggleButton btnCourse;
    @FXML
    private ToggleButton btnSubject;
    @FXML
    private Pane studentEntryContainer;
    @FXML
    private Pane employeEntryContainer;
    @FXML
    private TableView<ObservableList<String>> tblStudentGrades;
    @FXML
    private ComboBox<String> comboBoxSubjectCode;
    @FXML
    private ComboBox<String> comboBoxYearBlock;
    @FXML
    private ComboBox<String> comboBoxCollegeGrade;
    @FXML
    private Button btnLoadData;
    @FXML
    private Pane gradesEntryContainer;
    @FXML
    private ToggleGroup sideBar;
    @FXML
    private Label lblEmployeeNo;
    @FXML
    private Label lblTotalStudents;
    @FXML
    private Label lblSemester;
    @FXML
    private Label lblManage;
    @FXML
    private TableView<ObservableList<String>> tblManage;
    @FXML
    private Pane manageContainer;
    @FXML
    private Button btnManageDelete;
    @FXML
    private TableView<ObservableList<String>> tblStudentRecord;
    @FXML
    private TableView<ObservableList<String>> tblStudentGradeRecord;
    @FXML
    private ComboBox<String> choiceSYRecords;
    @FXML
    private Button btnLoadStudentRecord;
    @FXML
    private TextField txtStudentRecordSearch;
    @FXML
    private Pane scheduleContainer;
    @FXML
    private TableView<ObservableList<String>> tblSubjectScheduling;
    @FXML
    private ToggleButton btnSubjectSchedule;
    @FXML
    private TextField txtSchoolYearSchedule;
    @FXML
    private TextField txtSemesterSchedule;
    @FXML
    private ComboBox<String> comboBoxCollegeSchedule;
    @FXML
    private ComboBox<String> comboBoxSubjectSchedule;
    @FXML
    private TextArea txtAreaDescription;
    @FXML
    private ComboBox<String> comboBoxCourseSchedule;
    @FXML
    private ComboBox<String> comboBoxYearSchedule;
    @FXML
    private ComboBox<String> comboBoxBlockSchedule;
    @FXML
    private TextField txtBlockSched;
    @FXML
    private TextField txtTime;
    @FXML
    private TextField txtRoom;
    @FXML
    private CheckBox checkBoxSunday;
    @FXML
    private CheckBox checkBoxMonday;
    @FXML
    private CheckBox checkBoxTuesday;
    @FXML
    private CheckBox checkBoxWednesday;
    @FXML
    private CheckBox checkBoxThursday;
    @FXML
    private CheckBox checkBoxFriday;
    @FXML
    private CheckBox checkBoxSaturday;
    @FXML
    private ComboBox<String> comboBoxMode;
    @FXML
    private ComboBox<String> comboBoxFaculty;
    @FXML
    private TextField txtNameProf;
    @FXML
    private Button btnAddSchedule;
    @FXML
    private Button btnUpdateSchedule;
    @FXML
    private Button btnRemoveSchedule;
    @FXML
    private Button btnClearSchedule;
    @FXML
    private Button btnGo;
    @FXML
    private Pane classListContainer;
    @FXML
    private ComboBox<String> comboBoxClassListCourse;
    @FXML
    private ComboBox<String> comboBoxClassListYear;
    @FXML
    private ComboBox<String> comboBoxClassListBlock;
    @FXML
    private Button btnLoadClass;
    @FXML
    private TableView<String> tblClassStudents;
    @FXML
    private TableView<String> tblClassSchedule;
    @FXML
    private TextField txtClassListSY;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentSY = Maintenance.getInstance().getCurrentSY();
        currentSem = Maintenance.getInstance().getCurrentSem();
        sideBar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

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
        tblSubjects.setPlaceholder(new Label("Please select a block/section."));

        tblSubjects.getSelectionModel().setSelectionMode(null);
        tblStudents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);

        try{
            Integer.parseInt(currentSem);
            lblSemester.setText(StringUtils.integerToPlace(Integer.parseInt(currentSem)) + " Semester A.Y. " + currentSY);
        }catch(NumberFormatException e){
            lblSemester.setText("Summer Semester A.Y. " + currentSY);
        }

        txtSchoolYearSchedule.setText(currentSY);
        txtSemesterSchedule.setText(currentSem);



        comboBoxYearSchedule.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));
        comboBoxClassListYear.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5"));

        comboBoxBlockSchedule.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        comboBoxClassListBlock.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));

        comboBoxMode.setItems(FXCollections.observableArrayList("F2F", "OL"));

        try{
            ps = conn.prepareStatement("SELECT employee_id from employee");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxFaculty.getItems().add(rs.getString(1));
            }
        }catch(Exception e){
            System.out.println(e);
        }


        try{
            ps = conn.prepareStatement("SELECT count(*) FROM ENROLLMENT WHERE status = 'Enrolled' and sy = ? and semester = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            rs = ps.executeQuery();
            rs.next();
            lblTotalStudents.setText(rs.getInt(1) + " Enrolled Students");

        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while initializing the dashboard: " + e);
        }

        // initialize comboboxes
        try{
            ps = conn.prepareStatement("SELECT college_code from college");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCollege.getItems().add(rs.getString(1));
                comboBoxCollegeGrade.getItems().add(rs.getString(1));
                comboBoxCollegeSchedule.getItems().add(rs.getString(1));
            }

            ps = conn.prepareStatement("SELECT course_code from course");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCourse.getItems().add(rs.getString(1));
                comboBoxCourseSchedule.getItems().add(rs.getString(1).replace("BS", ""));
                comboBoxClassListCourse.getItems().add(rs.getString(1).replace("BS", ""));
            }

            comboBoxYear.setItems(FXCollections.observableArrayList("Any", "1", "2", "3", "4", "5"));
            comboBoxYear.getSelectionModel().selectFirst();

            ps = conn.prepareStatement("SELECT subject_code from subject where subject_code <> '00000'");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxSubjectSchedule.getItems().add(rs.getString(1));
            }

        }catch (Exception e){
            System.out.println(e);
        }



    }

    public void setUser(User user){
        Employee employee = (Employee) user;
        this.employee = employee;
        lblWelcome.setText("Welcome, " + employee.getFirstName() + "!");
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
        lblEmployeeNo.setText(employee.getEmployeeID());

        if(employee.getBirthday() != null) {
            SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy");
            lblBirthday.setText(format.format(employee.getBirthday()));
        }else{
            lblBirthday.setText("-");
        }

        if(employee.getImage() != null){
            setImage(employee.getImage());
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
            imgContainer.setFill(ip);
        }
    }

    public void onChangePictureAction(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image formats (.jpg, .png)",
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
            ps.setString(2, employee.getEmployeeID());
            ps.executeUpdate();
            setImage(blob);
        } catch(Exception e){
            System.out.println(e);
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
            TableViewUtils.generateEditableTableFromResultSet(tbl, rs, new String[]{"STUDENT", "STUDENT_NO"}, new Runnable() {
                @Override
                public void run() {

                }
            });
        }catch(Exception e) {
            AlertMessage.showErrorAlert("An error has occurred while displaying the table: "+e);
        }
    }

    @FXML
    protected void onComboBoxCollegeAction(Event event){
        comboBoxYear.setDisable(false);

        if(comboBoxStudentNo.getPromptText() != null)
            txtName.setText("");
        if(comboBoxCollege.getSelectionModel().getSelectedItem() == null)
            return;


        ObservableList<String> o = FXCollections.observableArrayList();
        try{
            ps = conn.prepareStatement("SELECT course_code from course where college_code = ?");
            ps.setString(1, comboBoxCollege.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                o.add(rs.getString(1));
            }
            comboBoxCourse.setItems(o);

            String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";

            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COLLEGE_CODE = ? AND REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled') AND status = 'A'");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, registrationStatus);
            ps.setString(3, currentSY);
            ps.setString(4, currentSem);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        } catch(Exception e){
            System.out.println(e);
        }
//        tblSubjects.getItems().clear();
        comboBoxBlock.setPromptText("Select a block/section");

    }

    @FXML
    protected void onComboBoxCourseAction(Event event) {

        comboBoxYear.setDisable(false);

        if(comboBoxStudentNo.getPromptText() != null)
            txtName.setText("");
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

            ps = conn.prepareStatement("select distinct block from vwSubjectSchedules where course = ? and sy = ? and semester = ?");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem().replace("BS", ""));
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            while(rs.next()){
                ob.add(rs.getString(1));
            }

            String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";

            ps = conn.prepareStatement("SELECT STUDENT_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled') and status = 'A'");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, registrationStatus);
            ps.setString(3, currentSY);
            ps.setString(4, currentSem);

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);

            comboBoxBlock.setItems(ob);
            comboBoxBlock.setDisable(comboBoxCourse.getSelectionModel().getSelectedItem() == null);
            comboBoxBlock.setPromptText("Select a block/section");


        } catch(Exception e){
            System.out.println(e);
        }
//        tblSubjects.getItems().clear();
    }

    @FXML
    protected void onComboBoxStudentNoAction(Event event){
        if(comboBoxStudentNo.getPromptText() == null || comboBoxStudentNo.getPromptText().isEmpty())
            return;
        comboBoxYear.getSelectionModel().select(Integer.toString(1+Integer.parseInt(currentSY.substring(0,4))-Integer.parseInt(comboBoxStudentNo.getPromptText().substring(0, 4))));
        comboBoxYear.setDisable(comboBoxStudentNo.getPromptText() != null);
        try{
            ps = conn.prepareStatement("SELECT college_code from vwstudentinfo where student_number = ?");
            ps.setString(1, comboBoxStudentNo.getPromptText());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollege.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("SELECT course_code from vwstudentinfo where student_number = ?");
            ps.setString(1, comboBoxStudentNo.getPromptText());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCourse.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled') and status = 'A'");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);

        }catch (Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onComboBoxYearAction(ActionEvent event){
        if(comboBoxYear.getSelectionModel().getSelectedItem() == null)
            return;
        if(!comboBoxYear.getSelectionModel().getSelectedItem().equals("Any")){
            if(comboBoxStudentNo.getPromptText() != null || comboBoxStudentNo.getPromptText().isEmpty())
                return;
            try{
                ps = conn.prepareStatement("SELECT student_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where (" + currentSY.substring(0, 4) + " - cast(substring(STUDENT_No, 1, 4) as unsigned)+1) = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled') and status = 'A'");
                ps.setString(1, comboBoxYear.getSelectionModel().getSelectedItem());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);

                rs = ps.executeQuery();
                TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
            }catch (Exception e){
                AlertMessage.showErrorAlert("An error occurred while retreiving regular enrollees: " + e);
            }
            return;
        }
        try{
            ps = conn.prepareStatement("SELECT student_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where registration_status = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled') and status = 'A'");
            ps.setString(1, currentSY);
            ps.setString(2,currentSem);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while retreiving regular enrollees: " + e);
        }


    }
    @FXML
    protected void onTblEnrolleesMouseClicked(MouseEvent event){
        ObservableList<String> o = (ObservableList<String>) tblEnrollees.getSelectionModel().getSelectedItem();

        if(o.equals(selectedInTable)){
            o = null;
            selectedInTable = null;
        }
        String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";

        btnApprove.setDisable(o == null);
        btnDisapprove.setDisable(o == null);

        if(o == null) {

//            tblSubjects.getItems().clear();
            comboBoxStudentNo.setPromptText("");
            comboBoxBlock.getSelectionModel().clearSelection();
            String query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending'))";
            if(registrationStatus.equalsIgnoreCase("IRREGULAR")){
                query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no in(select student_no from enrollment where SY = ? and semester = ? and status = 'Pending')";
            }
            try{
                ps = conn.prepareStatement(query);
                ps.setString(1, registrationStatus);
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);
                rs = ps.executeQuery();
                TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);

            }catch(Exception e){
                System.out.println(e);
            }
            txtName.setText("");
            comboBoxStudentNo.getSelectionModel().clearSelection();
            return;
        }


        for(int i = 0; i < tblEnrollees.getColumns().size(); ++i){
            TableColumn item = tblEnrollees.getColumns().get(i);
            if(item.getText().equals("COURSE CODE"))
                comboBoxCourse.getSelectionModel().select(o.get(i));
            if(item.getText().equals("STUDENT NO"))
                comboBoxStudentNo.setPromptText(o.get(i));
        }

        try{
            Statement s = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = s.executeQuery("select student_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where student_no =  '"+ o.get(0) + "'");
            if(rs.next())
                txtName.setText(rs.getString("name"));

            rs.beforeFirst();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
            tblEnrollees.getSelectionModel().select(0);
            selectedInTable = tblEnrollees.getSelectionModel().getSelectedItem();
            if(comboBoxBlock.getSelectionModel().getSelectedItem() != null){
                onComboBoxBlockAction(event);
            };
        }catch(Exception e){
            System.out.println(e);
        }

        if(registrationStatus.equalsIgnoreCase("IRREGULAR")){
            try{
                ps = conn.prepareStatement("select " +
                        "    s.subject_code, " +
                        "    s.course, " +
                        "    s.year, " +
                        "    s.block, " +
                        "    s.description, " +
                        "    s.schedule, " +
                        "    s.credits, " +
                        "    s.professor " +
                        "from student_schedule ss " +
                        "inner join vwsubjectschedules s on ss.subject_code = s.SUBJECT_CODE " +
                        "       and ss.sy = s.sy " +
                        "       and ss.semester = s.semester " +
                        "       and ss.block_no = concat(COURSE, year, block) " +
                        "where ss.student_no = ? and ss.sy = ? and ss.semester = ?");
                ps.setString(1, comboBoxStudentNo.getPromptText());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);
                TableViewUtils.generateTableFromResultSet(tblSubjects, ps.executeQuery());
            }catch (Exception e){
                AlertMessage.showErrorAlert("An error occurred while displaying student schedule: " + e);
            }
        }
    }
    @FXML
    protected void onComboBoxBlockAction(Event event) {
        if(comboBoxStudentNo.getPromptText().isEmpty() || comboBoxStudentNo.getPromptText() == null) {
            tblSubjects.setPlaceholder(new Label("Please select a student first."));
            return;
        }
        if(comboBoxBlock.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ps = conn.prepareStatement("select subject_code, description, block, SCHEDULE, credits, PROFESSOR from vwSubjectSchedules where course = ? and semester = ? and sy = ? and block = ? and year = ?");
            ps.setString(1,comboBoxCourse.getSelectionModel().getSelectedItem().replace("BS", ""));
            ps.setString(2, currentSem);
            ps.setString(3, currentSY);
            ps.setString(4,comboBoxBlock.getSelectionModel().getSelectedItem());
            ps.setString(5, Integer.toString(1+Integer.parseInt(currentSY.substring(0, 4))-Integer.parseInt(comboBoxStudentNo.getPromptText().substring(0,4))));
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
        Optional<ButtonType> confirmation = AlertMessage.showConfirmationAlert("Are you sure you want to enroll the student?");
        if(confirmation.isEmpty() || confirmation.get() == ButtonType.NO)
            return;

        try{
            for(int i = 0; i < tblSubjects.getItems().size(); ++i){
                ps = conn.prepareStatement("INSERT INTO STUDENT_SCHEDULE VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, comboBoxStudentNo.getPromptText());
                ps.setString(4,  tblSubjects.getItems().get(i).get(0));
                ps.setString(5, comboBoxCollege.getSelectionModel().getSelectedItem());
                ps.setString(6, comboBoxCourse.getSelectionModel().getSelectedItem().replace("BS","")+
                        (1+Integer.parseInt(currentSY.substring(0,4))-Integer.parseInt(comboBoxStudentNo.getPromptText().substring(0, 4)))+comboBoxBlock.getSelectionModel().getSelectedItem());

                ps.executeUpdate();
            }
            AlertMessage.showInformationAlert( comboBoxStudentNo.getPromptText() + " was successfully enrolled");
            ps = conn.prepareStatement("INSERT INTO ENROLLMENT VALUES(?, ?, ?, 'Pending')");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxStudentNo.getPromptText());
            ps.executeUpdate();


            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status = 'Enrolled')");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
//            tblSubjects.getItems().clear();

            comboBoxStudentNo.getSelectionModel().clearSelection();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onBtnEnrollmentAction(ActionEvent event) throws IllegalAccessException {
        btnApprove.setVisible(false);
        btnDisapprove.setVisible(false);
        btnEnrollStudent.setVisible(true);
        comboBoxBlock.setVisible(true);
//        tblSubjects.getItems().clear();
        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);

        onEnroll(event);
    }
    private void onEnroll(ActionEvent event) throws IllegalAccessException {
        txtName.setText("");
        comboBoxCollege.getSelectionModel().clearSelection();
        comboBoxCollege.setPromptText("Filter students by college");
        comboBoxCourse.getSelectionModel().clearSelection();
        comboBoxCourse.setPromptText("Filter students by course");
        comboBoxStudentNo.setPromptText("");
        String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";
        String query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending'))";
        if(registrationStatus.equalsIgnoreCase("IRREGULAR")){
            query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no in(select student_no from enrollment where SY = ? and semester = ? and status = 'Pending')";
        }
        try{
            ps = conn.prepareStatement(query);
            ps.setString(1, registrationStatus);
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        }catch(Exception e){
            System.out.println(e);
        }

        currentPane.setVisible(false);
        currentPane = enrollContainer;
        currentPane.setVisible(true);
//        onBtnClick(event);
    }
    @FXML
    protected void onBtnApprovalAction(ActionEvent event) throws IllegalAccessException {
        btnApprove.setVisible(true);
        btnDisapprove.setVisible(true);
        btnEnrollStudent.setVisible(false);
        comboBoxBlock.setVisible(false);
//        tblSubjects.getItems().clear();
        tblSubjects.setPlaceholder(new Label("Select a student."));
        onEnroll(event);


    }
    @FXML
    protected void onBtnApproveAction(ActionEvent event) {
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to approve the student's schedule?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
            AlertMessage.showInformationAlert("Cancelled approval.");
            return;
        }
        try{
            ps = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS = 'Enrolled' WHERE STUDENT_NO = ? AND SY = ? AND SEMESTER = ?");
            ps.setString(1, comboBoxStudentNo.getPromptText());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully approved student no. " + comboBoxStudentNo.getPromptText());
            btnApproval.fire();
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    @FXML
    protected void onBtnDisapproveAction(ActionEvent event){
        try{
            Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to disapprove the student's schedule?");
            if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
                AlertMessage.showInformationAlert("Cancelled disapproval.");
                return;
            }

            ps = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS = 'Declined' WHERE SY = ? AND SEMESTER = ? AND STUDENT_NO = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxStudentNo.getPromptText());
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM STUDENT_SCHEDULE WHERE STUDENT_NO = ? AND SY = ? AND SEMESTER = ?");
            ps.setString(1, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.executeUpdate();

            btnApproval.fire();
            AlertMessage.showInformationAlert("Disapproved schedule.");
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while disapproving schedule: " + e);
        }
    }
    @FXML
    protected void onBtnStudentEntryAction(ActionEvent event) throws IllegalAccessException {
        txtStudentSearch.clear();
        tblStudents.getSelectionModel().clearSelection();
//        tblStudents.getItems().clear();
        tblStudents.getColumns().clear();
        currentPane.setVisible(false);
        currentPane = studentEntryContainer;
        currentPane.setVisible(true);
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
        lblCurrentMasterlist.setText("STUDENT MASTERLIST");
        txtStudentSearch.setPromptText("Search for a student number or name");
        try{
            ps = conn.prepareStatement("SELECT student_no, lastname, firstname, gender, bday, age, address, cp_num, email, plm_email, college_code, course_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status, registration_status from vwstudentinfo");
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, ps.executeQuery(), new String[]{"STUDENT", "STUDENT_NO"}, new Runnable() {
                @Override
                public void run() {
                    btnStudentEntry.fire();
                }
            });
//            TableFilter.forTableView(tblStudents).apply();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying student masterlist: "+e);
        }
    }

    @FXML
    protected void onTxtStudentSearch(KeyEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable("SELECT student_no, lastname, firstname, gender, bday, age, address, cp_num, email, plm_email, college_code, course_code case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status, registration_status from vwstudentinfo", tblStudents);
            return;
        }
        if(!txtStudentSearch.getText().matches("[^A-Za-z0-9]")){
            txtStudentSearch.setText("");
            return;
        }
        String query = "SELECT * from vwstudentinfo where student_no regexp(?) or firstname regexp(?) or lastname regexp(?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentSearch.getText());
            ps.setString(2, txtStudentSearch.getText());
            ps.setString(3, txtStudentSearch.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"STUDENT", "STUDENT_NO"}, new Runnable() {
                @Override
                public void run() {
                    btnStudentEntry.fire();
                }
            });
//            TableFilter.forTableView(tblStudents).apply();

        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying student masterlist:"+e);
        }
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
        StudentProperty student = new StudentProperty();
        SingleSelectionField<String> gender = Field.ofSingleSelectionType(List.of("M", "F"));
        gender.selectionProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        System.out.println(newValue);
                        student.setGender(newValue);
                    }
                } );
        gender.required("Student must have a gender.");
        gender.label("Gender");

        DateField bday = Field.ofDate(LocalDate.now())
                .required("Student must have a birthdate.")
                .label("Birthday");
        bday.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                student.setBirthday(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(newValue));
            }
        });

        SingleSelectionField<String> course = Field.ofSingleSelectionType(courses);
        course.selectionProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(newValue);
                student.setCourse_code(newValue);
            }
        } );
        course.required("Student must have a course.");
        course.label("Course");
        Form addStudent = Form.of(
                Group.of(
                        Field.ofStringType(student.student_numberProperty()).bind(student.student_numberProperty())
                                .required("Student must have a student number.")
                                .label("Student Number")
                                .validate(RegexValidator.forPattern("^[0-9]{4}-[0-9]{5}", "Must have a valid student number format (year-nnnnn) ex: 2022-00000")),
                        Field.ofStringType(student.last_nameProperty()).bind(student.last_nameProperty())
                                .required("Student must have a last name.")
                                .validate(RegexValidator.forPattern("^[A-Z]{1}([a-z]+)?( )?([A-Z]{1}[a-z]+)?", "Must have a valid name format ex: Dela Cruz"))
                                .label("Last Name"),
                        Field.ofStringType(student.first_nameProperty()).bind(student.first_nameProperty())
                                .required("Student must have a first name.")
                                .validate(RegexValidator.forPattern("^[A-Z]{1}([a-z]+)?( )?([A-Z]{1}[a-z]+)?", "Must have a valid name format (uppercase start of name/s) ex: Juan"))
                                .label("First Name"),
                        gender,
                        Field.ofDate(LocalDate.now())
                                .required("Student must have a birthdate.")
                                .label("Birthday"),
                        Field.ofStringType(student.emailProperty()).bind(student.emailProperty())
                                .label("Personal Email")
                                .validate(RegexValidator.forEmail("Must be a valid email address.")),
                        Field.ofStringType(student.cellphone_numberProperty()).bind(student.cellphone_numberProperty())
                                .label("Cellphone Number")
                                .validate(RegexValidator.forPattern("^(09|\\+639)\\d{9}$", "Must be a valid Philippine phone number ex: 09123456789.")),
                        Field.ofStringType(student.addressProperty()).bind(student.addressProperty())
                                .label("Address")
                                .required("Must have an address"),
                        course
                )
        ).title("Add Student");

        Pane root = new Pane();
        root.getChildren().add(new FormRenderer(addStudent));
        Button submit = new Button("Submit");
        submit.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(student.getFirst_name());
                System.out.println(student.getBirthday());
                System.out.println(student.getCourse_code());
            }
        });
        root.getChildren().add(submit);
        Stage stage = new Stage();
        stage.setTitle("Add Student");
        stage.setScene(new Scene(root));
        stage.show();



//       ((Node)(event.getSource())).getScene().getWindow().hide())
    }
    @FXML
    protected void onBtnScheduleAction(ActionEvent event) {
        currentPane.setVisible(false);
        currentPane = scheduleContainer;
        currentPane.setVisible(true);
        onBtnClearScheduleAction(event);
    }
    @FXML
    protected void onComboBoxCollegeScheduleAction(ActionEvent event){
        if(comboBoxCollegeSchedule.getSelectionModel().getSelectedItem() == null){
            return;
        }
        try{
            ps = conn.prepareStatement("SELECT trim(replace(course_code, 'BS', '')) from course where college_code = ?");
            ps.setString(1, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
//            comboBoxCourseSchedule.getItems().clear();
            while(rs.next()){
                comboBoxCourseSchedule.getItems().add(rs.getString(1));
            }
            ps = conn.prepareStatement("SELECT subject_code from subject where college_code = ? and subject_code <> '00000'");
            ps.setString(1, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
//            comboBoxSubjectSchedule.getItems().clear();
            while(rs.next()){
                comboBoxSubjectSchedule.getItems().add(rs.getString(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onComboBoxSubjectScheduleAction(ActionEvent event){
        if(comboBoxSubjectSchedule.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ps = conn.prepareStatement("SELECT DISTINCT DESCRIPTION FROM SUBJECT WHERE SUBJECT_CODE = ?");
            ps.setString(1, comboBoxSubjectSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
            txtAreaDescription.setText(rs.getString(1));
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onComboBoxFacultyAction(ActionEvent event) {
        if(comboBoxFaculty.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ps = conn.prepareStatement("SELECT CONCAT(FIRSTNAME, ' ', LASTNAME) FROM EMPLOYEE WHERE EMPLOYEE_ID = ?");
            ps.setString(1, comboBoxFaculty.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                txtNameProf.setText(rs.getString(1));
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onBtnGoAction(ActionEvent event){
//        tblSubjectScheduling.getItems().clear();
        if(comboBoxCourseSchedule.getSelectionModel().getSelectedItem() == null || comboBoxYearSchedule.getSelectionModel().getSelectedItem() == null || comboBoxYearSchedule.getSelectionModel().getSelectedItem() == null){
            return;
        }
        try{
            ps = conn.prepareStatement("SELECT * FROM VWSUBJECTSCHEDULES WHERE SY = ? AND SEMESTER = ? AND CONCAT(COURSE, YEAR, BLOCK) = ? ORDER BY SUBJECT_CODE, SCHEDULE");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxCourseSchedule.getSelectionModel().getSelectedItem()+comboBoxYearSchedule.getSelectionModel().getSelectedItem()+comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblSubjectScheduling, rs);
//            TableFilter.forTableView(tblSubjectScheduling).apply();

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying block schedule: " + e);
        }
    }
    @FXML
    protected void onTblSubjectSchedulingMouseClicked(MouseEvent event){
        checkBoxSunday.setSelected(false);
        checkBoxMonday.setSelected(false);
        checkBoxTuesday.setSelected(false);
        checkBoxWednesday.setSelected(false);
        checkBoxThursday.setSelected(false);
        checkBoxFriday.setSelected(false);
        checkBoxSaturday.setSelected(false);
        ObservableList<String> o = tblSubjectScheduling.getSelectionModel().getSelectedItem();
        btnUpdateSchedule.setDisable(o == null);
        btnRemoveSchedule.setDisable(o == null);
        if(o == null)
            return;

        try{
            ps = conn.prepareStatement("SELECT distinct college_code from subject_schedule where block_no = ? and subject_code = ?");
            ps.setString(1, o.get(3)+o.get(4)+o.get(5));
            ps.setString(2, o.get(2));
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollegeSchedule.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE LASTNAME = ? AND FIRSTNAME = ?");
            ps.setString(1, o.get(9).split(" ")[1]);
            ps.setString(2, o.get(9).split(" ")[0]);
            rs = ps.executeQuery();
            if(rs.next()){
                comboBoxFaculty.getSelectionModel().select(rs.getString(1));
            }
        }catch(Exception e){
            System.out.println(e);
        }

        comboBoxSubjectSchedule.getSelectionModel().select(o.get(2));
        txtAreaDescription.setText(o.get(6));
        comboBoxCourseSchedule.getSelectionModel().select(o.get(3));
        comboBoxYearSchedule.getSelectionModel().select(o.get(4));
        comboBoxBlockSchedule.getSelectionModel().select(o.get(5));

        String[] schedule = o.get(7).split(",");
        for(String sched : schedule){
            String[] s = sched.split(" ");
            String day = s[0];
            switch(day){
                case "M":
                    checkBoxMonday.setSelected(true);
                    break;
                case "T":
                    checkBoxTuesday.setSelected(true);
                    break;
                case "W":
                    checkBoxWednesday.setSelected(true);
                    break;
                case "Th":
                    checkBoxThursday.setSelected(true);
                    break;
                case "F":
                    checkBoxFriday.setSelected(true);
                    break;
                case "S":
                    checkBoxSaturday.setSelected(true);
                    break;
                case "Su":
                    checkBoxSunday.setSelected(true);
                    break;
                default:
                    AlertMessage.showErrorAlert("Invalid day of the week in schedule. Please fix");
                    break;
            }
            txtTime.setText(s[1]);
            comboBoxMode.getSelectionModel().select(s[2]);
            StringBuilder room = new StringBuilder();
            for(int i = 3; i < s.length; ++i){
                room.append(s[i]);
            }
            txtRoom.setText(room.toString());
            txtNameProf.setText(o.get(o.size()-1));
        }
    }
    @FXML
    protected void onBtnClearScheduleAction(ActionEvent event){
        try{
            ps = conn.prepareStatement("SELECT college_code from college");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCollegeSchedule.getItems().add(rs.getString(1));
            }

            ps = conn.prepareStatement("SELECT course_code from course");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCourseSchedule.getItems().add(rs.getString(1).replace("BS", ""));
            }

            ps = conn.prepareStatement("SELECT subject_code from subject where subject_code <> '00000'");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxSubjectSchedule.getItems().add(rs.getString(1));
            }
        }catch (Exception e){
            System.out.println(e);
        }
//        tblSubjectScheduling.getItems().clear();
        comboBoxCollegeSchedule.getSelectionModel().clearSelection();
        comboBoxSubjectSchedule.getSelectionModel().clearSelection();
        txtAreaDescription.clear();
        comboBoxCourseSchedule.getSelectionModel().clearSelection();
        comboBoxYearSchedule.getSelectionModel().clearSelection();
        comboBoxBlockSchedule.getSelectionModel().clearSelection();
        txtTime.clear();
        txtRoom.clear();
        checkBoxSunday.setSelected(false);
        checkBoxMonday.setSelected(false);
        checkBoxTuesday.setSelected(false);
        checkBoxWednesday.setSelected(false);
        checkBoxThursday.setSelected(false);
        checkBoxFriday.setSelected(false);
        checkBoxSaturday.setSelected(false);
        comboBoxMode.getSelectionModel().clearSelection();
        comboBoxFaculty.getSelectionModel().clearSelection();
        txtNameProf.clear();
        btnUpdateSchedule.setDisable(true);
        btnRemoveSchedule.setDisable(true);
    }
    @FXML
    protected void onBtnAddScheduleAction(ActionEvent event){
        List<String> selected = new ArrayList<>();
        if(checkBoxSunday.isSelected())
            selected.add(checkBoxSunday.getText());
        if(checkBoxMonday.isSelected())
            selected.add(checkBoxMonday.getText());
        if(checkBoxTuesday.isSelected())
            selected.add(checkBoxTuesday.getText());
        if(checkBoxWednesday.isSelected())
            selected.add(checkBoxWednesday.getText());
        if(checkBoxThursday.isSelected())
            selected.add(checkBoxThursday.getText());
        if(checkBoxFriday.isSelected())
            selected.add(checkBoxFriday.getText());
        if(checkBoxSaturday.isSelected())
            selected.add(checkBoxSaturday.getText());

        try{
            int res = 0;
            for(int i = 0; i < selected.size(); ++i){
                ps = conn.prepareStatement("INSERT INTO SUBJECT_SCHEDULE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
                ps.setString(4, comboBoxCourseSchedule.getSelectionModel().getSelectedItem() + comboBoxYearSchedule.getSelectionModel().getSelectedItem() + comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
                ps.setString(5, comboBoxSubjectSchedule.getSelectionModel().getSelectedItem());
                ps.setString(6, selected.get(i));
                ps.setString(7, txtTime.getText());
                ps.setString(8, txtRoom.getText());
                ps.setString(9, comboBoxMode.getSelectionModel().getSelectedItem());
                ps.setString(10, String.format("%02d", i+1));
                ps.setString(11, comboBoxFaculty.getSelectionModel().getSelectedItem());
                res += ps.executeUpdate();
            }
            if(res > 0){
                AlertMessage.showInformationAlert("Added the subject schedule successfully!");
            }
            btnGo.fire();
        }catch (Exception e){
            AlertMessage.showErrorAlert("The selected schedule already exists in the table. Use update instead if you wish to alter the table.");
        }

        onBtnGoAction(event);
    }
    @FXML
    protected void onBtnUpdateScheduleAction(ActionEvent event){
        List<String> selected = new ArrayList<>();
        if(checkBoxSunday.isSelected())
            selected.add(checkBoxSunday.getText());
        if(checkBoxMonday.isSelected())
            selected.add(checkBoxMonday.getText());
        if(checkBoxTuesday.isSelected())
            selected.add(checkBoxTuesday.getText());
        if(checkBoxWednesday.isSelected())
            selected.add(checkBoxWednesday.getText());
        if(checkBoxThursday.isSelected())
            selected.add(checkBoxThursday.getText());
        if(checkBoxFriday.isSelected())
            selected.add(checkBoxFriday.getText());
        if(checkBoxSaturday.isSelected())
            selected.add(checkBoxSaturday.getText());
        try {
            int res = 0;
            ps = conn.prepareStatement("DELETE FROM SUBJECT_SCHEDULE WHERE SY = ? AND SEMESTER = ? AND COLLEGE_CODE = ? AND BLOCK_NO = ? AND SUBJECT_CODE = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            ps.setString(4, comboBoxCourseSchedule.getSelectionModel().getSelectedItem() + comboBoxYearSchedule.getSelectionModel().getSelectedItem() + comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
            ps.setString(5, comboBoxSubjectSchedule.getSelectionModel().getSelectedItem());
            ps.executeUpdate();
            for(int i = 0; i < selected.size(); ++i) {
                ps = conn.prepareStatement("INSERT INTO SUBJECT_SCHEDULE VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
                ps.setString(4, comboBoxCourseSchedule.getSelectionModel().getSelectedItem() + comboBoxYearSchedule.getSelectionModel().getSelectedItem() + comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
                ps.setString(5, comboBoxSubjectSchedule.getSelectionModel().getSelectedItem());
                ps.setString(6, selected.get(i));
                ps.setString(7, txtTime.getText());
                ps.setString(8, txtRoom.getText());
                ps.setString(9, comboBoxMode.getSelectionModel().getSelectedItem());
                ps.setString(10, String.format("%02d", i+1));
                ps.setString(11, comboBoxFaculty.getSelectionModel().getSelectedItem());
                res += ps.executeUpdate();
            }
            if(res > 0){
                AlertMessage.showInformationAlert("Added the subject schedule successfully!");
            }
            btnGo.fire();
        }catch(Exception e) {
            AlertMessage.showErrorAlert("An error occurred while updating a subject schedule: " + e);
        }
    }
    @FXML
    protected void onBtnRemoveScheduleAction(ActionEvent event){
        try{
            ps = conn.prepareStatement("DELETE FROM SUBJECT_SCHEDULE WHERE SY = ? AND SEMESTER = ? AND COLLEGE_CODE = ? AND BLOCK_NO = ? AND SUBJECT_CODE = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            ps.setString(4, comboBoxCourseSchedule.getSelectionModel().getSelectedItem() + comboBoxYearSchedule.getSelectionModel().getSelectedItem() + comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
            ps.setString(5, comboBoxSubjectSchedule.getSelectionModel().getSelectedItem());
            System.out.println(comboBoxCourseSchedule.getSelectionModel().getSelectedItem() + comboBoxYearSchedule.getSelectionModel().getSelectedItem() + comboBoxBlockSchedule.getSelectionModel().getSelectedItem());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Removed subject schedule successfully!");

            comboBoxCollegeSchedule.getSelectionModel().clearSelection();
            comboBoxSubjectSchedule.getSelectionModel().clearSelection();
            txtAreaDescription.clear();
            txtTime.clear();
            txtRoom.clear();
            checkBoxSunday.setSelected(false);
            checkBoxMonday.setSelected(false);
            checkBoxTuesday.setSelected(false);
            checkBoxWednesday.setSelected(false);
            checkBoxThursday.setSelected(false);
            checkBoxFriday.setSelected(false);
            checkBoxSaturday.setSelected(false);
            comboBoxMode.getSelectionModel().clearSelection();
            comboBoxFaculty.getSelectionModel().clearSelection();
            txtNameProf.clear();
            btnUpdateSchedule.setDisable(true);
            btnRemoveSchedule.setDisable(true);
            btnGo.fire();

        }catch(Exception e){

        }
    }
    @FXML
    protected void onBtnEmployeeEntryAction(ActionEvent event) {
        txtStudentSearch.clear();
        currentPane.setVisible(false);
        currentPane = studentEntryContainer;
        currentPane.setVisible(true);
        tblStudents.getSelectionModel().clearSelection();
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
//        tblStudents.getItems().clear();
        tblStudents.getColumns().clear();
        lblCurrentMasterlist.setText("EMPLOYEE MASTERLIST");
        txtStudentSearch.setPromptText("Search for an employee id or name");

        try{
            ps = conn.prepareStatement("SELECT employee_id, lastname, firstname, bday, age, gender, email, cp_num, address from vwemployeeaccount");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"EMPLOYEE", "EMPLOYEE_ID"}, new Runnable() {
                @Override
                public void run() {
                    btnEmployeeEntry.fire();
                }
            });
//            TableFilter.forTableView(tblStudents).apply();


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onTxtStudentSearchKeyTyped(KeyEvent event){
        if(txtStudentSearch.getPromptText().contains("employee"))
            onTxtEmployeeSearch(event);
        else
            onTxtStudentSearch(event);
    }
    @FXML
    protected void onTxtEmployeeSearch(KeyEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable("SELECT employee_id, lastname, firstname, email, gender, cp_num, address, bday from vwemployeeaccount", tblStudents);
            return;
        }
        String query = "SELECT * from vwemployeeaccount where employee_id regexp(?) or firstname regexp(?) or lastname regexp(?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentSearch.getText());
            ps.setString(2, txtStudentSearch.getText());
            ps.setString(3, txtStudentSearch.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"EMPLOYEE", "EMPLOYEE_ID"}, new Runnable() {
                @Override
                public void run() {
                    btnEmployeeEntry.fire();
                }
            });
//            TableFilter.forTableView(tblStudents).apply();

        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying employee masterlist:"+e);
        }
    }

//    @FXML
//    protected void onBtnSubjectScheduleAction(ActionEvent event){
//        btnManageDelete.setDisable(true);
//        currentPane.setVisible(false);
//        currentPane = manageContainer;
//        currentPane.setVisible(true);
//        tblManage.getColumns().clear();
//        tblManage.getItems().clear();
//        lblManage.setText("MANAGE COLLEGES");
//        try{
//            ps = conn.prepareStatement("SELECT * FROM vwSubjectScheduleBySequence");
//            rs = ps.executeQuery();
//            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i){
//                final int j = i;
//                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1).toUpperCase());
//                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
//                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
//                    }
//                });
//
//                col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
//                    @Override public void handle(TableColumn.CellEditEvent t) {
//                        ObservableList<String> o = (ObservableList<String>) t.getRowValue();
//                        try{
//                            System.out.println(o.get(j));
//
//                            System.out.println(t.getNewValue());
//                            ps = conn.prepareStatement("UPDATE SUBJECT_SCHEDULE SET " + rs.getMetaData().getColumnName(j+1) + " = ? "+ "WHERE SY = ? AND SEMESTER = ? AND COLLEGE_CODE = ? AND SUBJECT_CODE = ? AND BLOCK_NO = ? AND SEQUENCE_NO = ?");
//                            ps.setString(1, t.getNewValue().toString()) ;
//                            ps.setString(2, o.get(0));
//                            ps.setString(3, o.get(1));
//                            ps.setString(4, o.get(2));
//                            ps.setString(5, o.get(3));
//                            ps.setString(6, o.get(5));
//                            ps.setString(7, o.get(10));
//
//                            System.out.println(List.of(o.get(0), o.get(1), o.get(2), o.get(3), o.get(5), o.get(10)));
//
//                            ps.executeUpdate();
////                            o.set(j, t.getNewValue().toString());
//
//                            btnSubjectSchedule.fire();
//                        }catch(Exception e){
//                            System.out.println(e);
//                        }
//
//                    }
//                });
//                String txt = col.getText().replaceAll("_", " ").toUpperCase();
//                switch(txt){
//                    case "NAME":
//                    case "DESCRIPTION":
//                    case "SUBJECT CODE":
//                    case "COLLEGE CODE":
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new TableCell<ObservableList<String>, String>() {
//                                            @Override
//                                            protected void updateItem(String item, boolean empty) {
//                                                super.updateItem(item, empty);
//
//                                                if (item == null || empty) {
//                                                    setText(null);
//                                                    setStyle("");
//                                                } else {
//                                                    Text text = new Text(item);
//                                                    text.setStyle("-fx-text-alignment:left;");
//                                                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
//                                                    setGraphic(text);
//                                                }
//                                            }
//                                        };
//                                    }
//                                }
//                        );
//                        break;
//                    case "TYPE":
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("F2F", "OL"));
//                                    }
//                                }
//                        );
//                        break;
//                    case "SEQUENCE NO":
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("01", "02"));
//                                    }
//                                }
//                        );
//                        break;
//                    case "SY":
//                        ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT SY FROM SY"));
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
//                                    }
//                                }
//                        );
//                        break;
//                    case "SEMESTER":
//                        comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT SEMESTER FROM SEMESTER"));
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
//                                    }
//                                }
//                        );
//                        break;
//                    case "FACULTY ID":
//                        comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT EMPLOYEE_ID FROM EMPLOYEE"));
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
//                                    }
//                                }
//                        );
//                        break;
//                    default:
//                        col.setCellFactory(
//                                new Callback<TableColumn, TableCell>() {
//                                    public TableCell call(TableColumn p) {
//                                        return new WrappingTextFieldTableCell<ObservableList<String>>();
//
//                                    }
//                                }
//                        );
//                }
//                tblManage.getColumns().addAll(col);
//            }
//            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
//            while(rs.next()){
//                ObservableList<String> row = FXCollections.observableArrayList();
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
//                    row.add(rs.getString(i));
//                }
//                scheds.add(row);
//            }
//            tblManage.setItems(scheds);
//
//            tblManage.setRowFactory(tblView -> {
//                final TableRow<ObservableList<String>> r = new TableRow<>();
//                r.hoverProperty().addListener((observable) -> {
//                    final ObservableList<String> current = r.getItem();
//                    if (r.isHover() && current != null) {
//                        r.setStyle("-fx-background-color: #dbdbdb");
//                    } else {
//                        r.setStyle("");
//                    }
//                });
//                return r;
//            });
//            TableViewUtils.resizeTable(tblManage);
//
//
//        } catch(Exception e){
//            AlertMessage.showErrorAlert("An error occurred while displaying subject schedules: " + e);
//        }
//    }

    @FXML
    protected void onBtnClassAction(ActionEvent event) {
        currentPane.setVisible(false);
        currentPane = classListContainer;
        currentPane.setVisible(true);
        txtClassListSY.setText(currentSY+"-"+currentSem);
        comboBoxClassListCourse.getSelectionModel().clearSelection();
        comboBoxClassListYear.getSelectionModel().clearSelection();;
        comboBoxClassListBlock.getSelectionModel().clearSelection();
        tblClassStudents.getColumns().clear();
        tblClassSchedule.getColumns().clear();
    }

    @FXML
    protected void onBtnLoadClassAction(ActionEvent event) {
        String course = comboBoxClassListCourse.getSelectionModel().getSelectedItem();
        String year = comboBoxClassListYear.getSelectionModel().getSelectedItem();
        String block = comboBoxClassListBlock.getSelectionModel().getSelectedItem();

        if(course == null || year == null || block == null){
            AlertMessage.showErrorAlert("Please select a course, year, and block.");
            return;
        }
        try{
            ps = conn.prepareStatement("SELECT `student number`, `last name`, `first name` FROM VWCLASSLIST where SY = ? and semester = ? and block_no = ? and REGISTRATION_STATUS = 'Regular'");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, course+year+block);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblClassStudents, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while generating the student class list: " + e);
        }
        try{
            ps = conn.prepareStatement("select subject_code, description, schedule, credits, professor from vwsubjectschedules where sy = ? and semester = ? and course = ? and year = ? and block = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, course);
            ps.setString(4, year);
            ps.setString(5, block);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblClassSchedule, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while generating the student class list: " + e);
        }


    }
    @FXML
    protected void onBtnDashboardAction(ActionEvent event) throws IllegalAccessException {
        currentPane.setVisible(false);
        currentPane = dashboardContainer;
        currentPane.setVisible(true);
        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);

        try{
            Integer.parseInt(currentSem);
            lblSemester.setText(StringUtils.integerToPlace(Integer.parseInt(currentSem)) + " Semester A.Y. " + currentSY);
        }catch(NumberFormatException e){
            lblSemester.setText("Summer Semester A.Y. " + currentSY);
        }

        try{
            ps = conn.prepareStatement("SELECT count(*) FROM STUDENT");
            rs = ps.executeQuery();
            rs.next();
            int total = rs.getInt(1);
            ps = conn.prepareStatement("SELECT count(*) FROM ENROLLMENT WHERE status = 'Enrolled' and sy = ? and semester = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            rs = ps.executeQuery();
            rs.next();
            lblTotalStudents.setText(rs.getInt(1) + "/" + total + " Enrolled");

        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while initializing the dashboard: " + e);
        }
    }
    @FXML
    protected void onTblSubjectsMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onTblScheduleMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onBtnStudentRecordsAction(ActionEvent event) {
        if(choiceSYRecords.getSelectionModel().getSelectedItem() != null){
            choiceSYRecords.setDisable(true);
            choiceSYRecords.getSelectionModel().clearSelection();
        }
        currentPane.setVisible(false);
        currentPane = studentRecordsContainer;
        currentPane.setVisible(true);

//        tblStudentGradeRecord.getItems().clear();

        try{
            ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
//            TableFilter.forTableView(tblStudentRecord).apply();
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying students: " + e);
        }
    }
    @FXML
    protected void onTblStudentRecordMouseClicked(MouseEvent event){
        ObservableList<String> o = tblStudentRecord.getSelectionModel().getSelectedItem();
        if(o.equals(selectedInTable)){
            o = null;
            selectedInTable = null;
        }

        if(o == null) {
//            tblStudentGradeRecord.getItems().clear();
            choiceSYRecords.getSelectionModel().clearSelection();
            choiceSYRecords.setDisable(true);
            btnLoadStudentRecord.setDisable(true);
            try{
                ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO");
                rs = ps.executeQuery();
                TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
//                TableFilter.forTableView(tblStudentRecord).apply();

            }catch(Exception e){
                System.out.println(e);
            }
            return;
        }

        try{
            ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO where student_no = ?");
            ps.setString(1, tblStudentRecord.getSelectionModel().getSelectedItem().get(0));
            System.out.println("here");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
            tblStudentRecord.getSelectionModel().select(0);
            selectedInTable = tblStudentRecord.getSelectionModel().getSelectedItem();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while selecting a student: " + e);
        }

        choiceSYRecords.setDisable(false);
        try{
            ObservableList<String> sy = FXCollections.observableArrayList();
            ps = conn.prepareStatement("SELECT distinct School_Year FROM vwgradeentry where student_no = ?");
            ps.setString(1, tblStudentRecord.getSelectionModel().getSelectedItem().get(0));
            rs = ps.executeQuery();
            while(rs.next()){
                sy.add(rs.getString(1));
            }
            choiceSYRecords.setItems(sy);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while retrieving school years: " + e);
        }
//        tblStudentGradeRecord.getItems().clear();
        tblStudentGradeRecord.getColumns().clear();
    }
    @FXML
    protected void onChoiceSYRecordsAction(ActionEvent event) {
        btnLoadStudentRecord.setDisable(choiceSYRecords.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnLoadStudentRecordAction(ActionEvent event) {
        if(choiceSYRecords.getSelectionModel().getSelectedItem() == null){
//            tblStudentGradeRecord.getItems().clear();
            return;
        }
        try{
            ps = conn.prepareStatement("select v.subject_code, v.block, v.semester, sub.description, v.grade, v.remark from vwgradeentry v inner join subject sub on v.subject_code = sub.subject_code inner join student s on v.student_no = s.STUDENT_NO where v.student_no = ? and v.school_year = ?");
            ps.setString(1, tblStudentRecord.getSelectionModel().getSelectedItem().get(0));
            ps.setString(2, choiceSYRecords.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {

                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                if(col.getText().equalsIgnoreCase("GRADE")){
                    col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> t) {
                            ObservableList<String> o = t.getRowValue();
                            List<Double> validGrades = List.of(1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75, 3.00, 5.00);
                            try{
                                Double.parseDouble(t.getNewValue());
                            }catch(NumberFormatException e){
                                AlertMessage.showErrorAlert("Please enter a valid grade format. ex: 1.25");
                                btnLoadStudentRecord.fire();
                                return;
                            }
                            if(!validGrades.contains(Double.parseDouble(t.getNewValue()))){
                                AlertMessage.showErrorAlert("Please enter a valid grade format. ex: 1.25");
                                btnLoadStudentRecord.fire();
                                return;
                            }
                            o.set(o.size()-2, t.getNewValue());
                            try {
                                Connection conn = Database.connect();
                                PreparedStatement ps = conn.prepareStatement("REPLACE INTO GRADE(sy, semester, student_no, subject_code, block_no, grade) VALUES(?, ?, ?, ?, ?, ?)");
                                ps.setString(1, choiceSYRecords.getSelectionModel().getSelectedItem());
                                ps.setString(2, o.get(2));
                                ps.setString(3, tblStudentRecord.getSelectionModel().getSelectedItem().get(0));
                                ps.setString(4, tblStudentGradeRecord.getSelectionModel().getSelectedItem().get(0));
                                ps.setString(5, tblStudentGradeRecord.getSelectionModel().getSelectedItem().get(1));
                                ps.setDouble(6, Double.parseDouble(o.get(o.size()-2)));
                                ps.executeUpdate();
                                btnLoadStudentRecord.fire();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    });
                    col.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                        @Override
                        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
                            return new TextFieldTableCell(new DefaultStringConverter());
                        }
                    });
                }
                tblStudentGradeRecord.getColumns().addAll(col);
            }
            TableViewUtils.generateTable(tblStudentGradeRecord, rs);

        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while trying to fetch the grades: "+e);
        }
    }

    @FXML
    protected void onTxtStudentRecordSearchKeyTyped(KeyEvent event) {
        choiceSYRecords.setDisable(true);
        if(txtStudentRecordSearch.getText().isBlank()){
            displayTable("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO", tblStudentRecord);
            return;
        }
        String query = "SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO where student_no regexp(?) or lastname regexp(?) or firstname regexp(?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentRecordSearch.getText());
            ps.setString(2, txtStudentRecordSearch.getText());
            ps.setString(3, txtStudentRecordSearch.getText());

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying student record:"+e);
        }
    }

    @FXML
    protected void onBtnGradesEntryAction(ActionEvent event) throws IllegalAccessException {
//        tblStudentGrades.getItems().clear();
        tblStudentGrades.getColumns().clear();
        currentPane.setVisible(false);
        currentPane = gradesEntryContainer;
        currentPane.setVisible(true);

    }

    @FXML
    protected void onComboBoxCollegeGradeAction(ActionEvent event) {
        if(comboBoxCollegeGrade.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ObservableList<String> blocks = FXCollections.observableArrayList();
            ps = conn.prepareStatement("select distinct block from vwgradereport g inner join subject s where s.college_code = ?");
            ps.setString(1, comboBoxCollegeGrade.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                blocks.add(rs.getString(1));
            }

            comboBoxYearBlock.setItems(blocks);
            comboBoxYearBlock.setDisable(false);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onComboBoxYearBlockAction(ActionEvent event) {
        if(comboBoxYearBlock.getSelectionModel().getSelectedItem() == null)
            return;
        try{
            ObservableList<String> subjects = FXCollections.observableArrayList();
            ps = conn.prepareStatement("select distinct subject_code from student_schedule where block_no = ? and sy = ? and semester = ?");
            ps.setString(1, comboBoxYearBlock.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            while(rs.next()){
                subjects.add(rs.getString(1));
            }
            comboBoxSubjectCode.setItems(subjects);
            comboBoxSubjectCode.setDisable(false);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onComboBoxSubjectCodeAction(ActionEvent event) {
        btnLoadData.setDisable(comboBoxSubjectCode.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnLoadDataAction(ActionEvent event){
//        tblStudentGrades.getItems().clear();
        tblStudentGrades.getColumns().clear();

        try{
            ps = conn.prepareStatement("select v.student_no, s.lastname, s.firstname, v.grade, v.remark from vwgradeentry v inner join student s on v.student_no = s.STUDENT_NO where school_year = ? and semester = ? and block = ? and subject_code = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxYearBlock.getSelectionModel().getSelectedItem());
            ps.setString(4, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            System.out.println(rs.getRow());
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {

                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                if(col.getText().equalsIgnoreCase("GRADE")){
                    col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> t) {
                            ObservableList<String> o = (ObservableList<String>) t.getRowValue();
                            List<Double> validGrades = List.of(1.00, 1.25, 1.50, 1.75, 2.00, 2.25, 2.50, 2.75, 3.00, 5.00);
                            try{
                                Double.parseDouble(t.getNewValue());
                            }catch(NumberFormatException e){
                                AlertMessage.showErrorAlert("Please enter a valid grade format. ex: 1.25");
                                btnLoadData.fire();
                                return;
                            }
                            if(!validGrades.contains(Double.parseDouble(t.getNewValue()))){
                                AlertMessage.showErrorAlert("Please enter a valid grade format. ex: 1.25");
                                btnLoadData.fire();
                                return;
                            }
                            o.set(o.size()-2, t.getNewValue());
                            System.out.println(o);
                            try {
                                Connection conn = Database.connect();
                                PreparedStatement ps = conn.prepareStatement("REPLACE INTO GRADE(sy, semester, student_no, subject_code, block_no, grade) VALUES(?, ?, ?, ?, ?, ?)");
                                ps.setString(1, currentSY);
                                ps.setString(2, currentSem);
                                ps.setString(3, tblStudentGrades.getSelectionModel().getSelectedItem().get(0));
                                ps.setString(4, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
                                ps.setString(5, comboBoxYearBlock.getSelectionModel().getSelectedItem());
                                ps.setDouble(6, Double.parseDouble(o.get(o.size()-2)));
                                ps.executeUpdate();
                                btnLoadData.fire();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    });
                    col.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                        @Override
                        public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
                            return new TextFieldTableCell(new DefaultStringConverter());
                        }
                    });
                }
                tblStudentGrades.getColumns().addAll(col);
            }
            TableViewUtils.generateTable(tblStudentGrades, rs);

        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while trying to fetch the grades: "+e);
        }
}
    @FXML
    protected void onBtnSYAction(ActionEvent event) {
        currentPane.setVisible(false);
        currentPane = manageContainer;
        currentPane.setVisible(true);
        tblManage.getColumns().clear();
        tblManage.getItems().clear();
        lblManage.setText("MANAGE SCHOOL YEAR");

        TableColumn<ObservableList<String>, String> status = new TableColumn<>("STATUS");
        status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
            return new SimpleStringProperty();
        }
        });
        try{
            ps = conn.prepareStatement("SELECT * FROM SY");
            rs = ps.executeQuery();
            TableColumn<ObservableList<String>, String> sy = new TableColumn<>("SCHOOL YEAR");
            sy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                    return new SimpleStringProperty(observableListStringCellDataFeatures.getValue().get(0));
                }
            });
            tblManage.getColumns().add(sy);
            tblManage.getColumns().add(status);
            status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                    return new SimpleStringProperty(observableListStringCellDataFeatures.getValue().get(1));
                }
            });
            status.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                @Override
                public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> observableListStringTableColumn) {
                    return new ComboBoxTableCell<>(new DefaultStringConverter(),FXCollections.observableArrayList("Active", "Closed"));
                }
            });

            tblManage.getItems().clear();
            status.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                @Override public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> t) {
                    ObservableList<String> o = t.getRowValue();
                    if(t.getOldValue().equalsIgnoreCase("Active") && t.getNewValue().equalsIgnoreCase("Closed")){
                        AlertMessage.showErrorAlert("There must be one active school year.");
                        o.set(1, t.getOldValue());
                        tblManage.refresh();
                        return;
                    }
                    o.set(1, t.getNewValue());
                    currentSY = o.get(0);
                    Maintenance.getInstance().setCurrentSY(currentSY);
                    for(int i = 0; i < tblManage.getItems().size(); ++i){
                        if(!tblManage.getItems().get(i).get(0).equals(currentSY))
                            tblManage.getItems().get(i).set(1, "Closed");
                        tblManage.refresh();
                    }
                }
            });
            while(rs.next()){
                tblManage.getItems().add(FXCollections.observableArrayList(rs.getString(1), (rs.getString(1).equals(currentSY)) ? "Active" : "Closed"));
            }
            TableViewUtils.resizeTable(tblManage);
            tblManage.setRowFactory(tblView -> {
                final TableRow<ObservableList<String>> r = new TableRow<>();
                r.hoverProperty().addListener((observable) -> {
                    final ObservableList<String> current = r.getItem();
                    if (r.isHover() && current != null) {
                        r.setStyle("-fx-background-color: #dbdbdb");
                    } else {
                        r.setStyle("");
                    }
                });
                return r;
            });
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while fetching school years: " + e);
        }

    }
    @FXML
    protected void onBtnSemesterAction(ActionEvent event) {
        btnManageDelete.setDisable(true);

        currentPane.setVisible(false);
        currentPane = manageContainer;
        currentPane.setVisible(true);
        tblManage.getColumns().clear();
        tblManage.getItems().clear();
        lblManage.setText("MANAGE SEMESTER");

        TableColumn<ObservableList<String>, String> status = new TableColumn<>("STATUS");
        status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                return new SimpleStringProperty();
            }
        });
        try{
            ps = conn.prepareStatement("SELECT * FROM SEMESTER");
            rs = ps.executeQuery();
            TableColumn<ObservableList<String>, String> sem = new TableColumn<>("SEMESTER");
            sem.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                    return new SimpleStringProperty(observableListStringCellDataFeatures.getValue().get(0));
                }
            });
            tblManage.getColumns().add(sem);
            tblManage.getColumns().add(status);
            status.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList<String>, String> observableListStringCellDataFeatures) {
                    return new SimpleStringProperty(observableListStringCellDataFeatures.getValue().get(1));
                }
            });
            status.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                @Override
                public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> observableListStringTableColumn) {
                    return new ComboBoxTableCell<>(new DefaultStringConverter(),FXCollections.observableArrayList("Active", "Closed"));
                }
            });

            status.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                @Override public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> t) {
                    ObservableList<String> o = t.getRowValue();
                    if(t.getOldValue().equalsIgnoreCase("Active") && t.getNewValue().equalsIgnoreCase("Closed")){
                        AlertMessage.showErrorAlert("There must be one active semester.");
                        o.set(1, t.getOldValue());
                        tblManage.refresh();
                        return;
                    }
                    o.set(1, t.getNewValue());
                    currentSem = o.get(0);
                    Maintenance.getInstance().setCurrentSem(currentSem);

                    for(int i = 0; i < tblManage.getItems().size(); ++i){
                        if(!tblManage.getItems().get(i).get(0).equals(currentSem))
                            tblManage.getItems().get(i).set(1, "Closed");
                        tblManage.refresh();
                    }
                    System.out.println(tblManage.getItems());
                }
            });
            while(rs.next()){
                tblManage.getItems().add(FXCollections.observableArrayList(rs.getString(1), (rs.getString(1).equals(currentSem)) ? "Active" : "Closed"));
            }
            tblManage.setRowFactory(tblView -> {
                final TableRow<ObservableList<String>> r = new TableRow<>();
                r.hoverProperty().addListener((observable) -> {
                    final ObservableList<String> current = r.getItem();
                    if (r.isHover() && current != null) {
                        r.setStyle("-fx-background-color: #dbdbdb");
                    } else {
                        r.setStyle("");
                    }
                });
                return r;
            });
            TableViewUtils.resizeTable(tblManage);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while fetching school years: " + e);
        }

    }

    @FXML
    protected void onBtnCollegeAction(ActionEvent event) {
        btnManageDelete.setDisable(true);
        currentPane.setVisible(false);
        currentPane = manageContainer;
        currentPane.setVisible(true);
        tblManage.getColumns().clear();
        tblManage.getItems().clear();
        lblManage.setText("MANAGE COLLEGES");
        try{
            ps = conn.prepareStatement("SELECT college_code, description, date_opened, date_closed, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status FROM COLLEGE");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"COLLEGE", "COLLEGE_CODE"}, new Runnable() {
                @Override
                public void run() {
                    btnCollege.fire();
                }
            });
        } catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying colleges: " + e);
        }
    }


    @FXML
    protected void onTblManageMouseClicked(MouseEvent event){
        btnManageDelete.setDisable(tblManage.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnCourseAction(ActionEvent event) {
        btnManageDelete.setDisable(true);
        currentPane.setVisible(false);
        currentPane = manageContainer;
        currentPane.setVisible(true);
        tblManage.getColumns().clear();
        tblManage.getItems().clear();
        lblManage.setText("MANAGE COURSES");
        try{
            ps = conn.prepareStatement("SELECT course_code, description, college_code, date_opened, date_closed, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status FROM COURSE");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"COURSE", "COURSE_CODE"},  new Runnable() {
                @Override
                public void run() {
                    btnCourse.fire();
                }
            });
        } catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying colleges: " + e);
        }
    }
    @FXML
    protected void onBtnSubjectAction(ActionEvent event) {
        btnManageDelete.setDisable(true);
        currentPane.setVisible(false);
        currentPane = manageContainer;
        currentPane.setVisible(true);
        tblManage.getColumns().clear();
//        tblManage.getItems().clear();
        lblManage.setText("MANAGE SUBJECTS");
        try{
            ps = conn.prepareStatement("SELECT subject_code, description, units, college_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status FROM SUBJECT WHERE SUBJECT_CODE <> '00000'");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"SUBJECT", "SUBJECT_CODE"},  new Runnable() {
                @Override
                public void run() {
                    btnSubject.fire();
                }
            });
//            TableFilter.forTableView(tblManage).apply();

        } catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying subjects: " + e);
        }
    }


    @FXML
    protected void onBtnChangePasswordAction(ActionEvent event) {

    }
    @FXML
    protected void btnDownloadOnMouseClicked(MouseEvent event) {

    }

}
