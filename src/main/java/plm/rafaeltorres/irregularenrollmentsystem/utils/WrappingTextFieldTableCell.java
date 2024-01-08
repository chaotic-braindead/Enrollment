package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;

public class WrappingTextFieldTableCell<S> extends TextFieldTableCell<S, String> {

    private final Text cellText;
    private String regex;
    private String errorMessage;


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