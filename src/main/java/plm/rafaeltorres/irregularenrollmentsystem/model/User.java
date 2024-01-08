package plm.rafaeltorres.irregularenrollmentsystem.model;

import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;

// only used for checking types in sceneswitcher
public abstract class User {
    public User(ResultSet rs){
        Field[] fields = this.getClass().getDeclaredFields();
        Connection conn = Database.getInstance().getConnection();
        try{
            for(Field f : fields){
                f.setAccessible(true);
                Object o = rs.getObject(f.getName());
                if(o == null) continue;
                if(o.getClass().equals(byte[].class)){
                    Blob blob = conn.createBlob();
                    blob.setBytes(1, (byte[])o);
                    f.set(this, blob);
                    conn.close();
                }
                else{
                    f.set(this, o);
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }


    @Override
    public String toString(){
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder(this.getClass().getName()+"[");
        try{
            for(Field f : fields){
                f.setAccessible(true);
                sb.append(f.getName()).append("=").append(f.get(this)).append(" ");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        sb = new StringBuilder(sb.toString().trim());
        sb.append("]");
        return sb.toString();
    }
}
