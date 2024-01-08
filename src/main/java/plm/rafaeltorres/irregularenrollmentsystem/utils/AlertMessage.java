package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import plm.rafaeltorres.irregularenrollmentsystem.MainScene;

import java.util.Optional;

public class AlertMessage {
    public static void addIcon(Dialog alert){
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(MainScene.class.getResourceAsStream("assets/img/PLM_Seal_2013.png")));
    }
    public static void showInformationAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, ButtonType.OK);
        addIcon(alert);
        alert.show();
    }
    public static void showErrorAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.ERROR, contextText, ButtonType.OK);
        addIcon(alert);
        alert.show();
    }
    public static Optional<ButtonType> showConfirmationAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, ButtonType.YES, ButtonType.NO);
        addIcon(alert);
        return alert.showAndWait();
    }
}
