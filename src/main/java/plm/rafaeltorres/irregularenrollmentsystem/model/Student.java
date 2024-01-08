package plm.rafaeltorres.irregularenrollmentsystem.model;

import java.sql.Blob;
import java.sql.Date;
import java.sql.ResultSet;

public class Student extends User {
    private String student_no;
    private String firstname;
    private String lastname;
    private String email;
    private String college_code;
    private String plm_email;
    private String course_code;
    private String gender;
    private Date bday;
    private String cp_num;
    private long age;
    private String address;
    private String status;
    private String registration_status;
    private Blob image;

    public Student(ResultSet rs) {
        super(rs);
    }

    public String getCollege() {
        return college_code;
    }

    public String getAddress() {
        return address;
    }

    public long getAge() {
        return age;
    }

    public String getPersonalEmail() {
        return email;
    }

    public String getPLMEmail() {
        return plm_email;
    }

    public String getCellphoneNumber() {
        return cp_num;
    }

    public String getStatus() {
        return status;
    }

    public Date getBirthday() {
        return bday;
    }

    public String getCourse() {
        return course_code;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getGender() {
        return gender;
    }

    public Blob getImage() {
        return image;
    }

    public String getLastName() {
        return lastname;
    }

    public String getStudentNo() {
        return student_no;
    }

    public String getRegistrationStatus() {
        return registration_status;
    }

    public void setImage(Blob image){
        this.image = image;
    }


}
