package plm.rafaeltorres.irregularenrollmentsystem.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Maintenance {
    private final static Maintenance instance = new Maintenance();
    private String currentSY = DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now()) + "-" + (Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now()))+1);
    private String currentSem = "1";
    public static Maintenance getInstance() {
        return instance;
    }
    public String getCurrentSY(){
        return currentSY;
    }
    public String getCurrentSem(){
        return currentSem;
    }
    public void setCurrentSY(String sy){
        currentSY = sy;
    }
    public void setCurrentSem(String sem){
        currentSem = sem;
    }
}