package plm.rafaeltorres.irregularenrollmentsystem.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.time.LocalDate;

public class StudentProperty {
    private StringProperty student_number;
    private StringProperty first_name;
    private StringProperty last_name;
    private StringProperty email;
    private SimpleObjectProperty<String> course_code;
    private ListProperty<String> courseList;
    private ListProperty<String> genderList;
    private SimpleObjectProperty<String> gender;
    private ObjectProperty<LocalDate> birthday;
    private StringProperty cellphone_number;
    private StringProperty age;
    private StringProperty address;
    private StringProperty status;

    public StringProperty student_numberProperty() {
        if (student_number == null){
            SimpleStringProperty s = new SimpleStringProperty(this, "student_number");
            s.set("");
            student_number = s;
        }
        return student_number;
    }
    public SimpleObjectProperty<String> course_codeProperty() {
        if (course_code == null){
            SimpleObjectProperty<String> s = new SimpleObjectProperty<>(this, "course_code");
            course_code = s;
        }
        return course_code;
    }
    public ListProperty<String> courseListProperty() {
        if (courseList == null){
            ObservableList<String> courses = FXCollections.observableArrayList(Database.fetch("SELECT COURSE_CODE FROM COURSE"));
            ListProperty<String> s = new SimpleListProperty<>(this, "courseList");
            s.set(courses);
            courseList = s;
        }
        return courseList;
    }
    public StringProperty first_nameProperty() {
        if (first_name == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "first_name");
            s.set("");
            first_name = s;
        }
        return first_name;
    }
    public StringProperty last_nameProperty() {
        if (last_name == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "last_name");
            s.set("");
            last_name= s;
        }
        return last_name;
    }
    public ListProperty<String> genderListProperty() {
        if (genderList== null) {
            ListProperty<String> s = new SimpleListProperty<>(this, "genderList");
            s.set(FXCollections.observableArrayList("M", "F"));
            genderList = s;
        }
        return genderList;
    }
    public SimpleObjectProperty<String> genderProperty() {
        if (gender== null) {
            SimpleObjectProperty<String> s = new SimpleObjectProperty<>(this, "gender");
            s.set("");
            gender = s;
        }
        return gender;
    }
    public ObjectProperty<LocalDate> birthdayProperty() {
        if (birthday== null){
            ObjectProperty<LocalDate> s = new SimpleObjectProperty<>();
            s.set(LocalDate.now());
            birthday = s;
        }
        return birthday;
    }
    public StringProperty emailProperty() {
        if (email == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "email");
            s.set("");
            email = s;
        }
        return email;
    }
    public StringProperty statusProperty() {
        if (status == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "status");
            s.set("");
            status = s;
        }
        return status;
    }
    public StringProperty cellphone_numberProperty() {
        if (cellphone_number== null){
            SimpleStringProperty s = new SimpleStringProperty(this, "cellphone_number");
            s.set("");
            cellphone_number = s;
        }
        return cellphone_number;
    }
    public StringProperty ageProperty() {
        if (age == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "age");
            s.set("");
            age = s;
        }
        return age;
    }
    public StringProperty addressProperty() {
        if (address == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "address");
            s.set("");
            address = s;
        }
        return address;
    }

    public String getAddress() {
        return addressProperty().get();
    }

    public String getAge() {
        return ageProperty().get();
    }

    public String getEmail() {
        return emailProperty().get();
    }

    public String getCellphone_number() {
        return cellphone_numberProperty().get();
    }

    public String getStatus() {
        return statusProperty().get();
    }

    public LocalDate getBirthday() {
        return birthdayProperty().get();
    }

    public String getCourse_code() {
        return course_codeProperty().get();
    }
    public ObservableList<String> getCourseList() { return courseListProperty().get(); }

    public String getFirst_name() {
        return first_nameProperty().get();
    }

    public String getGender() {
        return genderProperty().get().toString();
    }
    public ObservableList<String> getGenderList() {
        return genderListProperty().get();
    }

    public String getLast_name() {
        return last_nameProperty().get();
    }

    public String getStudent_number() {
        return student_numberProperty().get();
    }

    public void setAddress(String value) {
        addressProperty().set(value);
    }

    public void setAge(String value) {
        ageProperty().set(value);
    }

    public void setEmail(String value) {
        emailProperty().set(value);
    }

    public void setCellphone_number(String value) {
        cellphone_numberProperty().set(value);
    }

    public void setStatus(String value) {
        statusProperty().set(value);
    }

    public void setBirthday(LocalDate value) {
        birthdayProperty().set(value);
    }

    public void setCourse_code(String value) {
        course_codeProperty().set(value);
    }

    public void setCourseList(ObservableList<String> value) {
        courseListProperty().set(value);
    }

    public void setFirst_name(String value) {
        first_nameProperty().set(value);
    }

    public void setGender(String value) {
        genderProperty().set(value);
    }
    public void setGenderList(ObservableList<String> value) {
        genderListProperty().set(value);
    }



    public void setLast_name(String value) {
        last_nameProperty().set(value);
    }

    public void setStudent_number(String value) {
        student_numberProperty().set(value);
    }
}
