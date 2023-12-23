package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import com.dlsc.formsfx.model.event.FieldEvent;
import com.dlsc.formsfx.model.structure.*;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.model.validators.StringLengthValidator;
import com.dlsc.formsfx.view.controls.SimpleRadioButtonControl;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.beans.binding.Bindings;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Employee;
import plm.rafaeltorres.irregularenrollmentsystem.model.StudentProperty;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.DatePickerTableCell;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;
import plm.rafaeltorres.irregularenrollmentsystem.utils.TableViewUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sideBar.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });

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
        tblSubjects.setPlaceholder(new Label("Please select a block/section."));

        tblSubjects.getSelectionModel().setSelectionMode(null);
        tblStudents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        txtCurrentSYandSem.setText("SY " + currentSY + " - " + currentSem);

        // initialize comboboxes
        try{
            ps = conn.prepareStatement("SELECT college_code from college");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCollege.getItems().add(rs.getString(1));
                comboBoxCollegeGrade.getItems().add(rs.getString(1));
            }

            ps = conn.prepareStatement("SELECT course_code from course");
            rs = ps.executeQuery();
            while(rs.next()){
                comboBoxCourse.getItems().add(rs.getString(1));
            }

            comboBoxYear.setItems(FXCollections.observableArrayList("Any", "1", "2", "3", "4", "5"));
            comboBoxYear.getSelectionModel().selectFirst();


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

            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COLLEGE_CODE = ? AND REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, registrationStatus);
            ps.setString(3, currentSY);
            ps.setString(4, currentSem);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
        } catch(Exception e){
            System.out.println(e);
        }
        tblSubjects.getItems().clear();
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

            ps = conn.prepareStatement("SELECT STUDENT_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
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
        tblSubjects.getItems().clear();
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

            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
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
                ps = conn.prepareStatement("SELECT student_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where (" + currentSY.substring(0, 4) + " - cast(substring(STUDENT_No, 1, 4) as unsigned)+1) = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
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
            ps = conn.prepareStatement("SELECT student_no, concat(LASTNAME, ', ', FIRSTNAME) as NAME, course_code, registration_status from vwstudentinfo where registration_status = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
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
        String registrationStatus = (!btnApprove.isVisible()) ? "REGULAR" : "IRREGULAR";

        if(o == null) {
            tblSubjects.getItems().clear();
            comboBoxBlock.getSelectionModel().clearSelection();
            try{
                ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ?)");
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
            TableColumn item = (TableColumn) tblEnrollees.getColumns().get(i);
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
            if(comboBoxBlock.getSelectionModel().getSelectedItem() != null){
                onComboBoxBlockAction(event);
            };
        }catch(Exception e){
            System.out.println(e);
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
            ps = conn.prepareStatement("INSERT INTO ENROLLMENT VALUES(?, ?, ?)");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, comboBoxStudentNo.getPromptText());
            ps.executeUpdate();

            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE COURSE_CODE = ? AND REGISTRATION_STATUS = 'REGULAR' and student_no not in(select student_no from enrollment where SY = ? and semester = ? )");
            ps.setString(1, comboBoxCourse.getSelectionModel().getSelectedItem());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);

            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblEnrollees, rs);
            tblSubjects.getItems().clear();

            comboBoxStudentNo.getSelectionModel().clearSelection();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    protected void onBtnEnrollAction(ActionEvent event) throws IllegalAccessException {
        btnApprove.setVisible(false);
        btnDisapprove.setVisible(false);
        btnEnrollStudent.setVisible(true);
        comboBoxBlock.setVisible(true);
        tblSubjects.getItems().clear();
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
        try{
            ps = conn.prepareStatement("SELECT STUDENT_No, concat(LASTNAME, ', ', FIRSTNAME) as NAME, COURSE_CODE, REGISTRATION_STATUS FROM VWSTUDENTINFO WHERE REGISTRATION_STATUS = ? and student_no not in(select student_no from enrollment where SY = ? and semester = ?)");
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
        tblSubjects.getItems().clear();
        onEnroll(event);
    }

    @FXML
    protected void onBtnStudentEntryAction(ActionEvent event) throws IllegalAccessException {
        txtStudentSearch.clear();
        currentPane.setVisible(false);
        currentPane = studentEntryContainer;
        currentPane.setVisible(true);
        tblStudents.getSelectionModel().clearSelection();
        btnDeleteStudent.setDisable(tblStudents.getSelectionModel().getSelectedItem() == null);
        tblStudents.getItems().clear();
        tblStudents.getColumns().clear();
        try{
            ps = conn.prepareStatement("SELECT * from vwstudentinfo");
            rs = ps.executeQuery();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                    @Override public void handle(TableColumn.CellEditEvent t) {
                        ObservableList<String> o = (ObservableList<String>) t.getRowValue();
                        System.out.println(o.get(j));
                        o.set(j, t.getNewValue().toString());
                        System.out.println(o);

                        try{
                            Connection conn = Database.connect();
                            PreparedStatement ps = conn.prepareStatement("UPDATE STUDENT SET " + rs.getMetaData().getColumnName(j+1) + " = ? " + "WHERE STUDENT_NO = ?");
                            ps.setString(1, o.get(j));
                            ps.setString(2, o.get(0));
                            ps.executeUpdate();
                            System.out.println(o.get(j) + " from column " + rs.getMetaData().getColumnName(j+1));
                            btnStudentEntry.fire();

                        }catch(Exception e){
                            System.out.println(e);
                        }

                    }
                });
                tblStudents.getColumns().addAll(col);
            }

            for(int i = 0; i < tblStudents.getColumns().size(); ++i){
                TableColumn col = (TableColumn) tblStudents.getColumns().get(i);
                String txt = col.getText().toUpperCase();
                switch(txt){
                    case "AGE":
                    case "PLM EMAIL":
                    case "REGISTRATION STATUS":
                    case "STUDENT NUMBER":
                    case "STUDENT_NO":
                    case "STUDENT_NUMBER":
                        break;
                    case "GENDER":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("M", "F"));
                                    }
                                }
                        );
                        break;
                    case "BIRTHDAY":
                    case "BDAY":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new DatePickerTableCell();
                                    }
                                }
                        );
                        break;
                    case "STATUS":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("A", "I"));
                                    }
                                }
                        );
                        break;
                    case "COLLEGE_CODE":
                    case "COLLEGE":
                        ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COLLEGE_CODE FROM COLLEGE"));
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                    }
                                }
                        );
                        break;
                    case "COURSE_CODE":
                    case "COURSE":
                        comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COURSE_CODE FROM COURSE"));
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                    }
                                }
                        );
                        break;
                    default:
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new TextFieldTableCell(new DefaultStringConverter());

                                    }
                                }
                        );
                }
            }

            TableViewUtils.generateTable(tblStudents, rs);

        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void fetchStudentEntryTable(String query){

    }


    public void refreshStudentTable() throws  IllegalAccessException{
        onBtnStudentEntryAction(new ActionEvent());
    }
    @FXML
    protected void onTxtStudentSearchKeyTyped(KeyEvent event) {
        btnDeleteStudent.setDisable(true);

        if(txtStudentSearch.getText().isBlank()){
            displayTable("SELECT student_number, last_name, first_name, gender, birthday, age, cellphone_number, address, course_code, status, registration_status from vwstudentinfo", tblStudents);
            return;
        }
        String query = "SELECT * from vwstudentinfo where student_no regexp(?) or firstname regexp(?) or lastname regexp(?)";
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
        tblStudents.setEditable(btnEditStudent.isSelected());
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
                                .validate(RegexValidator.forPattern("^[A-Z]{1}[a-z]+( )?([A-Z]{1}[a-z]+)?", "Must have a valid name format ex: Dela Cruz"))
                                .label("Last Name"),
                        Field.ofStringType(student.first_nameProperty()).bind(student.first_nameProperty())
                                .required("Student must have a first name.")
                                .validate(RegexValidator.forPattern("^[A-Z]{1}[a-z]+( )?([A-Z]{1}[a-z]+)?", "Must have a valid name format (uppercase start of name/s) ex: Juan"))
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
//        onBtnClick(event);
    }
    @FXML
    protected void onTblSubjectsMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onTblScheduleMouseClicked(MouseEvent event) {

    }

    @FXML
    protected void onBtnStudentRecordsAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnEmployeeEntryAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnGradesEntryAction(ActionEvent event) throws IllegalAccessException {
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
            ps = conn.prepareStatement("select distinct subject_code from vwgradereport where block = ?");
            ps.setString(1, comboBoxYearBlock.getSelectionModel().getSelectedItem());
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
        tblStudentGrades.getItems().clear();
        tblStudentGrades.getColumns().clear();

        try{
            if(Year.now().getValue() - Integer.parseInt(currentSY.substring(0,4)) == 0){
                ps = conn.prepareStatement("select " +
                        "    s.student_no, " +
                        "    st.LASTNAME, " +
                        "    st.FIRSTNAME, " +
                        "    v.grade, " +
                        "    v.remark " +
                        "from vwGradeReportBackup v " +
                        "    right join student_schedule s on v.subject_code = s.subject_code and v.sy = s.sy and v.semester = s.semester and v.block_no = s.block_no " +
                        "inner join student st on s.student_no = st.STUDENT_NO where s.sy = ? and s.semester = ? and s.block_no = ? and s.subject_code = ?");
                ps.setString(1, currentSY);
                ps.setString(2, currentSem);
                ps.setString(3, comboBoxYearBlock.getSelectionModel().getSelectedItem());
                ps.setString(4, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
            }
            else{
                ps = conn.prepareStatement("select Student_No, concat(Last_Name, ', ', First_Name) as name, grade, remark from vwgradereport where Subject_Code = ? and block = ? and School_Year = ? and semester = ?");
                ps.setString(1, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
                ps.setString(2, comboBoxYearBlock.getSelectionModel().getSelectedItem());
                ps.setString(3, currentSY);
                ps.setString(4, currentSem);
            }
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
                            o.set(o.size()-2, t.getNewValue().toString());
                            System.out.println(o);
                            try {
                                Connection conn = Database.connect();
                                PreparedStatement ps = conn.prepareStatement("UPDATE GRADE SET GRADE = ? WHERE sy = ? and semester = ? and student_no = ? and subject_code = ? and block_no = ?");
                                ps.setDouble(1, Double.parseDouble(o.get(o.size()-2)));
                                ps.setString(2, currentSY);
                                ps.setString(3, currentSem);
                                ps.setString(4, tblStudentGrades.getSelectionModel().getSelectedItem().get(0));
                                ps.setString(5, comboBoxSubjectCode.getSelectionModel().getSelectedItem());
                                ps.setString(6, comboBoxYearBlock.getSelectionModel().getSelectedItem());
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

    }
    @FXML
    protected void onBtnSemesterAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnCollegeAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnCourseAction(ActionEvent event) {

    }
    @FXML
    protected void onBtnSubjectAction(ActionEvent event) {

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
