package plm.rafaeltorres.irregularenrollmentsystem.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class EmployeeProperty {
    private StringProperty employee_id;
    private StringProperty first_name;
    private StringProperty last_name;
    private StringProperty email;
    private ListProperty<String> genderList;
    private SimpleObjectProperty<String> gender;
    private ObjectProperty<LocalDate> birthday;
    private StringProperty cellphone_number;
    private StringProperty address;
    public StringProperty employee_idProperty(){
        if (employee_id == null) {
            SimpleStringProperty s = new SimpleStringProperty(this, "employee_id");
            s.set("");
            employee_id= s;
        }
        return employee_id;
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
    public StringProperty cellphone_numberProperty() {
        if (cellphone_number== null){
            SimpleStringProperty s = new SimpleStringProperty(this, "cellphone_number");
            s.set("");
            cellphone_number = s;
        }
        return cellphone_number;
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

    public String getEmail() {
        return emailProperty().get();
    }

    public String getCellphone_number() {
        return cellphone_numberProperty().get();
    }
    public LocalDate getBirthday() {
        return birthdayProperty().get();
    }
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

    public String getEmployee_id() {
        return employee_idProperty().get();
    }
    public void setEmployee_id(String value){
        employee_idProperty().set(value);
    }
    public void setAddress(String value) {
        addressProperty().set(value);
    }
    public void setEmail(String value) {
        emailProperty().set(value);
    }

    public void setCellphone_number(String value) {
        cellphone_numberProperty().set(value);
    }
    public void setBirthday(LocalDate value) {
        birthdayProperty().set(value);
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
}
