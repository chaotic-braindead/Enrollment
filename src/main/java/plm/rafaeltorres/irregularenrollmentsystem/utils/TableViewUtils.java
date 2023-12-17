package plm.rafaeltorres.irregularenrollmentsystem.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.sql.ResultSet;

public class TableViewUtils {
    public static void generateTableFromResultSet(TableView tbl, ResultSet rs){
        try{
            tbl.getColumns().clear();
            tbl.getItems().clear();
            for(int i = 0; i < rs.getMetaData().getColumnCount(); ++i){
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1).toUpperCase().replaceAll("_", " "));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return (param.getValue().get(j) == null) ? new SimpleStringProperty("null") : new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
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
            tbl.setItems(scheds);

            double[] cols = new double[tbl.getColumns().size()];
            for(int i = 0; i < tbl.getColumns().size(); ++i){
                TableColumn col = (TableColumn) tbl.getColumns().get(i);
                Text txtCol = new Text(col.getText());
                cols[i] = txtCol.getLayoutBounds().getWidth() + 10.0d;
            }

            for(int i = 0; i < tbl.getItems().size(); ++i) {
                ObservableList ob = (ObservableList) tbl.getItems().get(i);
                for (int j = 0; j < ob.size(); ++j) {
                    Text txtItem = new Text(ob.get(j) == null ? "null" : ob.get(j).toString());
                    cols[j] = Math.max(txtItem.getLayoutBounds().getWidth() + 10.0d, cols[j]);
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
            else if(total > tbl.getWidth()){
                for(int i = 0; i < tbl.getColumns().size(); ++i){
                    TableColumn col = (TableColumn) tbl.getColumns().get(i);
                    col.setPrefWidth(col.getPrefWidth() - (total - tbl.getWidth()) / tbl.getColumns().size());
                }
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }
}