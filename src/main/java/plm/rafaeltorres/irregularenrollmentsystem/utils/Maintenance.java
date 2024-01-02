package plm.rafaeltorres.irregularenrollmentsystem.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Maintenance {
    private String currentSY;
    private String currentSem;
    private LocalDate startSem1 = LocalDate.of(LocalDate.now().getYear(), Month.AUGUST, 15);
    private LocalDate startSem2 = LocalDate.of(LocalDate.now().getYear(), Month.FEBRUARY, 1);
    private LocalDate startSemS = LocalDate.of(LocalDate.now().getYear(), Month.JUNE, 15);
    private final static Maintenance instance = new Maintenance();
    private Maintenance(){
       refresh();
    }
    public void refresh(){
        LocalDate now = LocalDate.now();
        if(now.isBefore(startSem2))
            currentSem = "1";
        else if(now.isAfter(startSem1) && now.isBefore(startSemS))
            currentSem = "2";
        else if(now.isAfter(startSem2))
            currentSem = "S";

        if((currentSem.equals("1") && LocalDate.now().isBefore(startSem1)) || currentSem.equals("2") || currentSem.equals("S"))
            currentSY = (Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now()))-1)+"-"+DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now());
        else
            currentSY = DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now())+"-"+(Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now()))+1);
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
    public void setCurrentSY(String sy){
        currentSY = sy;
    }
    public void setCurrentSem(String sem){
        currentSem = sem;
    }
    public void setStartSem1(LocalDate date){
        startSem1 = date;
    }
    public void setStartSem2(LocalDate date){
        startSem2 = date;
    }
    public void setStartSemS(LocalDate date){
        startSemS = date;
    }
}