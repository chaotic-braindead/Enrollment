package plm.rafaeltorres.irregularenrollmentsystem.model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

public class Schedule {
    private String subject_code;
    private String description;
    private String section;
    private String time_slot;
    private String room;
    private String units;
    private String total_students;
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

    public String getRoom() {
        return room;
    }

    public String getSection() {
        return section;
    }

    public String getSubjectCode() {
        return subject_code;
    }

    public String getTimeSlot() {
        return time_slot;
    }

    public String getTotalStudents() {
        return total_students;
    }
    public String getDescription(){ return description; }
    public String getUnits(){ return units; }

    @Override
    public String toString(){
        return String.format("[Schedule subject_code=%s, section=%s, time_slot=%s, room=%s, total_students=%s]", subject_code, section, time_slot, room, total_students);
    }
}
