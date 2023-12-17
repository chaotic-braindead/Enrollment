package plm.rafaeltorres.irregularenrollmentsystem.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    public abstract void initialize(URL location, ResourceBundle resources);

    // empty since SceneSwitcher (line 20) gives an error when a Controller instance does not have this method
    public void setStudentNo(String arg){ }

}
