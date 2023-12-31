package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;
import plm.rafaeltorres.irregularenrollmentsystem.model.User;
import plm.rafaeltorres.irregularenrollmentsystem.utils.AlertMessage;
import plm.rafaeltorres.irregularenrollmentsystem.utils.SceneSwitcher;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected Connection conn = Database.connect();
    public abstract void initialize(URL location, ResourceBundle resources);

    public abstract void setUser(User arg);
    @FXML
    protected void onBtnLogoutAction(ActionEvent event) throws IOException {
        Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Are you sure you want to log out?");
        if(confirm.isEmpty() || confirm.get() == ButtonType.NO)
            return;

        SceneSwitcher.switchScene(event, "Login.fxml");
    }
}
