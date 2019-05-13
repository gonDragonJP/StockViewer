package stockViewer.trade;

import java.lang.reflect.Field;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.TickerData;

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
		
		tableView.setPrefHeight(200);
		
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
