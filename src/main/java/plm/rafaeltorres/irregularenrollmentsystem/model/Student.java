package plm.rafaeltorres.irregularenrollmentsystem.model;

import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;

public class Student extends User {
    private String student_number;
    private String first_name;
    private String last_name;
    private String personal_email;
    private String college;
    private String plm_email;
    private String course;
    private String gender;
    private Date birthday;
    private String cellphone_number;
    private long age;
    private String address;
    private String status;
    private String registration_status;
    private Blob image;

    public Student(ResultSet rs) {
        super(rs);
    }

    public String getCollege() {
        return college;
    }

    public String getAddress() {
        return address;
    }

    public long getAge() {
        return age;
    }

    public String getPersonalEmail() {
        return personal_email;
    }

    public String getPLMEmail() {
        return plm_email;
    }

    public String getCellphoneNumber() {
        return cellphone_number;
    }

    public String getStatus() {
        return status;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getCourse() {
        return course;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getGender() {
        return gender;
    }

    public Blob getImage() {
        return image;
    }

    public String getLastName() {
        return last_name;
    }

    public String getStudentNo() {
        return student_number;
    }

    public String getRegistrationStatus() {
        return registration_status;
    }

    public void setImage(Blob image){
        this.image = image;
    }


}
