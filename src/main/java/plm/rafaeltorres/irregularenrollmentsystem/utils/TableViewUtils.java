package plm.rafaeltorres.irregularenrollmentsystem.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TableViewUtils {
    public static void generateEditableTableFromResultSet(TableView tbl, ResultSet rs, String[] args, Runnable callback){
        TableColumn col = null;
        try{
            tbl.getColumns().clear();
            tbl.getItems().clear();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i){
                final int j = i;
                col = new TableColumn(rs.getMetaData().getColumnName(i+1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                    @Override public void handle(TableColumn.CellEditEvent t) {
                        ObservableList<String> o = (ObservableList<String>) t.getRowValue();
                        Connection conn = Database.connect();
                        PreparedStatement ps = null;
                        try{
                            if(rs.getMetaData().getColumnName(j+1).equalsIgnoreCase("COURSE_CODE") && args[0].equalsIgnoreCase("STUDENT") && o.get(o.size()-1).equalsIgnoreCase("Regular")){
                                if(t.getOldValue().equals(t.getNewValue()))
                                    return;
                                Optional<ButtonType> confirm = AlertMessage.showConfirmationAlert("Warning: Changing the course of the student will result in he/she being irregular. This action is irreversible. Do you wish to continue?");
                                if(confirm.isEmpty() || (confirm.get().equals(ButtonType.NO))){
                                    AlertMessage.showInformationAlert("Cancelled transaction.");
                                    callback.run();
                                    return;
                                }
                                try{
                                    ps = conn.prepareStatement("replace into grade values (?, ?, ?, '00000', '', 5.00)");
                                    ps.setString(1, Maintenance.getInstance().getCurrentSY());
                                    ps.setString(2, Maintenance.getInstance().getCurrentSem());
                                    ps.setString(3, o.get(0));
                                    ps.executeUpdate();
                                }catch(Exception e){
                                    AlertMessage.showErrorAlert("An error occurred while shifting courses: " + e);
                                }
                            }

                            if(rs.getMetaData().getColumnName(j+1).equalsIgnoreCase("STATUS") && !args[0].equalsIgnoreCase("STUDENT") && !args[0].equalsIgnoreCase("SUBJECT")){
                                String query = "UPDATE " + args[0] + " SET DATE_CLOSED = '" + DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()) + "' WHERE " + args[1] + " = ?";
                                if(t.getNewValue().toString().equalsIgnoreCase("A"))
                                    query = "UPDATE " + args[0] + " SET DATE_CLOSED = '9999-12-31' WHERE " + args[1] + " = ?";
                                ps = conn.prepareStatement(query);
                                ps.setString(1, o.get(0));
                                ps.executeUpdate();
                            }

                            ps = conn.prepareStatement("UPDATE " + args[0] + " SET " + rs.getMetaData().getColumnName(j+1) + " = ? "+ "WHERE "+ args[1] +" = ?");
                            ps.setString(1, t.getNewValue().toString());
                            ps.setString(2, o.get(0));
                            ps.executeUpdate();
                            callback.run();
                        }catch(Exception e){
                            System.out.println(e);
                        }

                    }
                });
                String txt = col.getText().replaceAll("_", " ").toUpperCase();
                switch(txt){
                    case "AGE":
                    case "PLM EMAIL":
                    case "REGISTRATION STATUS":
                    case "STUDENT NUMBER":
                    case "DATE CLOSED":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new TableCell<ObservableList<String>, String>() {
                                            @Override
                                            protected void updateItem(String item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (item == null || empty) {
                                                    setText(null);
                                                    setGraphic(null);
                                                } else {
                                                    Text text = new Text(item);
                                                    text.setStyle("-fx-text-alignment:left;");
                                                    text.setStyle("-fx-text-fill: black");
                                                    text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(10));
                                                    setGraphic(text);
                                                }
                                            }
                                        };
                                    }
                                }
                        );
                        break;
                    case "EMPLOYEE ID":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell<ObservableList<String>>("^E[0-9]{3}", "Enter a valid employee id format. Ex: E001", true);
                                    }
                                }
                        );
                        break;
                    case "STUDENT NO":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell<ObservableList<String>>("^[0-9]{4}-[0-9]{5}", "Enter a valid student number format. Ex: 2022-34000", true);
                                    }
                                }
                        );
                        break;
                    case "LASTNAME":
                    case "FIRSTNAME":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell<ObservableList<String>>("^[A-Za-z ,.'-]{1,100}", "Must have a length between 1 to 100 and a valid name format ex: Dela Cruz ");
                                    }
                                }
                        );
                        break;
                    case "EMAIL":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell<ObservableList<String>>("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", "Must be a valid email.");
                                    }
                                }
                        );
                        break;
                    case "GENDER":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("M", "F"));
                                    }
                                }
                        );
                        break;
                    case "CP NUM":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell("^(09|\\+639)\\d{9}$", "Must be a valid Philippine phone number ex: 09123456789.");
                                    }
                                }
                        );
                        break;
                    case "BIRTHDAY":
                    case "BDAY":
                    case "DATE OPENED":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new DatePickerTableCell();
                                    }
                                }
                        );
                        break;
                    case "STATUS":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        ComboBoxTableCell comboBoxTableCell = new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("A", "I"));
                                        comboBoxTableCell.setStyle("-fx-text-fill: black");
                                        return comboBoxTableCell;
                                    }
                                }
                        );
                        break;
                    case "DESCRIPTION":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell("^.{1,100}$", "Length must be between 1 and 100");
                                    }
                                }
                        );
                        break;
                    case "COLLEGE CODE":
                        if(args[0].equalsIgnoreCase("COLLEGE")){
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            return new WrappingTextFieldTableCell("^.{1,20}$", "Length must be between 1 and 20", true);
                                        }
                                    }
                            );
                        }
                        else if(!args[0].equalsIgnoreCase("STUDENT")){
                            ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COLLEGE_CODE FROM COLLEGE"));
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            ComboBoxTableCell comboBoxTableCell = new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                            comboBoxTableCell.setStyle("-fx-text-fill: black");
                                            return comboBoxTableCell;
                                        }
                                    }
                            );
                        }
                        break;
                    case "SUBJECT CODE":
                        if(args[0].equalsIgnoreCase("SUBJECT")){
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            return new WrappingTextFieldTableCell("^.{1,20}$", "Length must be between 1 and 20", true);
                                        }
                                    }
                            );
                        }
                        else{
                            ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT SUBJECT_CODE FROM SUBJECT"));
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            ComboBoxTableCell comboBoxTableCell = new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                            comboBoxTableCell.setStyle("-fx-text-fill: black");
                                            return comboBoxTableCell;
                                        }
                                    }
                            );
                        }
                        break;
                    case "UNITS":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell("^[1-9]{1}", "Must be numeric and greater than 0 but less than 10");
                                    }
                                }
                        );
                        break;
                    case "COURSE CODE":
                        if(args[0].equalsIgnoreCase("COURSE")){
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            return new WrappingTextFieldTableCell("^.{1,20}$", "Length must be between 1 and 20", true);
                                        }
                                    }
                            );
                            break;
                        }
                        if(args[0].equalsIgnoreCase("STUDENT")){
                            ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COURSE_CODE FROM COURSE"));
                            col.setCellFactory(
                                    new Callback<TableColumn, TableCell>() {
                                        public TableCell call(TableColumn p) {
                                            ComboBoxTableCell comboBoxTableCell = new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                            comboBoxTableCell.setStyle("-fx-text-fill: black");
                                            return comboBoxTableCell;
                                        }
                                    }
                            );
                            break;
                        }
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new TextFieldTableCell(new DefaultStringConverter());
                                    }
                                }
                        );
                        break;
                    default:
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new WrappingTextFieldTableCell<ObservableList<String>>("^.{1,100}$", "Must only contain 1 to 100 characters");
                                    }
                                }
                        );
                }
                tbl.getColumns().addAll(col);
            }
            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.add(rs.getString(i));
                }
                scheds.add(row);
            }

            tbl.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
                @Override
                public Boolean call(TableView.ResizeFeatures p) {
                    return true;
                }
            });
            tbl.setRowFactory(tblView -> {
                final TableRow<ObservableList<String>> r = new TableRow<>();
                r.hoverProperty().addListener((observable) -> {
                    final ObservableList<String> current = r.getItem();
                    if (r.isHover() && current != null) {
                        r.setStyle("-fx-background-color: #dbdbdb");
                    } else {
                        r.setStyle("");
                    }
                });
                return r;
            });
            tbl.setItems(scheds);
            resizeTable(tbl);
            tbl.refresh();

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void generateTableFromResultSet(TableView tbl, ResultSet rs){
        try{
            tbl.getColumns().clear();
            tbl.getItems().clear();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i){
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1).toUpperCase().replaceAll("_", " "));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                col.setCellFactory(
                        new Callback<TableColumn, TableCell>() {
                            public TableCell call(TableColumn p) {
                                return new TableCell<ObservableList<String>, String>() {
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (item == null || empty) {
                                            setText(null);
                                            setStyle("");
                                        } else {
                                            Text text = new Text(item);
                                            text.setStyle("-fx-text-alignment:left;");
                                            text.wrappingWidthProperty().bind(getTableColumn().widthProperty().subtract(35));
                                            setGraphic(text);
                                        }
                                    }
                                };
                            }
                        }
                );
                tbl.getColumns().addAll(col);
            }
            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.add(rs.getString(i));
                }
                scheds.add(row);
            }


            tbl.setRowFactory(tblView -> {
                final TableRow<ObservableList<String>> r = new TableRow<>();
                r.hoverProperty().addListener((observable) -> {
                    final ObservableList<String> current = r.getItem();

                    if (r.isHover() && current != null) {
                        r.setStyle("-fx-background-color: #dbdbdb");
                    } else {
                        r.setStyle("");
                    }
                });
                return r;
            });
            tbl.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
                @Override
                public Boolean call(TableView.ResizeFeatures p) {
                    return true;
                }
            });
            tbl.setItems(scheds);
            resizeTable(tbl);
            tbl.refresh();


        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void resizeTable(TableView tbl){
        double[] cols = new double[tbl.getColumns().size()];
        for(int i = 0; i < tbl.getColumns().size(); ++i){
            TableColumn col = (TableColumn) tbl.getColumns().get(i);
            cols[i] = col.getWidth();
        }

        for(int i = 0; i < tbl.getItems().size(); ++i) {
            ObservableList ob = (ObservableList) tbl.getItems().get(i);
            for (int j = 0; j < ob.size(); ++j) {
                Label txtItem = new Label(ob.get(j) == null ? "-" : ob.get(j).toString());
                cols[j] = Math.max(txtItem.getLayoutBounds().getWidth(), cols[j]);
            }
        }

        double total = 0;
        for(int i = 0; i < tbl.getColumns().size(); ++i){
            TableColumn col = (TableColumn) tbl.getColumns().get(i);
            col.setPrefWidth(cols[i]);
            total += cols[i];
        }
        if(total < tbl.getWidth()){
            for(int i = 0; i < tbl.getColumns().size(); ++i){
                TableColumn col = (TableColumn) tbl.getColumns().get(i);
                col.setPrefWidth(col.getPrefWidth() + (tbl.getWidth() - total) / tbl.getColumns().size());
            }
        }

        tbl.setRowFactory(tblView -> {
            final TableRow<ObservableList<String>> r = new TableRow<>();
            r.hoverProperty().addListener((observable) -> {
                final ObservableList<String> current = r.getItem();
                if (r.isHover() && current != null) {
                    r.setStyle("-fx-background-color: #dbdbdb");
                } else {
                    r.setStyle("");
                }
            });
            return r;
        });
        tbl.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures p) {
                return true;
            }
        });
    }





    public static void generateTable(TableView tbl, ResultSet rs){
        try {
            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.add(rs.getString(i));
                }
                scheds.add(row);
            }
            tbl.setItems(scheds);

            tbl.setRowFactory(tblView -> {
                final TableRow<ObservableList<String>> r = new TableRow<>();
                r.hoverProperty().addListener((observable) -> {
                    final ObservableList<String> current = r.getItem();

                    if (r.isHover() && current != null)
                        r.setStyle("-fx-background-color: #dbdbdb");
                    else
                        r.setStyle("");

                });
                return r;
            });

            resizeTable(tbl);
            tbl.refresh();

        }catch (Exception e){
            System.out.println(e);
        }
    }
}