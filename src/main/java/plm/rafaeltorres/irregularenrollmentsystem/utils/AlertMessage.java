package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertMessage {
    public static void showInformationAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, contextText, ButtonType.OK);
        alert.show();
    }
    public static void showErrorAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.ERROR, contextText, ButtonType.OK);
        alert.show();
    }
    public static Optional<ButtonType> showConfirmationAlert(String contextText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contextText, ButtonType.YES, ButtonType.NO);
        return alert.showAndWait();
    }
}
