package plm.rafaeltorres.irregularenrollmentsystem.utils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class Maintenance {
    private String currentSY;
    private String currentSem;
    private LocalDate startSem1 = LocalDate.of(LocalDate.now().getYear(), Month.AUGUST, 15);
    private LocalDate endSem1 = LocalDate.of(LocalDate.now().getYear()+1, Month.JANUARY, 31);
    private LocalDate startSem2 = LocalDate.of(LocalDate.now().getYear()+1, Month.FEBRUARY, 1);
    private LocalDate endSem2 = LocalDate.of(LocalDate.now().getYear()+1, Month.JUNE, 15);
    private LocalDate startSemS = LocalDate.of(LocalDate.now().getYear()+1, Month.JUNE, 15);
    private LocalDate endSemS = LocalDate.of(LocalDate.now().getYear()+1, Month.JULY, 31);
    private final static Maintenance instance = new Maintenance();
    private Maintenance(){
        LocalDate now = LocalDate.now();
        if(now.isAfter(startSem1) && now.isBefore(endSem1))
            currentSem = "1";
        else if(now.isBefore(startSem2) && now.isAfter(endSem2))
            currentSem = "2";
        else if(now.isAfter(startSemS) && now.isBefore(endSemS))
            currentSem = "S";
        if(currentSem.equals("2") || currentSem.equals("S"))
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
    public void setEndSem1(LocalDate date){
        endSem1 = date;
    }
    public void setStartSem2(LocalDate date){
        startSem2 = date;
    }
    public void setEndSem2(LocalDate date){
        endSem2 = date;
    }
    public void setStartSemS(LocalDate date){
        startSemS = date;
    }
    public void setEndSemS(LocalDate date){
        endSemS = date;
    }
}