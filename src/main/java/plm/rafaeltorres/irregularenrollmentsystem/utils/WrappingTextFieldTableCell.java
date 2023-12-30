package plm.rafaeltorres.irregularenrollmentsystem.utils;

import javafx.event.EventHandler;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
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
        text.textProperty().bind(itemProperty());
        text.setOnKeyTyped(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event)
            {
                // get the typed character
                String characterString = event.getCharacter();
                System.out.println("here");
                char c = characterString.charAt(0);
                // if it is a control character or it is undefined, ignore it
                if (Character.isISOControl(c) || characterString.contentEquals(KeyEvent.CHAR_UNDEFINED))
                    return;

                // get the text field/area that triggered this key event and its text
                TextInputControl source = (TextInputControl) event.getSource();
                String text = source.getText();

                // If the text exceeds its max length or if a character that matches
                // notAllowedCharactersRegEx is typed
                if (characterString.matches(regex))
                {
                    // remove the last character
                    source.deletePreviousChar();
                    System.out.println("here");
                }
            }
        });

        return text;
    }
}