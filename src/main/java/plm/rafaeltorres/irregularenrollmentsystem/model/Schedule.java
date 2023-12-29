package plm.rafaeltorres.irregularenrollmentsystem.model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

public class Schedule {
    private String subject_code;
    private String course;
    private String block;
    private String year;
    private String description;
    private String schedule;
    private String credits;
    private String professor;
    public Schedule(ResultSet rs){
        Field[] fields = this.getClass().getDeclaredFields();
        try{
            for(Field f : fields){
                f.setAccessible(true);
                f.set(this, rs.getObject(f.getName()));
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    public Schedule(Object o){
        Field[] fields = this.getClass().getDeclaredFields();
        List<String> l = (List<String>) o;
        try{
            for(int i = 0; i < fields.length; ++i){
                Field f = fields[i];
                f.setAccessible(true);
                f.set(this, l.get(i));
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }


    public String getSubjectCode() {
        return subject_code;
    }


    public String getBlock() {
        return course+block+year;
    }

    public String getCredits() {
        return credits;
    }

    public String getDescription() {
        return description;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getProfessor() {
        return professor;
    }

    public String getSubject_code() {
        return subject_code;
    }
}
