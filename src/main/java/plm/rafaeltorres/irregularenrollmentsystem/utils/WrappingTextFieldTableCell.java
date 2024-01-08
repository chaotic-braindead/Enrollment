package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;

public class WrappingTextFieldTableCell<S> extends TextFieldTableCell<S, String> {

    private final Text cellText;
    private String regex;

    public WrappingTextFieldTableCell() {
        super(new DefaultStringConverter());
        this.cellText = createText();
        regex = "^[0-9]*$";
    }
    public WrappingTextFieldTableCell(String regex) {
        super(new DefaultStringConverter());
        this.cellText = createText();
        this.regex = regex;
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(cellText);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty() && !isEditing()) {
            setGraphic(cellText);
        }
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