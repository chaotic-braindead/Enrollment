package plm.rafaeltorres.irregularenrollmentsystem.controllers;
import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.*;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.security.crypto.bcrypt.BCrypt;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Employee;
import plm.rafaeltorres.irregularenrollmentsystem.model.EmployeeProperty;
import plm.rafaeltorres.irregularenrollmentsystem.model.StudentProperty;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private Button btnLogout;
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
    private TableView<String> tblClassStudents;
    @FXML
    private TableView<String> tblClassSchedule;
    @FXML
    private TableView<ObservableList<String>> tblTuitionFee;
    @FXML
    private TextField txtClassListSY;
    @FXML
    private TextField txtSearchManage;
    @FXML
    private javafx.scene.Group searchManageGroup;
    @FXML
    private javafx.scene.Group irregularLabelGroup;
    @FXML
    private ComboBox<String> cmbSelectTableCategory;
    @FXML
    private GridPane gridDataEntry;
    @FXML
    private GridPane gridMaintenance;
    @FXML
    private GridPane gridRecords;
    @FXML
    private GridPane gridTransaction;
    @FXML
    private Circle imgNavBar;
    @FXML
    private FontIcon navIkonli;
    @FXML
    private ToggleButton btnEnrollmentHistory;
    @FXML
    private Pane enrollmentHistoryContainer;
    @FXML
    private TableView<ObservableList<String>> tblEnrollmentHistory;
    @FXML
    private Button btnDeleteHistory;
    @FXML
    private ComboBox<String> cmbSYHistory;
    @FXML
    private ComboBox<String> cmbSemHistory;
    @FXML
    private TextField txtSearchStudNoHistory;
    @FXML
    private TextField txtSearchEnrollee;
    @FXML
    private Button btnFilterEnrollees;
    @FXML
    private Pane tuitionContainer;
    @FXML
    private TextField txtTuitionAmount;
    @FXML
    private TextField txtTuitionDesc;
    @FXML
    private Label lblBlockSection;
    @FXML
    private Label lblAdminEnrollment;

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
            ps = conn.prepareStatement("SELECT college_code from college where status = 'A'");
            rs = ps.executeQuery();
            comboBoxCollege.getItems().add("Any");
            while(rs.next()){
                comboBoxCollege.getItems().add(rs.getString(1));
                comboBoxCollegeGrade.getItems().add(rs.getString(1));
                comboBoxCollegeSchedule.getItems().add(rs.getString(1));
            }

            comboBoxCourse.getItems().add("Any");
            ps = conn.prepareStatement("SELECT course_code from course where status = 'A'");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCourse.getItems().add(rs.getString(1));
                comboBoxClassListCourse.getItems().add(rs.getString(1).replace("BS", ""));
            }

            comboBoxYear.setItems(FXCollections.observableArrayList("Any", "1", "2", "3", "4", "5"));
            comboBoxYear.getSelectionModel().selectFirst();
            cmbSelectTableCategory.setItems(FXCollections.observableArrayList("TRANSACTION", "RECORDS", "DATA ENTRY", "MAINTENANCE"));

            ps = conn.prepareStatement("SELECT subject_code from subject where subject_code <> '00000' and status = 'A'");
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
        else{
            // display default image
            URL resource = MainScene.class.getResource("assets/img/md-person-2.png");
            Image defaultImage = new Image(resource.toExternalForm(), false);
            ImagePattern pattern = new ImagePattern(defaultImage);
            imgContainer.setFill(pattern);
            imgNavBar.setFill(pattern);
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
            imgNavBar.setFill(ip);
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
        if(tblStudents.getSelectionModel().getSelectedItem() != null && ((ObservableList<String>)tblStudents.getSelectionModel().getSelectedItem()).get(0).equalsIgnoreCase(employee.getEmployeeID())){
            btnDeleteStudent.setDisable(true);
        }
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
        if(tblEnrollees.getSelectionModel().getSelectedItem() == null)
            txtName.setText("");
        if(comboBoxCollege.getSelectionModel().getSelectedItem() == null)
            return;


        ObservableList<String> o = FXCollections.observableArrayList();
        try{
            ps = conn.prepareStatement("SELECT course_code from course where status = 'A'");
            if(!comboBoxCollege.getSelectionModel().getSelectedItem().equalsIgnoreCase("Any")){
                ps = conn.prepareStatement("SELECT course_code from course where college_code = ? and status = 'A'");
                ps.setString(1, comboBoxCollege.getSelectionModel().getSelectedItem());
            }
            rs = ps.executeQuery();
            o.add("Any");
            while(rs.next()){
                o.add(rs.getString(1));
            }
            comboBoxCourse.setItems(o);

        } catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onBtnFilterAction(ActionEvent event){
        String college = comboBoxCollege.getSelectionModel().getSelectedItem();
        String course = comboBoxCourse.getSelectionModel().getSelectedItem();
        String year = comboBoxYear.getSelectionModel().getSelectedItem();

        String status = (!btnApprove.isVisible()) ? "Regular" : "Irregular";
        StringBuilder sb = new StringBuilder("SELECT student_no,  concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where registration_status = 'Regular' and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending')) AND status = 'A' and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0");
        if(status.equalsIgnoreCase("Irregular")){
            sb = new StringBuilder("SELECT student_no,  concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where registration_status = 'Irregular' and student_no in(select student_no from enrollment where SY = ? and semester = ? and status = 'Pending') and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0");
        }
        if(!college.equalsIgnoreCase("Any"))
            sb.append(" and college_code = '" + college + "'");

        if(!course.equalsIgnoreCase("Any"))
            sb.append(" and course_code = '" + comboBoxCourse.getSelectionModel().getSelectedItem() + "'");

        if(!year.equalsIgnoreCase("Any"))
            sb.append(" and cast('"+currentSY.substring(0,4)+"' as unsigned) - cast(substring(student_no, 1, 4) as unsigned) + 1 = " + Integer.parseInt(year));

        if(!txtSearchEnrollee.getText().isBlank())
            sb.append( " and student_no regexp('"+txtSearchEnrollee.getText()+"')");
        try{
            ps = conn.prepareStatement(sb.toString());
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, currentSY);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        }catch(Exception e) {
            AlertMessage.showErrorAlert("An error occurred while filtering enrollees: " + e);
        }

    }
    @FXML
    protected void onComboBoxCourseAction(Event event) {

        if(tblEnrollees.getSelectionModel().getSelectedItem() == null)
            txtName.setText("");
        if(comboBoxCourse.getSelectionModel().getSelectedItem() == null)
            return;
        ObservableList<String> o = FXCollections.observableArrayList();
        ObservableList<String> ob = FXCollections.observableArrayList();

        try{
            ps = conn.prepareStatement("SELECT college_code from course where course_code = ? and status = 'A'");
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

            comboBoxBlock.setItems(ob);
            comboBoxBlock.setDisable(comboBoxCourse.getSelectionModel().getSelectedItem() == null);


        } catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onTblEnrolleesMouseClicked(MouseEvent event){
        ObservableList<String> o = (ObservableList<String>) tblEnrollees.getSelectionModel().getSelectedItem();

        if(selectedInTable != null && o.equals(selectedInTable)){
            o = null;
            selectedInTable = null;
        }
        String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";

        btnApprove.setDisable(o == null);
        btnDisapprove.setDisable(o == null);
        comboBoxBlock.setDisable(o == null);
        btnFilterEnrollees.setDisable(o != null);
        txtSearchEnrollee.setDisable(o != null);
        comboBoxCollege.setDisable(o != null);
        comboBoxCourse.setDisable(o != null);
        comboBoxYear.setDisable(o != null);

        if(o == null) {
            tblSubjects.getItems().clear();
            comboBoxCollege.getSelectionModel().select("Any");
            comboBoxCourse.getSelectionModel().select("Any");
            comboBoxYear.getSelectionModel().select("Any");
            comboBoxBlock.setValue(null);
            txtSearchEnrollee.clear();
            comboBoxBlock.setDisable(true);
            String query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending'))  and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0";
            if(registrationStatus.equalsIgnoreCase("IRREGULAR")){
                query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no in(select student_no from enrollment where SY = ? and semester = ? and status = 'Pending')  and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0";
            }
            try{
                ps = conn.prepareStatement(query);
                ps.setString(1, registrationStatus);
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);
                ps.setString(4, currentSY.substring(0, 4));
                rs = ps.executeQuery();
                TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);

            }catch(Exception e){
                System.out.println(e);
            }
            txtName.setText("");
            return;
        }
        for(int i = 0; i < tblEnrollees.getColumns().size(); ++i) {
            TableColumn<ObservableList<String>, ?> item = tblEnrollees.getColumns().get(i);
            if (item.getText().equals("COURSE CODE"))
                comboBoxCourse.getSelectionModel().select(o.get(i));
        }

        try{
            ps = conn.prepareStatement("SELECT COLLEGE_CODE FROM COURSE WHERE COURSE_CODE = ?");
            ps.setString(1, o.get(o.size()-2));
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollege.getSelectionModel().select(rs.getString(1));
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while getting enrollee info: " + e);
        }
        comboBoxYear.getSelectionModel().select(""+(Integer.parseInt(currentSY.substring(0,4)) - Integer.parseInt(o.get(0).substring(0, 4))+1));
        txtSearchEnrollee.setText(tblEnrollees.getSelectionModel().getSelectedItem().get(0));
        try{
            ObservableList<String> blocks = FXCollections.observableArrayList();
            ps = conn.prepareStatement("select distinct block from vwsubjectschedules where sy = ? and semester = ? and course = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxCourse.getSelectionModel().getSelectedItem().replace("BS", ""));
            rs = ps.executeQuery();
            while(rs.next()){
                blocks.add(rs.getString(1));
            }
            comboBoxBlock.setItems(blocks);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while initializing block combo box: " + e);
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
                ps.setString(1, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
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
        if(tblEnrollees.getSelectionModel().getSelectedItem() == null) {
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
            ps.setString(5, Integer.toString(1+Integer.parseInt(currentSY.substring(0, 4))-Integer.parseInt(tblEnrollees.getSelectionModel().getSelectedItem().get(0).substring(0,4))));
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblSubjects, rs);
            btnEnrollStudent.setDisable(false);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying the schedules: "+e);
        }
    }


    @FXML
    protected void onBtnEnrollStudentAction(Event event){
        Optional<ButtonType> confirmation = AlertMessage.showConfirmationAlert("Are you sure you want to enroll the student/s?");
        if(confirmation.isEmpty() || confirmation.get() == ButtonType.NO)
            return;


        try{
            for(int i = 0; i < tblSubjects.getItems().size(); ++i){
                ps = conn.prepareStatement("INSERT INTO STUDENT_SCHEDULE VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
                ps.setString(4,  tblSubjects.getItems().get(i).get(0));
                ps.setString(5, comboBoxCollege.getSelectionModel().getSelectedItem());
                ps.setString(6, comboBoxCourse.getSelectionModel().getSelectedItem().replace("BS","")+
                        (1+Integer.parseInt(currentSY.substring(0,4))-Integer.parseInt(tblEnrollees.getSelectionModel().getSelectedItem().get(0).substring(0, 4)))+comboBoxBlock.getSelectionModel().getSelectedItem());

                ps.executeUpdate();
            }
            AlertMessage.showInformationAlert( tblEnrollees.getSelectionModel().getSelectedItem().get(0) + " was successfully enrolled");
            ps = conn.prepareStatement("INSERT INTO ENROLLMENT VALUES(?, ?, ?, 'Pending', ?)");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
            ps.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.executeUpdate();


            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending')) and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.setString(4, currentSY.substring(0, 4));

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
            tblSubjects.getItems().clear();

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while enrolling student: " + e);
        }
    }
    @FXML
    protected void onBtnEnrollmentAction(ActionEvent event) throws IllegalAccessException {
        btnApprove.setVisible(false);
        btnDisapprove.setVisible(false);
        btnEnrollStudent.setVisible(true);
        btnEnrollStudent.setDisable(true);
        comboBoxBlock.setVisible(true);
        lblBlockSection.setVisible(true);
        txtSearchEnrollee.clear();
        tblSubjects.getItems().clear();
        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);
        comboBoxBlock.setValue(null);
        comboBoxCollege.getSelectionModel().select("Any");
        comboBoxCollege.setDisable(false);
        comboBoxCourse.getSelectionModel().select("Any");
        comboBoxCourse.setDisable(false);
        comboBoxYear.getSelectionModel().select("Any");
        comboBoxYear.setDisable(false);
        comboBoxBlock.setDisable(true);
        txtSearchEnrollee.setDisable(false);
        btnFilterEnrollees.setDisable(false);
        lblAdminEnrollment.setText("ENROLLMENT");
        tblEnrollees.getSelectionModel().clearSelection();
        onEnroll(event);
    }
    private void onEnroll(ActionEvent event) {
        txtName.setText("");
        String registrationStatus = (!btnApprove.isVisible()) ? "Regular" : "Irregular";
        String query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? and status in ('Enrolled', 'Pending')) and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0";
        if(registrationStatus.equalsIgnoreCase("IRREGULAR")){
            query = "SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no in(select student_no from enrollment where SY = ? and semester = ? and status = 'Pending') and (cast(substring(?, 1, 4) as signed) - cast(substring(student_no, 1, 4) as signed)) >= 0";
        }
        try{
            ps = conn.prepareStatement(query);
            ps.setString(1, registrationStatus);
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.setString(4, currentSY);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        }catch(Exception e){
            System.out.println(e);
        }

        currentPane.setVisible(false);
        currentPane = enrollContainer;
        currentPane.setVisible(true);
    }
    @FXML
    protected void onBtnApprovalAction(ActionEvent event) throws IllegalAccessException {
        btnApprove.setVisible(true);
        btnDisapprove.setVisible(true);
        btnApprove.setDisable(true);
        btnDisapprove.setDisable(true);
        btnEnrollStudent.setVisible(false);
        comboBoxBlock.setVisible(false);
        txtSearchEnrollee.clear();
        comboBoxCollege.getSelectionModel().select("Any");
        comboBoxCollege.setDisable(false);
        comboBoxCourse.getSelectionModel().select("Any");
        comboBoxCourse.setDisable(false);
        comboBoxYear.getSelectionModel().select("Any");
        comboBoxYear.setDisable(false);
        tblSubjects.getItems().clear();
        txtSearchEnrollee.setDisable(false);
        btnFilterEnrollees.setDisable(false);
        tblEnrollees.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblSubjects.setPlaceholder(new Label("Select a student."));
        irregularLabelGroup.setVisible(true);
        lblBlockSection.setVisible(false);
        lblAdminEnrollment.setText("SCHEDULE APPROVAL");
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
            ps = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS = 'Enrolled', timestamp = ? WHERE STUDENT_NO = ? AND SY = ? AND SEMESTER = ?");
            ps.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(2, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
            ps.setString(3, currentSY);
            ps.setString(4, currentSem);
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully approved student no. " + tblEnrollees.getSelectionModel().getSelectedItem().get(0));
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

            ps = conn.prepareStatement("UPDATE ENROLLMENT SET STATUS = 'Declined', timestamp = ? WHERE SY = ? AND SEMESTER = ? AND STUDENT_NO = ?");
            ps.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.setString(4, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM STUDENT_SCHEDULE WHERE STUDENT_NO = ? AND SY = ? AND SEMESTER = ?");
            ps.setString(1, tblEnrollees.getSelectionModel().getSelectedItem().get(0));
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.executeUpdate();

            btnApproval.fire();
            tblSubjects.getItems().clear();
            AlertMessage.showInformationAlert("Disapproved schedule.");
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while disapproving schedule: " + e);
        }
    }
    @FXML
    protected void onBtnStudentEntryAction(ActionEvent event) throws IllegalAccessException {
        txtStudentSearch.clear();
        tblStudents.getSelectionModel().clearSelection();
        tblStudents.getColumns().clear();
        currentPane.setVisible(false);
        currentPane = studentEntryContainer;
        currentPane.setVisible(true);
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
        lblCurrentMasterlist.setText("STUDENT MASTERLIST");
        txtStudentSearch.setPromptText("Search for a student number, name, college code, or course code");
        try{
            ps = conn.prepareStatement("SELECT student_no, lastname, firstname, gender, bday, age, address, cp_num, email, plm_email, college_code, course_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status, registration_status from vwstudentinfo");
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, ps.executeQuery(), new String[]{"STUDENT", "STUDENT_NO"}, new Runnable() {
                @Override
                public void run() {
                    btnStudentEntry.fire();
                }
            });
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying student masterlist: "+e);
        }
    }

    @FXML
    protected void onTxtStudentSearch(ActionEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable("SELECT student_no, lastname, firstname, gender, bday, age, address, cp_num, email, plm_email, college_code, course_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status, registration_status from vwstudentinfo", tblStudents);
            return;
        }

        String query = "SELECT student_no, lastname, firstname, gender, bday, age, address, cp_num, email, plm_email, college_code, course_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status, registration_status from vwstudentinfo where student_no regexp(?) or firstname regexp(?) or lastname regexp(?) or college_code regexp(?) or course_code regexp(?)";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentSearch.getText());
            ps.setString(2, txtStudentSearch.getText());
            ps.setString(3, txtStudentSearch.getText());
            ps.setString(4, txtStudentSearch.getText());
            ps.setString(5, txtStudentSearch.getText());


            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"STUDENT", "STUDENT_NO"}, new Runnable() {
                @Override
                public void run() {
                    btnStudentEntry.fire();
                }
            });

        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying student masterlist:"+e);
        }
    }

    private void addStudentEntry(){
        StudentProperty student = new StudentProperty();
        Form addStudent = Form.of(
                        Group.of(
                                Field.ofStringType(student.student_numberProperty())
                                        .bind(student.student_numberProperty())
                                        .required("Student must have a student number.")
                                        .label("Student Number")
                                        .validate(RegexValidator.forPattern("^[0-9]{4}-[0-9]{5}", "Must have a valid student number format (year-nnnnn) ex: 2022-00000")),
                                Field.ofStringType(student.last_nameProperty())
                                        .bind(student.last_nameProperty())
                                        .required("Student must have a last name.")
                                        .validate(RegexValidator.forPattern("^[A-Za-z ,.'-]{1,100}", "Must have a valid name format ex: Dela Cruz"))
                                        .label("Last Name"),
                                Field.ofStringType(student.first_nameProperty())
                                        .bind(student.first_nameProperty())
                                        .required("Student must have a first name.")
                                        .validate(RegexValidator.forPattern("^[A-Za-z ,.'-]{1,100}", "Must have a valid name format (uppercase start of name/s) ex: Juan"))
                                        .label("First Name"),
                                Field.ofSingleSelectionType(student.genderListProperty())
                                        .bind(student.genderListProperty(), student.genderProperty())
                                        .required("Student must have a gender")
                                        .label("Gender"),
                                Field.ofDate(LocalDate.now())
                                        .bind(student.birthdayProperty())
                                        .required("Student must have a birthdate.")
                                        .label("Birthday"),
                                Field.ofStringType(student.emailProperty())
                                        .bind(student.emailProperty())
                                        .label("Personal Email")
                                        .validate(RegexValidator.forEmail("Must be a valid email address.")),
                                Field.ofStringType(student.cellphone_numberProperty())
                                        .bind(student.cellphone_numberProperty())
                                        .label("Cellphone Number")
                                        .validate(RegexValidator.forPattern("^(09|\\+639)\\d{9}$", "Must be a valid Philippine phone number ex: 09123456789.")),
                                Field.ofStringType(student.addressProperty())
                                        .bind(student.addressProperty())
                                        .label("Address")
                                        .validate(RegexValidator.forPattern("^.{1,100}$", "Must only contain 1 to 100 characters"))
                                        .required("Must have an address"),
                                Field.ofSingleSelectionType(student.courseListProperty())
                                        .bind(student.courseListProperty(), student.course_codeProperty())
                                        .required("Student must have a course")
                                        .label("Course")
                        )
                ).title("Add Student")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(addStudent);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add student");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !addStudent.isValid(),addStudent.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return student;
            }
            return null;
        });
        AlertMessage.addIcon(dialog);

        Optional<StudentProperty> newStudent = dialog.showAndWait();
        if(newStudent.isEmpty())
            return;

        try{
            ps = conn.prepareStatement("INSERT INTO STUDENT VALUES(?, ?, ?, ?, ?, null, ?, ?, ?, ?, 'A')");
            ps.setString(1, student.getStudent_number());
            ps.setString(2, student.getLast_name());
            ps.setString(3, student.getFirst_name());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getGender());
            ps.setString(6, student.getCourse_code());
            ps.setString(7, student.getCellphone_number());
            ps.setString(8, student.getAddress());
            ps.setString(9, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(student.getBirthday()));
            ps.executeUpdate();
            addAccount(student.getStudent_number(), "S");
            AlertMessage.showInformationAlert("Successfully added student!");
            btnStudentEntry.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("The student you entered already exists in the database.");
        }
    }

    private void addEmployeeEntry(){
        EmployeeProperty employee = new EmployeeProperty();
        Form addEmployee = Form.of(
                        Group.of(
                                Field.ofStringType(employee.employee_idProperty())
                                        .bind(employee.employee_idProperty())
                                        .required("Employee must have an employee id.")
                                        .label("Employee ID")
                                        .validate(RegexValidator.forPattern("^E[0-9]{3}", "Must have a valid employee id format ex: E000")),
                                Field.ofStringType(employee.last_nameProperty())
                                        .bind(employee.last_nameProperty())
                                        .required("Employee must have a last name.")
                                        .validate(RegexValidator.forPattern("^[A-Za-z ,.'-]{1,100}", "Must have a valid name format ex: Dela Cruz"))
                                        .label("Last Name"),
                                Field.ofStringType(employee.first_nameProperty())
                                        .bind(employee.first_nameProperty())
                                        .required("Employee must have a first name.")
                                        .validate(RegexValidator.forPattern("^[A-Za-z ,.'-]{1,100}", "Must have a valid name format (uppercase start of name/s) ex: Juan"))
                                        .label("First Name"),
                                Field.ofSingleSelectionType(employee.genderListProperty())
                                        .bind(employee.genderListProperty(), employee.genderProperty())
                                        .required("Employee must have a gender")
                                        .label("Gender"),
                                Field.ofDate(LocalDate.now())
                                        .bind(employee.birthdayProperty())
                                        .required("Employee must have a birthdate.")
                                        .label("Birthday"),
                                Field.ofStringType(employee.emailProperty())
                                        .bind(employee.emailProperty())
                                        .label("Personal Email")
                                        .validate(RegexValidator.forEmail("Must be a valid email address."))
                                        .required("Employee must have an email"),
                                Field.ofStringType(employee.cellphone_numberProperty())
                                        .bind(employee.cellphone_numberProperty())
                                        .label("Cellphone Number")
                                        .validate(RegexValidator.forPattern("^(09|\\+639)\\d{9}$", "Must be a valid Philippine phone number ex: 09123456789.")),
                                Field.ofStringType(employee.addressProperty())
                                        .bind(employee.addressProperty())
                                        .label("Address")
                                        .validate(RegexValidator.forPattern("^.{1,100}$", "Must have a length of 1 to 100 only."))
                                        .required("Must have an address")
                        )
        )
                .title("Add Student")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(addEmployee);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add student");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !addEmployee.isValid(),addEmployee.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return employee;
            }
            return null;
        });
        AlertMessage.addIcon(dialog);

        Optional<StudentProperty> newEmployee = dialog.showAndWait();
        if(newEmployee.isEmpty())
            return;

        try{
            ps = conn.prepareStatement("INSERT INTO EMPLOYEE VALUES(?, ?, ?, ?, ?, ?, ?, ?, 'A')");
            ps.setString(1, employee.getEmployee_id());
            ps.setString(2, employee.getLast_name());
            ps.setString(3, employee.getFirst_name());
            ps.setString(4, employee.getEmail());
            ps.setString(5, employee.getGender());
            ps.setString(6, employee.getCellphone_number());
            ps.setString(7, employee.getAddress());
            ps.setString(8, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(employee.getBirthday()));
            ps.executeUpdate();
            addAccount(employee.getEmployee_id(), "A");
            AlertMessage.showInformationAlert("Successfully added employee!");
            btnEmployeeEntry.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("The employee you entered already exists in the database.");
        }
    }
    private void addAccount(String accountNumber, String type){
        try{
            ps = conn.prepareStatement("INSERT INTO ACCOUNT VALUES(?, ?, null, ?)");
            ps.setString(1, accountNumber);
            ps.setString(2, BCrypt.hashpw(accountNumber, BCrypt.gensalt()));
            ps.setString(3, type);
            ps.executeUpdate();
        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while creating user account: " + e);
        }
    }
    @FXML
    protected void onBtnAddStudentAction(ActionEvent event){
        if(lblCurrentMasterlist.getText().toUpperCase().contains("STUDENT"))
            addStudentEntry();
        else
            addEmployeeEntry();
    }
    private void deleteEntry(String table, String pk, TableView tbl, Runnable callback){
        table = table.toLowerCase();
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to delete the " + table +"? All occurrences of this record from all tables will be deleted from the database.");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
            AlertMessage.showInformationAlert("Cancelled deletion of "+ table);
            return;
        }
        try{
            ps = conn.prepareStatement("DELETE FROM "+table+" WHERE "+pk+" = ?");
            ps.setString(1, ((ObservableList<String>)tbl.getSelectionModel().getSelectedItem()).get(0));
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully deleted " +table+ " from the database.");
            callback.run();
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while deleting " +table+ " from the database: " + e);
        }
    }

    private void deleteAccount(TableView tbl){
        try{
            ps = conn.prepareStatement("DELETE FROM ACCOUNT WHERE ACCOUNT_NO = ?");
            ps.setString(1, ((ObservableList<String>)tbl.getSelectionModel().getSelectedItem()).get(0));
            ps.executeUpdate();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while deleting an account: " + e);
        }
    }
    @FXML
    protected void onBtnDeleteStudentAction(ActionEvent event){

        if(lblCurrentMasterlist.getText().toUpperCase().contains("STUDENT"))
            deleteEntry("student", "student_no", tblStudents,  new Runnable() {
                @Override
                public void run() {
                    deleteAccount(tblStudents);
                    btnStudentEntry.fire();
                }
            });
        else
            deleteEntry("employee", "employee_id", tblStudents, new Runnable() {
                @Override
                public void run() {
                    deleteAccount(tblStudents);
                    btnEmployeeEntry.fire();
                }
            });
    }
    @FXML
    protected void onBtnScheduleAction(ActionEvent event) {
        currentPane.setVisible(false);
        currentPane = scheduleContainer;
        currentPane.setVisible(true);
        txtSchoolYearSchedule.setText(currentSY);
        txtSemesterSchedule.setText(currentSem);

        onBtnClearScheduleAction(event);
    }
    @FXML
    protected void onComboBoxCollegeScheduleAction(ActionEvent event){
        if(comboBoxCollegeSchedule.getSelectionModel().getSelectedItem() == null){
            return;
        }
        try{
            ObservableList<String> courses = FXCollections.observableArrayList();
            ObservableList<String> subjects = FXCollections.observableArrayList();

            ps = conn.prepareStatement("SELECT trim(replace(course_code, 'BS', '')) from course where college_code = ?");
            ps.setString(1, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                courses.add(rs.getString(1));
            }
            comboBoxCourseSchedule.setItems(courses);

            ps = conn.prepareStatement("SELECT subject_code from subject where college_code = ? and subject_code <> '00000' and status = 'A'");
            ps.setString(1, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                subjects.add(rs.getString(1));
            }
            comboBoxSubjectSchedule.setItems(subjects);
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
        comboBoxSubjectSchedule.setDisable(false);

        try{
            ps = conn.prepareStatement("SELECT college_code from course where course_code = ?");
            ps.setString(1, "BS"+comboBoxCourseSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollegeSchedule.getSelectionModel().select(rs.getString(1));
        }catch(Exception e){
            System.out.println(e);
        }
        try{
            ObservableList<String> subs = FXCollections.observableArrayList();
            ps = conn.prepareStatement("SELECT SUBJECT_CODE FROM SUBJECT WHERE COLLEGE_CODE = ? AND SUBJECT_CODE <> '00000' AND STATUS = 'A'");
            ps.setString(1, comboBoxCollegeSchedule.getSelectionModel().getSelectedItem());
            rs = ps.executeQuery();
            while(rs.next()){
                subs.add(rs.getString(1));
            }
            comboBoxSubjectSchedule.setItems(subs);
        }catch(Exception e ){
            System.out.println(e);
        }
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

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying block schedule: " + e);
        }

        btnUpdateSchedule.setDisable(true);
        btnRemoveSchedule.setDisable(true);
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
        comboBoxCollegeSchedule.setDisable(true);
        comboBoxSubjectSchedule.setDisable(true);
        ObservableList<String> o = tblSubjectScheduling.getSelectionModel().getSelectedItem();
        btnUpdateSchedule.setDisable(o == null);
        btnRemoveSchedule.setDisable(o == null);
        if(o == null)
            return;

        try{
            ps = conn.prepareStatement("SELECT distinct college_code from subject_schedule where block_no = ? and subject_code = ?");
            ps.setString(1, o.get(4)+o.get(5)+o.get(6));
            ps.setString(2, o.get(3));
            rs = ps.executeQuery();
            if(rs.next())
                comboBoxCollegeSchedule.getSelectionModel().select(rs.getString(1));

            ps = conn.prepareStatement("SELECT EMPLOYEE_ID FROM EMPLOYEE WHERE LASTNAME = ? AND FIRSTNAME = ?");
            ps.setString(1, o.get(10).split(" ")[1]);
            ps.setString(2, o.get(10).split(" ")[0]);
            rs = ps.executeQuery();
            if(rs.next()){
                comboBoxFaculty.getSelectionModel().select(rs.getString(1));
            }
        }catch(Exception e){
            System.out.println(e);
        }

        comboBoxSubjectSchedule.getSelectionModel().select(o.get(3));
        txtAreaDescription.setText(o.get(7));
        comboBoxCourseSchedule.getSelectionModel().select(o.get(4));
        comboBoxYearSchedule.getSelectionModel().select(o.get(5));
        comboBoxBlockSchedule.getSelectionModel().select(o.get(6));

        String[] schedule = o.get(8).split(",");
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
            ps = conn.prepareStatement("SELECT college_code from college where status = 'A'");
            rs = ps.executeQuery();
            ObservableList<String> colleges = FXCollections.observableArrayList();
            while(rs.next()){
                colleges.add(rs.getString(1));
            }
            comboBoxCollegeSchedule.setItems(colleges);

            ObservableList<String> courses = FXCollections.observableArrayList();
            ps = conn.prepareStatement("SELECT course_code from course where status = 'A'");
            rs = ps.executeQuery();
            while(rs.next()){
                courses.add(rs.getString(1).replace("BS", ""));
            }
            comboBoxCourseSchedule.setItems(courses);

            ObservableList<String> subjects = FXCollections.observableArrayList();
            ps = conn.prepareStatement("SELECT subject_code from subject where subject_code <> '00000' and status = 'A'");
            rs = ps.executeQuery();
            while(rs.next()){
                subjects.add(rs.getString(1));
            }
            comboBoxSubjectSchedule.setItems(subjects);
        }catch (Exception e){
            System.out.println(e);
        }
        comboBoxCollegeSchedule.getSelectionModel().clearSelection();
        comboBoxCollegeSchedule.setDisable(true);
        comboBoxSubjectSchedule.getSelectionModel().clearSelection();
        comboBoxSubjectSchedule.setDisable(true);
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
        tblSubjectScheduling.getItems().clear();
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
            if(res == 0){
                AlertMessage.showErrorAlert("Select a valid course/year/block and fill out all fields correctly in order to add a subject schedule.");
                return;
            }
            AlertMessage.showInformationAlert("Added the subject schedule successfully!");
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
                AlertMessage.showInformationAlert("Updated the subject schedule successfully!");
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
    protected void onBtnEnrollmentHistoryAction(ActionEvent event){
        currentPane.setVisible(false);
        currentPane = enrollmentHistoryContainer;
        currentPane.setVisible(true);
        btnDeleteHistory.setDisable(true);
        ObservableList<String> sy = FXCollections.observableArrayList();
        ObservableList<String> sem = FXCollections.observableArrayList();
        try{
            ps = conn.prepareStatement("SELECT SY FROM SY");
            rs = ps.executeQuery();
            sy.add("Any");
            while(rs.next()){
                if(Integer.parseInt(rs.getString(1).substring(0,4)) <= Integer.parseInt(currentSY.substring(0, 4))){
                    sy.add(rs.getString(1));
                }
            }
            cmbSYHistory.setItems(sy);

            ps = conn.prepareStatement("SELECT SEMESTER FROM SEMESTER");
            rs = ps.executeQuery();
            sem.add("Any");
            while(rs.next()){
                sem.add(rs.getString(1));
            }
            cmbSemHistory.setItems(sem);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while initializing combo boxes: " + e);
        }

        cmbSYHistory.getSelectionModel().select("Any");
        cmbSemHistory.getSelectionModel().select("Any");

        try{
            ps = conn.prepareStatement("SELECT e.sy, e.semester, e.student_no, concat(v.lastname, ', ', v.firstname) as name, v.registration_status, e.status, e.timestamp FROM ENROLLMENT e inner join vwstudentinfo v on e.student_no = v.student_no WHERE cast(substring(e.sy, 1, 4) as unsigned) <= ? ORDER BY e.TIMESTAMP DESC");
            ps.setInt(1, Integer.parseInt(currentSY.substring(0, 4)));
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollmentHistory, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying enrollment history: " + e);
        }
    }
    @FXML
    protected void onBtnDeleteHistoryAction(ActionEvent event){
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to delete this enrollment record from the table? All of the student's subject schedules will also be deleted.");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
            AlertMessage.showInformationAlert("Cancelled deletion.");
            return;
        }

        try{
            ps = conn.prepareStatement("DELETE FROM ENROLLMENT WHERE SY = ? AND SEMESTER = ? AND STUDENT_NO = ?");
            ps.setString(1, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(0));
            ps.setString(2, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(1));
            ps.setString(3, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(2));
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM STUDENT_SCHEDULE WHERE SY = ? AND SEMESTER = ? AND STUDENT_NO = ?");
            ps.setString(1, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(0));
            ps.setString(2, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(1));
            ps.setString(3, tblEnrollmentHistory.getSelectionModel().getSelectedItem().get(2));
            ps.executeUpdate();

            AlertMessage.showInformationAlert("Successfully deleted enrollment record.");
            btnEnrollmentHistory.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while deleting an enrollment history: " + e);
        }
    }

    @FXML
    protected void onTblEnrollmentHistoryMouseClicked(MouseEvent event){
        btnDeleteHistory.setDisable(tblEnrollmentHistory.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    protected void onBtnLoadHistoryAction(ActionEvent event){
        StringBuilder sb = new StringBuilder("SELECT e.sy, e.semester, e.student_no, concat(v.lastname, ', ', v.firstname) as name, v.registration_status, e.status, e.timestamp FROM ENROLLMENT e inner join vwstudentinfo v on e.student_no = v.student_no  WHERE cast(substring(e.sy, 1, 4) as unsigned) <= ? ");
        String sy = cmbSYHistory.getSelectionModel().getSelectedItem();
        String sem = cmbSemHistory.getSelectionModel().getSelectedItem();
        btnDeleteHistory.setDisable(true);
        if(!sy.equalsIgnoreCase("Any"))
            sb.append(" and e.SY = '" + sy + "'");

        if(!sem.equalsIgnoreCase("Any"))
            sb.append(" and e.SEMESTER = '" + sem + "'");

        if(!txtSearchStudNoHistory.getText().isBlank())
            sb.append(" and (e.student_no regexp('"+txtSearchStudNoHistory.getText()+"') or concat(v.lastname, ', ', v.firstname) regexp('"+txtSearchStudNoHistory.getText()+"'))");

        sb.append(" ORDER BY e.timestamp DESC");
        try{
            ps = conn.prepareStatement(sb.toString());
            ps.setInt(1, Integer.parseInt(currentSY.substring(0, 4)));
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollmentHistory, rs);
        }catch(Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying enrollment history: " + e);
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
        tblStudents.getColumns().clear();
        lblCurrentMasterlist.setText("EMPLOYEE MASTERLIST");
        txtStudentSearch.setPromptText("Search for an employee by employee id or name");

        try{
            ps = conn.prepareStatement("SELECT employee_id, lastname, firstname, bday, age, gender, email, cp_num, address from vwemployeeaccount");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblStudents, rs, new String[]{"EMPLOYEE", "EMPLOYEE_ID"}, new Runnable() {
                @Override
                public void run() {
                    btnEmployeeEntry.fire();
                }
            });


        }catch(Exception e){
            System.out.println(e);
        }
    }

    @FXML
    protected void onBtnSearchEntry(ActionEvent event){
        if(txtStudentSearch.getPromptText().contains("employee"))
            onTxtEmployeeSearch(event);
        else
            onTxtStudentSearch(event);
    }
    @FXML
    protected void onTxtEmployeeSearch(ActionEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable("SELECT employee_id, lastname, firstname, bday, age, gender, email, cp_num, address from vwemployeeaccount", tblStudents);
            return;
        }
        String query = "SELECT employee_id, lastname, firstname, bday, age, gender, email, cp_num, address from vwemployeeaccount where employee_id regexp(?) or firstname regexp(?) or lastname regexp(?)";
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

        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying employee masterlist:"+e);
        }
    }


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
            ps = conn.prepareStatement("SELECT distinct `student number`, `last name`, `first name` FROM VWCLASSLIST where SY = ? and semester = ? and block_no = ? and REGISTRATION_STATUS = 'Regular'");
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
            ps = conn.prepareStatement("SELECT count(*) FROM ENROLLMENT WHERE status = 'Enrolled' and sy = ? and semester = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            rs = ps.executeQuery();
            rs.next();
            lblTotalStudents.setText(rs.getInt(1) + " Enrolled Students");

        }catch(Exception e){
            AlertMessage.showErrorAlert("There was an error while initializing the dashboard: " + e);
        }
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

        tblStudentGradeRecord.getItems().clear();

        try{
            ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO ORDER BY registration_status");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying students: " + e);
        }
    }
    @FXML
    protected void onTblStudentRecordMouseClicked(MouseEvent event){
        ObservableList<String> o = tblStudentRecord.getSelectionModel().getSelectedItem();
        if(selectedInTable != null && o.equals(selectedInTable)){
            o = null;
            selectedInTable = null;
        }

        if(o == null) {
            tblStudentGradeRecord.getItems().clear();
            choiceSYRecords.getSelectionModel().clearSelection();
            choiceSYRecords.setDisable(true);
            btnLoadStudentRecord.setDisable(true);
            try{
                ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO ORDER BY registration_status");
                rs = ps.executeQuery();
                TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);

            }catch(Exception e){
                System.out.println(e);
            }
            return;
        }

        try{
            ps = conn.prepareStatement("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO where student_no = ?");
            ps.setString(1, tblStudentRecord.getSelectionModel().getSelectedItem().get(0));
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
        tblStudentGradeRecord.getColumns().clear();
    }
    @FXML
    protected void onChoiceSYRecordsAction(ActionEvent event) {
        btnLoadStudentRecord.setDisable(choiceSYRecords.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnLoadStudentRecordAction(ActionEvent event) {
        if(choiceSYRecords.getSelectionModel().getSelectedItem() == null){
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
    protected void onBtnStudentRecordSearch(ActionEvent event){
        choiceSYRecords.setDisable(true);
        if(txtStudentRecordSearch.getText().isBlank()){
            displayTable("SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO ORDER BY registration_status", tblStudentRecord);
            return;
        }
        String query = "SELECT student_no, concat(lastname, ', ', firstname) as name, case when gender = 'M' then 'Male' when gender = 'F' then 'Female' else 'Invalid gender' end as gender, bday as birthday, course_code, registration_status FROM VWSTUDENTINFO where student_no regexp(?) or lastname regexp(?) or firstname regexp(?) or course_code regexp(?) ORDER BY registration_status";
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, txtStudentRecordSearch.getText());
            ps.setString(2, txtStudentRecordSearch.getText());
            ps.setString(3, txtStudentRecordSearch.getText());
            ps.setString(4, txtStudentRecordSearch.getText());

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblStudentRecord, rs);
        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying student record:"+e);
        }
    }

    @FXML
    protected void onTxtStudentRecordSearchKeyTyped(KeyEvent event) {

    }

    @FXML
    protected void onBtnGradesEntryAction(ActionEvent event) throws IllegalAccessException {
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
        if(comboBoxYearBlock.getSelectionModel().getSelectedItem() == null){
            comboBoxSubjectCode.getItems().clear();
            return;
        }
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
        tblStudentGrades.getColumns().clear();

        try{
            ps = conn.prepareStatement("select v.student_no, s.lastname, s.firstname, s.registration_status, v.grade, v.remark from vwgradeentry v inner join vwstudentinfo s on v.student_no = s.STUDENT_NO where school_year = ? and semester = ? and block = ? and subject_code = ? and v.student_no in(select student_no from enrollment where status = 'Enrolled')");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxYearBlock.getSelectionModel().getSelectedItem());
            ps.setString(4, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
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
    private void addSY(){
        SimpleStringProperty sy = new SimpleStringProperty();
        Form schoolYear = Form.of(
                Group.of(
                    Field.ofStringType("")
                        .bind(sy)
                        .required("SY is required")
                        .label("School Year")
                        .validate(RegexValidator.forPattern("^2[0-9]{3}-2[0-9]{3}", "Must be a valid school year format ex: 2023-2024"))
                )
        ).title("Add School Year")
            .binding(BindingMode.CONTINUOUS);

        FormRenderer formRenderer = new FormRenderer(schoolYear);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add School Year");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !schoolYear.isValid(),schoolYear.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return sy;
            }
            return null;
        });
        AlertMessage.addIcon(dialog);


        Optional<SimpleStringProperty> newSY = dialog.showAndWait();
        if(newSY.isEmpty())
            return;

        try{
            ps = conn.prepareStatement("INSERT INTO SY VALUES(?)");
            ps.setString(1, sy.get());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully added school year.");
            btnSY.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("You may have added an already existing school year.");
        }
    }
    private void addSem(){
        SimpleStringProperty sem = new SimpleStringProperty();
        Form semester = Form.of(
                        Group.of(
                                Field.ofStringType("")
                                        .bind(sem)
                                        .required("Semester is required")
                                        .label("Semester")
                                        .validate(RegexValidator.forPattern("[A-Z1-9]{1}", "Must have a length of 1 and be alphanumeric."))
                        )
                ).title("Add Semester")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(semester);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add Semester");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !semester.isValid(),semester.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return sem;
            }
            return null;
        });
        AlertMessage.addIcon(dialog);


        Optional<SimpleStringProperty> newSem = dialog.showAndWait();
        if(newSem.isEmpty())
            return;
        try{
            ps = conn.prepareStatement("INSERT INTO SEMESTER VALUES(?)");
            ps.setString(1, sem.get());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully added semester.");
            btnSemester.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while adding semester.");
        }
    }
    private void addCollege(){
        SimpleStringProperty college_code = new SimpleStringProperty();
        SimpleStringProperty description = new SimpleStringProperty();

        Form college = Form.of(
                        Group.of(
                                Field.ofStringType("")
                                        .bind(college_code)
                                        .required("College Code is required")
                                        .label("College Code")
                                        .validate(RegexValidator.forPattern("^.{1,20}$", "Length must be between 1 and 20")),
                                Field.ofStringType("")
                                        .bind(description)
                                        .required("College description is required.")
                                        .label("Description")
                                        .validate(RegexValidator.forPattern("^.{1,100}$", "Length must be between 1 and 100"))
                        )
                ).title("Add College")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(college);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add College");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !college.isValid(),college.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return List.of(college_code, description);
            }
            return null;
        });
        AlertMessage.addIcon(dialog);


        Optional<List<SimpleStringProperty>> newCollege = dialog.showAndWait();
        if(newCollege.isEmpty())
            return;
        try{
            ps = conn.prepareStatement("INSERT INTO COLLEGE VALUES(?, ?, ?, '9999-12-31', 'A')");
            ps.setString(1, newCollege.get().get(0).get());
            ps.setString(2, newCollege.get().get(1).get());
            ps.setString(3, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully added college.");
            btnCollege.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while adding college.");
        }
    }
    private void addCourse(){
        SimpleStringProperty course_code = new SimpleStringProperty();
        SimpleStringProperty description = new SimpleStringProperty();
        SimpleListProperty<String> college_codeList = new SimpleListProperty<>();
        college_codeList.set(FXCollections.observableArrayList(Database.fetch("SELECT COLLEGE_CODE FROM COLLEGE WHERE STATUS = 'A'")));
        ObjectProperty<String> college_code = new SimpleObjectProperty<>();

        Form course = Form.of(
                        Group.of(
                                Field.ofStringType("")
                                        .bind(course_code)
                                        .required("Course code is required.")
                                        .label("Course Code")
                                        .validate(RegexValidator.forPattern("^.{1,20}$", "Length must be between 1 and 20")),
                                Field.ofStringType("")
                                        .bind(description)
                                        .required("Course description is required.")
                                        .label("Description")
                                        .validate(RegexValidator.forPattern("^.{1,100}$", "Length must be between 1 and 100")),
                                Field.ofSingleSelectionType(college_codeList)
                                        .bind(college_codeList, college_code)
                                        .required("College Code is required")
                                        .label("College Code")
                        )
                ).title("Add Course")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(course);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add Course");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !course.isValid(),course.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return List.of(course_code, description, college_code);
            }
            return null;
        });
        AlertMessage.addIcon(dialog);


        Optional<List<Property>> newCourse = dialog.showAndWait();
        if(newCourse.isEmpty())
            return;
        try{
            ps = conn.prepareStatement("INSERT INTO COURSE VALUES(?, ?, ?, ?, '9999-12-31', 'A')");
            ps.setString(1, newCourse.get().get(0).getValue().toString());
            ps.setString(2, newCourse.get().get(1).getValue().toString());
            ps.setString(3, newCourse.get().get(2).getValue().toString());
            ps.setString(4, DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully added course.");
            btnCourse.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while adding course: " + e);
        }
    }
    private void addSubject(){
        SimpleStringProperty subject_code = new SimpleStringProperty();
        SimpleStringProperty description = new SimpleStringProperty();
        SimpleIntegerProperty units = new SimpleIntegerProperty();
        SimpleStringProperty curriculum = new SimpleStringProperty();
        SimpleListProperty<String> college_codeList = new SimpleListProperty<>();
        college_codeList.set(FXCollections.observableArrayList(Database.fetch("SELECT COLLEGE_CODE FROM COLLEGE WHERE STATUS = 'A'")));
        ObjectProperty<String> college_code = new SimpleObjectProperty<>();
        Form course = Form.of(
                        Group.of(
                                Field.ofStringType("")
                                        .bind(subject_code)
                                        .required("Subject code is required.")
                                        .label("Subject Code")
                                        .validate(RegexValidator.forPattern("^.{1,20}$", "Length must be between 1 and 20")),
                                Field.ofStringType("")
                                        .bind(description)
                                        .required("Subject description is required.")
                                        .label("Description")
                                        .validate(RegexValidator.forPattern("^.{1,100}$", "Length must be between 1 and 100")),
                                Field.ofIntegerType(units)
                                        .bind(units)
                                        .validate(IntegerRangeValidator.between(1, 9, "Value must range between 1-9 only"))
                                        .required("Units is required.")
                                        .label("Units"),
                                Field.ofStringType("")
                                        .bind(curriculum)
                                        .label("Curriculum")
                                        .validate(RegexValidator.forPattern("^.{1,10}$",  "Must have a length of 1 to 10 only"))
                                        .required("Curriculum is required"),
                                Field.ofSingleSelectionType(college_codeList)
                                        .bind(college_codeList, college_code)
                                        .required("College Code is required")
                                        .label("College Code")
                                )
                ).title("Add Subject")
                .binding(BindingMode.CONTINUOUS);
        FormRenderer formRenderer = new FormRenderer(course);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Add Subject");
        ButtonType saveConfigButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !course.isValid(),course.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return List.of(subject_code, description, units, curriculum, college_code);
            }
            return null;
        });
        AlertMessage.addIcon(dialog);


        Optional<List<Property>> newSubject = dialog.showAndWait();
        if(newSubject.isEmpty())
            return;
        try{
            ps = conn.prepareStatement("INSERT INTO SUBJECT VALUES(?, ?, ?, ?, ?, 'A')");
            ps.setString(1, newSubject.get().get(0).getValue().toString());
            ps.setString(2, newSubject.get().get(1).getValue().toString());
            ps.setInt(3, Integer.parseInt(newSubject.get().get(2).getValue().toString()));
            ps.setString(4, newSubject.get().get(3).getValue().toString());
            ps.setString(5, newSubject.get().get(4).getValue().toString());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully added subject.");
            btnSubject.fire();
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while adding subject: " + e);
        }
    }
    private void searchCollege(){
        try{
            ps = conn.prepareStatement("SELECT * FROM COLLEGE WHERE college_code regexp(?) or description regexp(?) or date_opened regexp(?) or date_closed regexp(?)");
            ps.setString(1, txtSearchManage.getText());
            ps.setString(2, txtSearchManage.getText());
            ps.setString(3, txtSearchManage.getText());
            ps.setString(4, txtSearchManage.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"COLLEGE", "COLLEGE_CODE"}, new Runnable() {
                @Override
                public void run() {
                    btnCollege.fire();
                }
            });
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while searching for your query: " + e);
        }
    }
    private void searchCourse(){
        try{
            ps = conn.prepareStatement("SELECT * FROM COURSE WHERE course_code regexp(?) or description regexp(?) or date_opened regexp(?) or date_closed regexp(?) or college_code regexp(?)");
            ps.setString(1, txtSearchManage.getText());
            ps.setString(2, txtSearchManage.getText());
            ps.setString(3, txtSearchManage.getText());
            ps.setString(4, txtSearchManage.getText());
            ps.setString(5, txtSearchManage.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"COURSE", "COURSE_CODE"}, new Runnable() {
                @Override
                public void run() {
                    btnCourse.fire();
                }
            });

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while searching for your query: " + e);
        }
    }
    private void searchSubject(){
        try{
            ps = conn.prepareStatement("SELECT subject_code, description, units, curriculum, college_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status FROM SUBJECT WHERE SUBJECT_CODE <> '00000' and (subject_code regexp(?) or description regexp(?) or college_code regexp(?) or curriculum regexp(?))");
            ps.setString(1, txtSearchManage.getText());
            ps.setString(2, txtSearchManage.getText());
            ps.setString(3, txtSearchManage.getText());
            ps.setString(4, txtSearchManage.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"SUBJECT", "SUBJECT_CODE"}, new Runnable() {
                @Override
                public void run() {
                    btnSubject.fire();
                }
            });


        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while searching for your query: " + e);
        }
    }
    @FXML
    protected void onBtnSearchManage(){
        String current = lblManage.getText().toUpperCase().replace("MANAGE", "").replace(" ", "");
        if(txtSearchManage.getText().isBlank()){
            switch(current){
                case "COLLEGES":
                    btnCollege.fire();
                    break;
                case "COURSES":
                    btnCourse.fire();
                    break;
                case "SUBJECTS":
                    btnSubject.fire();
                    break;
                default:
                    System.out.println("Invalid search operation");
            }
            return;
        }
        switch(current){
            case "COLLEGES":
                searchCollege();
                break;
            case "COURSES":
                searchCourse();
                break;
            case "SUBJECTS":
                searchSubject();
                break;
            default:
                System.out.println("Invalid search operation");
        }
    }
    @FXML
    protected void onBtnManageAddAction(){
        String current = lblManage.getText().toUpperCase().replace("MANAGE", "").replace(" ", "");
        switch(current){
            case "SCHOOLYEAR":
                addSY();
                break;
            case "SEMESTER":
                addSem();
                break;
            case "COLLEGES":
                addCollege();
                break;
            case "COURSES":
                addCourse();
                break;
            case "SUBJECTS":
                addSubject();
                break;
            default:
                System.out.println("Invalid add operation");
        }
    }

    @FXML
    protected void onBtnManageDelete(){
        String current = lblManage.getText().toUpperCase().replace("MANAGE", "").replace(" ", "");
        switch(current){
            case "SCHOOLYEAR":
                deleteEntry("SY", "SY", tblManage, new Runnable() {
                    @Override
                    public void run() {
                        btnSY.fire();
                    }
                });
                break;
            case "SEMESTER":
                deleteEntry("SEMESTER", "SEMESTER", tblManage, new Runnable() {
                    @Override
                    public void run() {
                        btnSemester.fire();
                    }
                });
                break;
            case "COLLEGES":
                deleteEntry("COLLEGE", "COLLEGE_CODE", tblManage, new Runnable() {
                    @Override
                    public void run() {
                        btnCollege.fire();
                    }
                });
                break;
            case "COURSES":
                deleteEntry("COURSE", "COURSE_CODE", tblManage, new Runnable() {
                    @Override
                    public void run() {
                        btnCourse.fire();
                    }
                });
                break;
            case "SUBJECTS":
                deleteEntry("SUBJECT", "SUBJECT_CODE", tblManage, new Runnable() {
                    @Override
                    public void run() {
                        btnSubject.fire();
                    }
                });
                break;
            default:
                System.out.println("Invalid add operation");
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
        searchManageGroup.setVisible(false);


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
            sy.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                @Override
                public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
                    return new WrappingTextFieldTableCell("^2[0-9]{3}-2[0-9]{3}", "Must be a valid school year format ex: 2023-2024");
                }
            });
            sy.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> event) {
                    Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Editing this will affect all records in the database that use this school year. Do you still want to edit?");
                    if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
                        AlertMessage.showInformationAlert("Cancelled edit.");
                        btnSY.fire();
                        return;
                    }
                    try{
                        ps = conn.prepareStatement("UPDATE SY SET SY = ? WHERE SY = ?");
                        ps.setString(1, event.getNewValue());
                        ps.setString(2, event.getOldValue());
                        ps.executeUpdate();
                        AlertMessage.showInformationAlert("Successfully edited school year!");
                    }catch(Exception e){
                        AlertMessage.showErrorAlert("An error occurred while editing school year: "+ e);
                    }
                    btnSY.fire();
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
                    if(t.getOldValue().equalsIgnoreCase("Closed") && t.getNewValue().equalsIgnoreCase("Closed")){
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
        searchManageGroup.setVisible(false);

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

            sem.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                @Override
                public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
                    return new WrappingTextFieldTableCell("[A-Z1-9]{1}", "Must have a length of 1 and be alphanumeric.");
                }
            });
            sem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ObservableList<String>, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ObservableList<String>, String> event) {
                    Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Editing this will affect all records in the database that use this school year. Do you still want to edit?");
                    if(confirm.isEmpty() || confirm.get() == ButtonType.NO){
                        AlertMessage.showInformationAlert("Cancelled edit.");
                        btnSemester.fire();
                        return;
                    }
                    try{
                        ps = conn.prepareStatement("UPDATE SY SET SY = ? WHERE SY = ?");
                        ps.setString(1, event.getNewValue());
                        ps.setString(2, event.getOldValue());
                        ps.executeUpdate();
                        AlertMessage.showInformationAlert("Successfully edited school year!");
                    }catch(Exception e){
                        AlertMessage.showErrorAlert("An error occurred while editing school year: "+ e);
                    }
                    btnSemester.fire();
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
        searchManageGroup.setVisible(true);

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
        searchManageGroup.setVisible(true);

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
        lblManage.setText("MANAGE SUBJECTS");
        searchManageGroup.setVisible(true);

        try{
            ps = conn.prepareStatement("SELECT subject_code, description, units, curriculum, college_code, case when status = 'A' then 'Active' when status = 'I' then 'Inactive' else 'Invalid status' end as status FROM SUBJECT WHERE SUBJECT_CODE <> '00000'");
            rs = ps.executeQuery();
            TableViewUtils.generateEditableTableFromResultSet(tblManage, rs, new String[]{"SUBJECT", "SUBJECT_CODE"},  new Runnable() {
                @Override
                public void run() {
                    btnSubject.fire();
                }
            });

        } catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying subjects: " + e);
        }
    }


    @FXML
    protected void onBtnChangePasswordAction(ActionEvent event) {
        SimpleStringProperty oldPass = new SimpleStringProperty();
        SimpleStringProperty newPass = new SimpleStringProperty();
        SimpleStringProperty confirmPass = new SimpleStringProperty();

        Form newPassword = Form.of(
                Group.of(
                        Field.ofPasswordType("")
                                .bind(oldPass)
                                .required("Please enter your old password")
                                .label("Old Password"),

                        Field.ofPasswordType("")
                                .bind(newPass)
                                .required("Please enter your new password")
                                .label("New Password")
                                .validate(RegexValidator.forPattern("[0-9a-zA-Z!@#$%]{8,}",
                                        "Must be at least 8 characters long and contains only alphanumeric or !@#$%")),
                        Field.ofPasswordType("")
                                .bind(confirmPass)
                                .required("Please confirm your new password")
                                .label("Confirm Password")
                                .validate(RegexValidator.forPattern("[0-9a-zA-Z!@#$%]{8,}",
                                        "Must be at least 8 characters long and contains only alphanumeric or !@#$%"))

                )
        ).binding(BindingMode.CONTINUOUS)
                .title("Change Password");


        FormRenderer formRenderer = new FormRenderer(newPassword);
        formRenderer.setPrefWidth(700);
        Dialog dialog = new Dialog();
        dialog.setTitle("Change Password");
        ButtonType saveConfigButtonType = new ButtonType("Change Password", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveConfigButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(formRenderer);
        dialog.getDialogPane()
                .lookupButton(saveConfigButtonType)
                .disableProperty()
                .bind(Bindings.createBooleanBinding(() -> !newPassword.isValid(),newPassword.validProperty()));
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveConfigButtonType) {
                return List.of(oldPass, newPass, confirmPass);
            }
            return null;
        });

        Optional<List<SimpleStringProperty>> changePass = dialog.showAndWait();
        if(changePass.isEmpty())
            return;

        if(!newPass.get().equals(confirmPass.get())){
            AlertMessage.showErrorAlert("Your passwords do not match. Please try again.");
            return;
        }

        try{
            ps = conn.prepareStatement("SELECT password from account where account_no = ?");
            ps.setString(1, employee.getEmployeeID());
            rs = ps.executeQuery();
            if(rs.next() && !BCrypt.checkpw(changePass.get().get(0).get(), rs.getString(1))){
                AlertMessage.showErrorAlert("The old password you entered is incorrect. Please try again.");
                return;
            }

            ps = conn.prepareStatement("UPDATE account set password = ? where account_no = ?");
            ps.setString(1, BCrypt.hashpw(changePass.get().get(1).get(), BCrypt.gensalt()));
            ps.setString(2, employee.getEmployeeID());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Your password has been changed.");
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while changing passwords: " + e);
        }
    }

    @FXML
    protected void onSelectTableCategory(ActionEvent event) {
        String strChoice = cmbSelectTableCategory.getValue();

        switch(strChoice){
            case "TRANSACTION":
                //Visible Property
                gridTransaction.setVisible(true);
                gridRecords.setVisible(false);
                gridDataEntry.setVisible(false);
                gridMaintenance.setVisible(false);
                //Disable Property
                gridTransaction.setDisable(false);
                gridRecords.setDisable(true);
                gridDataEntry.setDisable(true);
                gridMaintenance.setDisable(true);
                navIkonli.setIconLiteral("mdal-credit_card");
                break;
            case "RECORDS":
                //Visible Property
                gridTransaction.setVisible(false);
                gridRecords.setVisible(true);
                gridDataEntry.setVisible(false);
                gridMaintenance.setVisible(false);
                //Disable Property
                gridTransaction.setDisable(true);
                gridRecords.setDisable(false);
                gridDataEntry.setDisable(true);
                gridMaintenance.setDisable(true);
                navIkonli.setIconLiteral("mdmz-receipt");
                break;
            case "DATA ENTRY":
                //Visible Property
                gridTransaction.setVisible(false);
                gridRecords.setVisible(false);
                gridDataEntry.setVisible(true);
                gridMaintenance.setVisible(false);
                //Disable Property
                gridTransaction.setDisable(true);
                gridRecords.setDisable(true);
                gridDataEntry.setDisable(false);
                gridMaintenance.setDisable(true);
                navIkonli.setIconLiteral("mdoal-calendar_today");
                break;
            case "MAINTENANCE":
                //Visible Property
                gridTransaction.setVisible(false);
                gridRecords.setVisible(false);
                gridDataEntry.setVisible(false);
                gridMaintenance.setVisible(true);
                //Disable Property
                gridTransaction.setDisable(true);
                gridRecords.setDisable(true);
                gridDataEntry.setDisable(true);
                gridMaintenance.setDisable(false);
                navIkonli.setIconLiteral("eli-wrench");
                break;
            default:
                System.out.print("Logic Error");
        }
    }

    @FXML
    protected void onBtnTuitionAction(ActionEvent event){
        currentPane.setVisible(false);
        currentPane = tuitionContainer;
        currentPane.setVisible(true);
        try{
            ps = conn.prepareStatement("SELECT * FROM tuition");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblTuitionFee, rs);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying tuition table " + e);
        }
    }

    @FXML
    protected void onTblTuitionClick(MouseEvent event){
        txtTuitionDesc.setText(tblTuitionFee.getSelectionModel().getSelectedItem().get(0));
        txtTuitionAmount.setText(tblTuitionFee.getSelectionModel().getSelectedItem().get(1));
    }

    @FXML
    protected void onBtnAddTuition(ActionEvent event){
        String description = txtTuitionDesc.getText();
        BigDecimal amount;

        try {
            amount = new BigDecimal(txtTuitionAmount.getText());
        } catch (NumberFormatException e) {
            AlertMessage.showErrorAlert("Invalid amount. Please enter a numeric value.");
            return;
        }

        try {
            ps = conn.prepareStatement("INSERT INTO tuition VALUES (?, ?)");
            ps.setString(1, description);
            ps.setBigDecimal(2, amount);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                AlertMessage.showInformationAlert("Fee successfully added!");
            }

            ps = conn.prepareStatement("SELECT * FROM tuition");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblTuitionFee, rs);
        } catch (SQLException e) {
            AlertMessage.showErrorAlert("Error adding fee: " + e.getMessage());
        }
    }

    @FXML
    protected void onBtnUpdateTuition(ActionEvent event) {
        String description = txtTuitionDesc.getText();
        BigDecimal amount;

        try {
            amount = new BigDecimal(txtTuitionAmount.getText());
        } catch (NumberFormatException e) {
            AlertMessage.showErrorAlert("Invalid amount. Please enter a numeric value.");
            return;
        }

        try {
            ps = conn.prepareStatement("UPDATE tuition SET amount = ? WHERE description = ?");
            ps.setBigDecimal(1, amount);
            ps.setString(2, description);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                AlertMessage.showInformationAlert("Update successful!");
            } else {
                AlertMessage.showErrorAlert("No rows were updated. Check if the description exists.");
            }

            ps = conn.prepareStatement("SELECT * FROM tuition");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblTuitionFee, rs);
        } catch (SQLException e) {
            AlertMessage.showErrorAlert("Error updating tuition: " + e.getMessage());
        }
    }

    @FXML
    protected void onBtnDeleteTuition(ActionEvent event){
        String description = txtTuitionDesc.getText();
        BigDecimal amount;

        try {
            amount = new BigDecimal(txtTuitionAmount.getText());
        } catch (NumberFormatException e) {
            AlertMessage.showErrorAlert("Invalid amount. Please enter a numeric value.");
            return;
        }

        try {
            ps = conn.prepareStatement("DELETE FROM tuition WHERE description = ? AND amount = ?");
            ps.setString(1, description);
            ps.setBigDecimal(2, amount);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                AlertMessage.showInformationAlert("Fee was successfully deleted!");
            } else {
                AlertMessage.showErrorAlert("No fees were deleted. Check if the desription exists.");
            }

            ps = conn.prepareStatement("SELECT * FROM tuition");
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblTuitionFee, rs);
        } catch (SQLException e) {
            AlertMessage.showErrorAlert("Error deleting fee: " + e.getMessage());
        }
    }

}
