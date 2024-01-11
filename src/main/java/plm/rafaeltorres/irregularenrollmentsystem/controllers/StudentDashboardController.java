package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.util.BindingMode;
import com.dlsc.formsfx.model.validators.RegexValidator;
import com.dlsc.formsfx.view.renderer.FormRenderer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
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
import org.springframework.security.crypto.bcrypt.BCrypt;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.Schedule;
import plm.rafaeltorres.irregularenrollmentsystem.model.Student;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class StudentDashboardController extends Controller {
    private String currentSY = "2023-2024";
    private String currentSem = "1";
    private Student student;
    private Pane currentPane;
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
    private TableView<ObservableList<String>> tblGrades;
    @FXML
    private ComboBox<String> choiceSY;
    @FXML
    private ComboBox<String> choiceSemester;
    @FXML
    private Pane tuitionContainer;
    @FXML
    private TableView<ObservableList<String>> tblSubjects;
    @FXML
    private Button btnAdd;
    @FXML
    private TableView<ObservableList<String>> tblSchedule;
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
    @FXML
    private Label lblTotalUnits;
    @FXML
    private Button btnLoadGrades;
    @FXML
    private Pane irregularEnrollmentContainer;
    @FXML
    private TableView<ObservableList<String>> tblIrregScheds;
    @FXML
    private TextField txtSubjectSearch;
    @FXML
    private Button btnAddIrreg;
    @FXML
    private TableView tblTuitionFees;
    @FXML
    private Label lblTotalFee;
    @FXML
    private Label lblTuitionF;
    @FXML
    private Label lblMiscF;
    @FXML
    private Label lblNumberUnits;





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

        try{
            Integer.parseInt(currentSem);
            lblSemester.setText(StringUtils.integerToPlace(Integer.parseInt(currentSem)) + " Semester A.Y. " + currentSY);
        }catch(NumberFormatException e){
            lblSemester.setText("Summer Semester A.Y. " + currentSY);
        }


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
            btnEnrollRegular.setVisible(false);
            tblSubjects.setPlaceholder(new Label("Please wait for your department chairperson to assign your schedule."));
        }
        else{
            btnSubmit.setVisible(true);
            btnRemove.setVisible(true);
            tblSubjects.setPlaceholder(new Label("No schedules to display."));

            try{
                ps = conn.prepareStatement("SELECT * FROM student_schedule where student_no = ? and sy = ? and semester = ?");
                ps.setString(1, student.getStudentNo());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);
                rs = ps.executeQuery();
            }catch(Exception e){
                System.out.println(e);
            }
        }
        try{
            ps = conn.prepareStatement("select status from enrollment where student_no = ? and sy = ? and semester = ?");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            boolean res = rs.next();
            String status = "";
            if(res)
                status = rs.getString(1);

            if(status.equalsIgnoreCase("DECLINED")){
                AlertMessage.showInformationAlert("Your schedule was not approved by the chairperson. Please submit a new schedule.");
            }


            btnTuition.setDisable(!status.equalsIgnoreCase("ENROLLED"));
            btnEnroll.setDisable(status.equalsIgnoreCase("ENROLLED"));
            notEnrolledGroup.setVisible(!status.equalsIgnoreCase("ENROLLED"));
            enrolledGroup.setVisible(status.equalsIgnoreCase("ENROLLED"));
            btnDownloadSER.setDisable(!status.equalsIgnoreCase("ENROLLED"));

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
            ps = conn.prepareStatement("SELECT DISTINCT SY FROM GRADE WHERE STUDENT_NO = ? and subject_code <> '00000'");
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
        else{
            // display default image
            URL resource = MainScene.class.getResource("assets/img/md-person-2.png");
            Image defaultImage = new Image(resource.toExternalForm(), false);
            ImagePattern pattern = new ImagePattern(defaultImage);
            imgContainer.setFill(pattern);
            imgDashboardContainer.setFill(pattern);
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
        SimpleStringProperty oldPass = new SimpleStringProperty();
        SimpleStringProperty newPass = new SimpleStringProperty();
        SimpleStringProperty confirmPass = new SimpleStringProperty();

        Form newPassword = Form.of(
                        com.dlsc.formsfx.model.structure.Group.of(
                                com.dlsc.formsfx.model.structure.Field.ofPasswordType("")
                                        .bind(oldPass)
                                        .required("Please enter your old password")
                                        .label("Old Password"),

                                com.dlsc.formsfx.model.structure.Field.ofPasswordType("")
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
            ps.setString(1, student.getStudentNo());
            rs = ps.executeQuery();
            if(rs.next() && !BCrypt.checkpw(changePass.get().get(0).get(), rs.getString(1))){
                AlertMessage.showErrorAlert("The old password you entered is incorrect. Please try again.");
                return;
            }

            ps = conn.prepareStatement("UPDATE account set password = ? where account_no = ?");
            ps.setString(1, BCrypt.hashpw(changePass.get().get(1).get(), BCrypt.gensalt()));
            ps.setString(2, student.getStudentNo());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Your password has been changed.");
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while changing passwords: " + e);
        }
    }
    @FXML
    protected void btnDownloadOnMouseClicked(MouseEvent event) throws IOException, SQLException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        ps = conn.prepareStatement("select " +
                "v.subject_code, " +
                "v.block, " +
                "v.description, " +
                "v.CREDITS, " +
                "v.schedule, " +
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
        PDFGenerator.generateSER(stage, student, rs);
        AlertMessage.showInformationAlert("Successfully downloaded your SER!");
    }

    @FXML
    protected void onBtnSearchScheduleAction(ActionEvent event){
        if(txtSubjectSearch.getText().isBlank()){
            btnEnroll.fire();
            return;
        }
        try{
            ps = conn.prepareStatement("SELECT subject_code, course, year, block, description, schedule, credits, professor, 20-total_students as available_slots, case when 30-total_students = 0 then 'CLOSED' else 'OPEN' end as status from vwsubjectscheduleswithtotalstudents where sy = ? and semester = ? and college_code = ? and subject_code not in (select subject_code from student_schedule where sy = ? and semester = ? and student_no = ?) and course = ? and (subject_code regexp(?) or description regexp(?))");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getCollege());
            ps.setString(4, currentSY);
            ps.setString(5, currentSem);
            ps.setString(6, student.getStudentNo());
            ps.setString(7, student.getCourse().replace("BS", ""));
            ps.setString(8, txtSubjectSearch.getText());
            ps.setString(9, txtSubjectSearch.getText());
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblIrregScheds, rs);
        }catch (Exception e){
            AlertMessage.showErrorAlert("An error occurred while displaying schedules: " + e);
        }
    }

    private void displayAvailableScheds() {
        try{
            ps = conn.prepareStatement("SELECT * FROM ENROLLMENT WHERE STUDENT_NO = ? AND SY = ? AND SEMESTER = ? AND STATUS = 'Pending'");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            rs = ps.executeQuery();
            if(rs.next()){
                tblIrregScheds.getItems().clear();
                AlertMessage.showInformationAlert("Please wait for your department chairperson to approve your schedule.");
                return;
            }
        }catch (Exception e) {
            AlertMessage.showErrorAlert("An error occurred while displaying your subjects: " + e);
        }

        try{
            ps = conn.prepareStatement("SELECT subject_code, course, year, block, description, schedule, credits, professor, 20-total_students as available_slots, case when 30-total_students = 0 then 'CLOSED' else 'OPEN' end as status from vwsubjectscheduleswithtotalstudents where sy = ? and semester = ? and college_code = ? and subject_code not in (select subject_code from student_schedule where sy = ? and semester = ? and student_no = ?) and course = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getCollege());
            ps.setString(4, currentSY);
            ps.setString(5, currentSem);
            ps.setString(6, student.getStudentNo());
            ps.setString(7, student.getCourse().replace("BS", ""));
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblIrregScheds, rs);
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
    protected void onTblIrregSchedsMouseClicked(MouseEvent event){
        btnAddIrreg.setDisable(tblIrregScheds.getSelectionModel().getSelectedItem() == null);
    }
    @FXML
    protected void onBtnAddIrregAction(ActionEvent event){
        onBtnAddAction(event);
    }
    @FXML
    protected void onBtnEnrollAction(Event event) throws Exception{
        currentPane.setVisible(false);
        btnAddIrreg.setDisable(true);
        if(student.getRegistrationStatus().equals("Irregular")){
            currentPane = irregularEnrollmentContainer;
            currentPane.setVisible(true);
            btnSubmit.setDisable(false);
            displayAvailableScheds();
        }
        else {
            currentPane = enrollContainer;
            currentPane.setVisible(true);
            btnAdd.setVisible(false);
            try {
                ps = conn.prepareStatement("select v.subject_code, v.description, v.block, v.SCHEDULE, v.CREDITS, v.PROFESSOR from vwsubjectschedules v inner join student_schedule s on v.sy = s.sy and v.semester = s.semester and concat(v.course, v.year, v.block) = s.block_no and v.subject_code = s.subject_code "
                     +  "where s.student_no = ? and v.sy = ? and v.semester = ?");
                ps.setString(1, student.getStudentNo());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);;
                TableViewUtils.generateTableFromResultSet(tblSubjects, ps.executeQuery());
                btnEnrollRegular.setVisible(!tblSubjects.getItems().isEmpty());
            }catch(Exception e){
                AlertMessage.showErrorAlert("An error occurred while displaying your subjects: " + e);
            }
        }

    }

    @FXML
    protected void onBtnEnrollRegularAction(ActionEvent event){
        try{
            ps = conn.prepareStatement("UPDATE ENROLLMENT SET status = 'Enrolled', timestamp = ? where sy = ? and semester = ? and student_no = ?");
            ps.setString(1, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.setString(2, currentSY);
            ps.setString(3, currentSem);
            ps.setString(4, student.getStudentNo());
            ps.executeUpdate();
            AlertMessage.showInformationAlert("You have been successfully enrolled! Click on 'Schedule' or 'Tuition' to view more details about your enrollment.");
            btnDashboard.setSelected(true);
            btnEnroll.setSelected(false);
            btnEnroll.setDisable(true);
            btnTuition.setDisable(false);
            btnDownloadSER.setDisable(false);
            onBtnDashboardAction(event);
        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while processing your enrollment: " + e);
        }
    }
    private void displaySched() {
        lblTotalUnits.setText("");
        tblSchedule.setMouseTransparent(!student.getRegistrationStatus().equalsIgnoreCase("IRREGULAR"));
        if(student.getRegistrationStatus().equalsIgnoreCase("IRREGULAR")){
            try{
                ps = conn.prepareStatement("SELECT status from enrollment where student_no = ? and sy = ? and semester = ?");
                ps.setString(1, student.getStudentNo());
                ps.setString(2, currentSY);
                ps.setString(3, currentSem);

                rs = ps.executeQuery();
                rs.next();
                if(rs.getString(1).equalsIgnoreCase("PENDING")){
                    AlertMessage.showInformationAlert("Please wait for your schedule to be approved.");
                    btnSubmit.setDisable(true);
                    return;
                }
                else if(rs.getString(1).equalsIgnoreCase("ENROLLED")){
                    btnSubmit.setVisible(false);
                    btnRemove.setVisible(false);
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }
        try{
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

            int totalUnits = 0;
            for(ObservableList<String> item : tblSchedule.getItems()){
                totalUnits += Integer.parseInt(item.get(4));
            }
            lblTotalUnits.setText("Total Units: "+totalUnits);

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
        displaySched();
    }
    @FXML
    protected void onTblScheduleMouseClicked(MouseEvent event) {
        btnRemove.setDisable(tblSchedule.getSelectionModel().getSelectedItem() == null);
    }

    @FXML
    protected void onBtnSubmitAction(ActionEvent event) {
        // schedule validation
        int totalUnits = 0;

        Map<String, String> map = new HashMap<>();
        List<String> l = new ArrayList<>();

        for(ObservableList<String> item : tblSchedule.getItems()){
            totalUnits += Integer.parseInt(item.get(4));
            l.add(item.get(0));
            if(item.get(1).contains("(lab)"))
                map.put(item.get(0), item.get(0).replace(".1", ""));
            if(item.get(1).contains("(lec)"))
                map.put(item.get(0).replace(".1", ""), item.get(0)+".1");
        }
        for(ObservableList<String> item : tblSchedule.getItems()){
            if(map.containsKey(item.get(0)) && !l.contains(map.get(item.get(0)))){
                AlertMessage.showErrorAlert("You are required to take the respective laboratory/lecture class for " + item.get(0));
                return;
            }
        }

        if(totalUnits < 15){
            AlertMessage.showErrorAlert("You must have a minimum of 15 units to enroll. Please add more subjects to your schedule.");
            return;
        }

        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("You won't be able to add/remove subjects once you have submitted your schedule for approval. Do you wish to proceed?");
        if(confirm.isEmpty() || confirm.get().equals(ButtonType.NO)){
            AlertMessage.showInformationAlert("Cancelled submission.");
            return;
        }
        try{
            ps = conn.prepareStatement("replace into enrollment values(?, ?, ?, 'Pending', ?)");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getStudentNo());
            ps.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            ps.executeUpdate();
            AlertMessage.showInformationAlert("Successfully submitted your schedule for approval. Please wait for your chairperson to approve your schcedule.");
            btnDashboard.fire();

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while processing your schedule:\n"+e);
        }

    }
    @FXML
    protected void onBtnRemoveAction(ActionEvent event) {
        Schedule sched = new Schedule(tblSchedule.getSelectionModel().getSelectedItem());
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to remove "+sched.getSubjectCode()+" from your schedule?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;
        try{
            ps = conn.prepareStatement("delete from student_schedule where sy = ? and semester = ? and student_no = ? and subject_code = ?");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getStudentNo());
            ps.setString(4, sched.getSubjectCode());
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
    protected void onBtnTuitionAction(ActionEvent event){
        String strTuitionFeeQuery = "SELECT* FROM tuition";
        float flTotalFee = 0.00F, flUnitPrice = 0.00F, flSumFee = 0.00F, flSum = 0.00F, flTuition = 0.00F;

        currentPane.setVisible(false);
        currentPane = tuitionContainer;
        currentPane.setVisible(true);
        tblTuitionFees.setEditable(false);
        tblTuitionFees.setMouseTransparent(true);

        try
        {
            ps = conn.prepareStatement(strTuitionFeeQuery);
            rs = ps.executeQuery();
            TableViewUtils.generateTableFromResultSet(tblTuitionFees, rs);

            ps = conn.prepareStatement("SELECT amount FROM enrollment_system.tuition WHERE description = \"Tuition Fee (Price per Unit)\"");
            rs = ps.executeQuery();
            if(rs.next())
            {
                flUnitPrice = rs.getFloat(1);
            }

            ps = conn.prepareStatement("SELECT amount FROM enrollment_system.tuition WHERE description <> '* Except Tuition Fee (Price per Unit)';");
            rs = ps.executeQuery();
            while(rs.next())
            {
                flSum = rs.getFloat(1);
                flSumFee += flSum;
            }

            ps = conn.prepareStatement("select " +
                    "v.CREDITS " +
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

            int totalUnits = 0;
            while(rs.next())
            {
                totalUnits += rs.getInt(1);
            }

            flTuition = totalUnits*flUnitPrice;

            flTotalFee = flTuition+flSumFee;

            lblTuitionF.setText(String.format("%,.2f", flTuition));
            lblMiscF.setText(String.format("%,.2f", flSumFee));
            lblNumberUnits.setText("Tuition Fee x "+totalUnits+" Units");

            lblTotalFee.setText("Amount Due: "+String.format("%,.2f", flTotalFee));
        }

        catch(Exception e)
        {
            AlertMessage.showErrorAlert("An error occurred while generating your tuition invoice: " + e);
        }
    }

    @FXML
    protected void onBtnSaveTuitionAction(ActionEvent event) throws IOException {
        if(tblTuitionFees.getItems().isEmpty())
            return;
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        int totalUnits = 0;

        try{
            ps = conn.prepareStatement("select " +
                    "v.CREDITS " +
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

            while(rs.next())
            {
                totalUnits += rs.getInt(1);
            }
        }catch(Exception e){
            System.out.println(e);
        }

        PDFGenerator.generateTuitionSummary(stage, student, tblTuitionFees, totalUnits);
        AlertMessage.showInformationAlert("Successfully downloaded invoice!");
    }
    @FXML
    protected void onBtnGradesAction(ActionEvent event){
        currentPane.setVisible(false);
        currentPane = gradesContainer;
        currentPane.setVisible(true);
        tblGrades.setEditable(false);
        tblGrades.setMouseTransparent(true);
    }
    @FXML
    protected void onTblSubjectsMouseClicked(MouseEvent event) {
        ObservableList<String> s = tblSubjects.getSelectionModel().getSelectedItem();
        btnAdd.setDisable(s == null || (s.get(s.size()-1).equals("CLOSED")));
    }
    @FXML
    protected void onBtnAddAction(ActionEvent event) {
        Schedule sched = new Schedule(tblIrregScheds.getSelectionModel().getSelectedItem());
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to add "+sched.getSubjectCode()+" to your schedule?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;

        try{
            ps = conn.prepareStatement("INSERT INTO STUDENT_SCHEDULE VALUES(?, ?, ?, ?, ?, ?)");
            ps.setString(1, currentSY);
            ps.setString(2, currentSem);
            ps.setString(3, student.getStudentNo());
            ps.setString(4, sched.getSubjectCode());
            ps.setString(5, student.getCollege());
            ps.setString(6, sched.getBlock());

            ps.executeUpdate();
            tblSubjects.getItems().clear();
            tblSubjects.getColumns().clear();
            displayAvailableScheds();
            AlertMessage.showInformationAlert("Successfully added "+sched.getSubjectCode());
            btnEnroll.fire();
        } catch(Exception e){
            System.out.println(e);
            AlertMessage.showErrorAlert("An error as occurred while adding subject: "+e.toString());
        }
    }

    @FXML
    protected void onSYComboAction(ActionEvent event){
        String sy = choiceSY.getSelectionModel().getSelectedItem();
        if(sy == null)
            return;

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



    }
    @FXML
    protected void onBtnLoadGradesAction(ActionEvent event){
        String semester = choiceSemester.getSelectionModel().getSelectedItem();
        String sy = choiceSY.getSelectionModel().getSelectedItem();
        if(sy == null){
            AlertMessage.showErrorAlert("Select a valid school year.");
            return;
        }
        if(semester == null){
            AlertMessage.showErrorAlert("Select a valid semester.");
            return;
        }
        try{
            ps = conn.prepareStatement("select " +
                    "ss.subject_code as `SUBJECT CODE`," +
                    "    ss.description as `SUBJECT DESCRIPTION`," +
                    "    ss.units as UNITS," +
                    "    ss.grade as GRADE, ss.remark " +
                    "from vwStudentGradeForSYAndSem ss where ss.student_no = ? and ss.sy = ? and ss.semester = ?");
            ps.setString(1, student.getStudentNo());
            ps.setString(2, choiceSY.getSelectionModel().getSelectedItem());
            ps.setString(3, semester);
            rs = ps.executeQuery();
            if(rs.getRow() == 0){
                tblGrades.getItems().clear();
                tblGrades.setPlaceholder(new Label("No grades yet for this SY/Sem"));
            }
            tblGrades.getColumns().clear();

            TableViewUtils.generateTableFromResultSet(tblGrades, rs);

        }catch(Exception e){
            AlertMessage.showErrorAlert("An error occurred while fetching your grades.");
        }
    }





}
