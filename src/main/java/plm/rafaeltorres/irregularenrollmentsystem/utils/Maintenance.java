package plm.rafaeltorres.irregularenrollmentsystem.utils;

import plm.rafaeltorres.irregularenrollmentsystem.MainScene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Maintenance {
    private String currentSY;
    private String currentSem;
    private final static Maintenance instance = new Maintenance();
    private final Properties properties = new Properties();
    private final String path = MainScene.class.getResource("app.properties").getPath();

    private Maintenance() {
        try{
            properties.load(new FileInputStream(path));
        }catch(Exception e){
            System.out.println(e);
        }
        currentSY = properties.getProperty("currentSY");
        currentSem = properties.getProperty("currentSem");
    }

    public static Maintenance getInstance() {
        return instance;
    }
    public String getCurrentSY(){
        return currentSY;
    }
    public String getCurrentSem(){
        return currentSem;
    }
    private void set(String props, String newVal){
        try(FileOutputStream out = new FileOutputStream(path)){
            properties.setProperty(props, newVal);
            properties.store(out, null);
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void setCurrentSY(String sy){
        set("currentSY", sy);
        currentSY = sy;
    }
    public void setCurrentSem(String sem){
        set("currentSem", sem);
        currentSem = sem;
    }
}