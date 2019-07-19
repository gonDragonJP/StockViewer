package stockViewer.trade;

import java.lang.reflect.Field;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.TickerData;
import stockViewer.trade.GenerateTable.ResultTableField;

public class GenerateTable {
	
	public interface TableCallback{
		
		void tableClicked(MouseEvent event);
	}

	private static TableCallback tableCallback;
	
	private enum TradeTableColumn{
		
		Code(40,"tickerCode"),
		Date(80,"tradingDate"),
		Pos(40,"buyOrSell"),
		Price(50,"price"),
		Unit(50,"unit"),
		Sum_U(50,"sumUnit"),
		Acount(80,"acount"),
		Profit(50,"profitText");
		
		int width;
		String fieldName;
		
		private TradeTableColumn(int width, String fieldName) {
			
			this.width = width;
			this.fieldName = fieldName;
		}
	}
	
	public enum ResultTableField{
		
		Win_Rate("winRate","%"),
		Win_Number("winNumber",""),
		lose_Number("loseNumber",""),
		
		Ave_Profit("aveProfit",""),
		Ave_Loss("aveLoss",""),
		ProfitFactor("profitFactor",""),
		
		Max_RowWin("maxRowWin",""),
		Max_RowLose("maxRowLose",""),
		
		Total_Profit("totalProfit",""),
		Max_DrawDown("maxDrawDown","");
		
		public String fieldName;
		public String unit;
		
		ResultTableField(String fieldName, String unit){
			this.fieldName = fieldName;
			this.unit = unit;
		}
	}
	
	public static TableView<TradeData> gen(TableCallback callable) {
		
		tableCallback = callable;
		
		TableView<TradeData> tableView = new TableView<>();
		
		@SuppressWarnings("unchecked")
		TableColumn<TradeData,String>[] columns = new TableColumn[TradeTableColumn.values().length];
		
		for(TradeTableColumn e: TradeTableColumn.values()){
			
			columns[e.ordinal()] = new TableColumn<TradeData, String>(e.toString());
			columns[e.ordinal()].setPrefWidth(e.width);
		
			columns[e.ordinal()].setCellValueFactory(
					param -> {
						String colString = 
								getReflectedFieldAsString(param.getValue(),e.fieldName);
						return new SimpleStringProperty(colString);
					}
			);
		}
		
		tableView.getColumns().addAll(columns);
		tableView.setOnMouseClicked(event -> tableCallback.tableClicked(event));
		
		tableView.setPrefHeight(TradeDialog.TABLE_HEIGHT);
		
		return tableView;
	}
	
	public static TableView<ResultTableField> gen(ResultData resultData) {
		
		TableView<ResultTableField> tableView = new TableView<>();
		
		@SuppressWarnings("unchecked")
		TableColumn<ResultTableField,String>[] columns = new TableColumn[2];
		
		columns[0] = new TableColumn<>("Result Term");
		columns[0].setPrefWidth(200);
		columns[0].setCellValueFactory(param -> new SimpleStringProperty(param.getValue().toString()));
		
		columns[1] = new TableColumn<>("");
		columns[1].setPrefWidth(200);
		columns[1].setCellValueFactory(param ->{
			
				String valText = getReflectedFieldAsString(resultData, param.getValue().fieldName);
				valText += param.getValue().unit;
				
				return new SimpleStringProperty(valText);
			});
		columns[1].setCellFactory(TextFieldTableCell.forTableColumn());
		
		tableView.getColumns().addAll(columns);
		tableView.setPrefHeight(TradeDialog.RESULT_TABLE_HEIGHT);
		
		ObservableList<ResultTableField> resultTableData = FXCollections.observableArrayList();
		resultTableData.setAll(ResultTableField.values());
		tableView.itemsProperty().setValue(resultTableData);
		
		return tableView;
	}
	
	private static String getReflectedFieldAsString(Object object, String fieldName){
		
		Class<?> clazz = object.getClass();
		
		try{
		
			Field field = clazz.getDeclaredField(fieldName);
			
			return field.get(object).toString();
		
		}catch(ReflectiveOperationException e){
		
		}
		return "null";
	}
}
