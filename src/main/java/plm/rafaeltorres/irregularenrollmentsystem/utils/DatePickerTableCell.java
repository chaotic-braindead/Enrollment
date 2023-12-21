package plm.rafaeltorres.irregularenrollmentsystem.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.DatePicker;

public class DatePickerTableCell<S, T> extends TableCell<S, T> {

    private DatePicker datePicker;

    public DatePickerTableCell() {

        super();

        if (datePicker == null) {
            createDatePicker();
        }
        datePicker.getEditor().setDisable(true);
        datePicker.onMouseClickedProperty(

        );
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
            }
        });
    }

    @Override
    public void updateItem(T item, boolean empty) {
        Date d = null;
        try{
            if(item != null)
                d = new SimpleDateFormat("yyyy-MM-dd").parse(item.toString());
        }catch(Exception e){
            System.out.println(e);
        }
        if(d == null)
            return;
        super.updateItem((T)d, empty);

        SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");

        if (this.datePicker == null) {
            System.out.println("datePicker is NULL");
        }

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setContentDisplay(ContentDisplay.TEXT_ONLY);

            } else {
                setDatePickerDate(smp.format((T)d));
                setText(smp.format((T)d));
                setGraphic(this.datePicker);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            }
        }
    }



    private void setDatePickerDate(String dateAsStr) {

        LocalDate ld = null;
        int jour, mois, annee;

        jour = mois = annee = 0;
        try {
            jour = Integer.parseInt(dateAsStr.substring(0, 2));
            mois = Integer.parseInt(dateAsStr.substring(3, 5));
            annee = Integer.parseInt(dateAsStr.substring(6));
        } catch (NumberFormatException e) {
            System.out.println("setDatePickerDate / unexpected error " + e);
        }

        ld = LocalDate.of(annee, mois, jour);
        datePicker.setValue(ld);
    }

    private void createDatePicker() {
        this.datePicker = new DatePicker();
        datePicker.setPromptText("jj/mm/aaaa");
        datePicker.setEditable(true);

        datePicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                LocalDate date = datePicker.getValue();

                SimpleDateFormat smp = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
                cal.set(Calendar.MONTH, date.getMonthValue() - 1);
                cal.set(Calendar.YEAR, date.getYear());

                setText(smp.format(cal.getTime()));
                commitEdit((T)(cal.getTime()).toString());
                ObservableList<String> row = (ObservableList<String>) getTableView().getItems().get(getTableRow().getIndex());

                int idx = -1;

                for(int i = 0; i < getTableView().getColumns().size(); ++i){
                    if(getTableView().getColumns().get(i).getText() == getTableColumn().getText())
                        idx = i;
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                row.set(idx, (format.format(cal.getTime()).toString()));
            }
        });

        setAlignment(Pos.CENTER);
    }

    @Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }


    public DatePicker getDatePicker() {
        return datePicker;
    }

    public void setDatePicker(DatePicker datePicker) {
        this.datePicker = datePicker;
    }

}