package plm.rafaeltorres.irregularenrollmentsystem.utils;

import plm.rafaeltorres.irregularenrollmentsystem.MainScene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Maintenance {
    private String currentSY;
    private String currentSem;
    private final static Maintenance instance = new Maintenance();
    private final Properties properties = new Properties();
    private String path;

    private Maintenance() {
        File f = null;
        try{
            path = new File(MainScene.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
            f = new File(path + "/app.properties");
        }catch(Exception e){
            System.out.println(e);
        }
        if(!f.exists()){
            try{
                properties.load(MainScene.class.getResourceAsStream("app.properties"));
            }catch(Exception e){
                System.out.println(e);
            }

            try{
                f.createNewFile();
            }catch(Exception e){
                System.out.println(e);
            }
        }
        else{
            try{
                properties.load(new FileInputStream(path + "/app.properties"));
                if(!properties.containsKey("currentSY"))
                    properties.setProperty("currentSY", "2023-2024");

                if(!properties.containsKey("currentSem"))
                    properties.setProperty("currentSem", "1");
            }catch (Exception e){
                System.out.println(e);
            }
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
        try(FileOutputStream out = new FileOutputStream(path + "/app.properties")){
            properties.setProperty(props, newVal);
            properties.store(out, null);
        }catch(Exception e){
            System.out.println(e);
        }
        try{
            properties.load(new FileInputStream(path + "/app.properties"));
        }
        catch (Exception e){
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