package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

public class WrappingTextFieldTableCell<S> extends TextFieldTableCell<S, String> {

    private final Text cellText;
    private String regex;
    private String errorMessage;
    boolean isPrimaryKey;


    public WrappingTextFieldTableCell() {
        super(new DefaultStringConverter());
        this.cellText = createText();
    }

    public WrappingTextFieldTableCell(String regex, String errorMessage) {
        super(new DefaultStringConverter());
        this.cellText = createText();
        this.regex = regex;
        this.errorMessage = errorMessage;
    }
    public WrappingTextFieldTableCell(String regex, String errorMessage, boolean isPrimaryKey) {
        super(new DefaultStringConverter());
        this.cellText = createText();
        this.regex = regex;
        this.errorMessage = errorMessage;
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(cellText);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(item == null || empty){
            setGraphic(null);
            return;
        }
        if (!isEditing()){
            setGraphic(cellText);
        }
    }
    @Override
    public void commitEdit(String newValue) {
        if(regex != null && !newValue.matches(regex)){
            AlertMessage.showErrorAlert(errorMessage);
            return;
        }
        if(isPrimaryKey && !this.cellText.getText().equals(newValue)){
            Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert(
                    "Warning: Editing this value will cascade to all other records in the database " +
                            "which uses this value. Do you wish to proceed?");
            if(confirm.isEmpty() || confirm.get() == ButtonType.NO) {
                AlertMessage.showInformationAlert("Cancelled edit.");
                return;
            }

            try{
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_NO = ? WHERE ACCOUNT_NO = ?");
                ps.setString(1, newValue);
                ps.setString(2, this.cellText.getText());
                ps.executeUpdate();
            }catch(Exception e){
                AlertMessage.showErrorAlert("An error occurred while updating value.");
            }
        }
        super.commitEdit(newValue);
    }

    private Text createText() {
        Text text = new Text();
        text.wrappingWidthProperty().bind(widthProperty().subtract(10));
        text.setStyle("-fx-alignment: center");
        text.setStyle("-fx-text-fill: black");
        text.textProperty().bind(itemProperty());
        return text;
    }
}