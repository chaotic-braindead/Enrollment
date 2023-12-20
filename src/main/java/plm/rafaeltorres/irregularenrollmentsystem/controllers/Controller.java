package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import plm.rafaeltorres.irregularenrollmentsystem.model.Student;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    public abstract void initialize(URL location, ResourceBundle resources);

    public abstract void setUser(User arg);
    @FXML
    protected void onBtnLogoutAction(ActionEvent event) throws IOException {
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to log out?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;

        SceneSwitcher.switchScene(event, "Login.fxml");
    }
    @FXML
    protected void onBtnClick(Event event) throws IllegalAccessException{
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field f : fields){
            f.setAccessible(true);
            if(event.getSource().equals(f.get(this))){
                ToggleButton btn = (ToggleButton) f.get(this);
                btn.setSelected(true);
                continue;
            }
            if(f.getType().equals(ToggleButton.class)){
                ToggleButton btn = (ToggleButton)f.get(this);
                btn.setSelected(false);
            }
        }
    }


}
