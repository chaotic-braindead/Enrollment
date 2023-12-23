package plm.rafaeltorres.irregularenrollmentsystem.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import plm.rafaeltorres.irregularenrollmentsystem.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TableViewUtils {
    public static void generateEditableTableFromResultSet(TableView tbl, ResultSet rs, String[] args){
        try{
            tbl.getColumns().clear();
            tbl.getItems().clear();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i){
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("-") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
                    @Override public void handle(TableColumn.CellEditEvent t) {
                        ObservableList<String> o = (ObservableList<String>) t.getRowValue();
                        System.out.println(o.get(j));
                        o.set(j, t.getNewValue().toString());
                        System.out.println(o);
                        try{
                            Connection conn = Database.connect();
                            PreparedStatement ps = conn.prepareStatement("UPDATE " + args[0] + " SET " + rs.getMetaData().getColumnName(j+1) + " = ? "+ "WHERE "+ args[1] +" = ?");
                            ps.setString(1, o.get(j));
                            ps.setString(2, o.get(0));
                            ps.executeUpdate();
                            System.out.println(o.get(j) + " from column " + rs.getMetaData().getColumnName(j+1));
                        }catch(Exception e){
                            System.out.println(e);
                        }
//                        try{
//                            Connection conn = Database.connect();
//                            PreparedStatement ps = conn.prepareStatement("UPDATE " + table + " SET " + rs.getMetaData().getColumnName(j+1) + " = ? WHERE " + rs.getMetaData().getColumnName(j+1) + " = ? AND ");
//                            ps.setString();
//                        }catch(Exception e){
//                            System.out.println(e);
//                        }
//                        ((StringProperty)(ob.get(j))).setValue((String)t.getNewValue());
                    }
                });
                String txt = col.getText();
                switch(txt){
                    case "AGE":
                    case "PLM EMAIL":
                    case "REGISTRATION STATUS":
                    case "STUDENT NUMBER":
                    case "STUDENT_NO":
                        break;
                    case "GENDER":
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("Male", "Female"));
                                    }
                                }
                        );
                        break;
                    case "BIRTHDAY":
                    case "BDAY":
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
                                        return new ComboBoxTableCell(new DefaultStringConverter(), FXCollections.observableArrayList("Active", "Inactive"));
                                    }
                                }
                        );
                        break;
                    case "COLLEGE_CODE":
                    case "COLLEGE":
                        ObservableList<String> comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COLLEGE_CODE FROM COLLEGE"));
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                    }
                                }
                        );
                        break;
                    case "COURSE_CODE":
                    case "COURSE":
                        comboBoxItems = FXCollections.observableArrayList(Database.fetch("SELECT COURSE_CODE FROM COURSE"));
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new ComboBoxTableCell(new DefaultStringConverter(), comboBoxItems);
                                    }
                                }
                        );
                        break;
                    default:
                        col.setCellFactory(
                                new Callback<TableColumn, TableCell>() {
                                    public TableCell call(TableColumn p) {
                                        return new TextFieldTableCell(new DefaultStringConverter());

                                    }
                                }
                        );
                }
                tbl.getColumns().addAll(col);
            }
            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
//                row.addListener((ListChangeListener.Change<? extends String> c) -> {
//                    while (c.next()) {
//                        if (c.wasAdded()) {
//                            System.out.println("Added:");
//                            c.getAddedSubList().forEach(System.out::println);
//                            System.out.println();
//                        }
//                        if (c.wasRemoved()) {
//                            System.out.println("Removed:");
//                            c.getRemoved().forEach(System.out::println);
//                            System.out.println();
//                        }
//                        if (c.wasUpdated()) {
//                            System.out.println("Updated:");
//                            row.subList(c.getFrom(), c.getTo()).forEach(System.out::println);
//                            System.out.println();
//                        }
//                    }
//                });
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.add(rs.getString(i));
                }
                scheds.add(row);
            }
            tbl.setItems(scheds);

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

                tbl.getColumns().addAll(col);
            }
            ObservableList<ObservableList<String>> scheds = FXCollections.observableArrayList();
            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
//
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); ++i) {
                    row.add(rs.getString(i));
                }
                scheds.add(row);
            }
            tbl.setItems(scheds);

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
        }catch(Exception e){
            System.out.println(e);
        }
    }
}