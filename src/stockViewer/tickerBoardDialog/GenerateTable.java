package stockViewer.tickerBoardDialog;

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
	
	private enum TickerBoardColumn{
		
		TickerCode(80,"tickerCode"),
		MarketName(80,"marketName"),
		StockName(300,"stockName");
		
		int width;
		String fieldName;
		
		private TickerBoardColumn(int width, String fieldName) {
			
			this.width = width;
			this.fieldName = fieldName;
		}
	}
	
	public static TableView<TickerData> gen(TableCallback callable) {
		
		tableCallback = callable;
		
		TableView<TickerData> tableView = new TableView<>();
		
		@SuppressWarnings("unchecked")
		TableColumn<TickerData,String>[] columns = new TableColumn[TickerBoardColumn.values().length];
		
		for(TickerBoardColumn e: TickerBoardColumn.values()){
			
			columns[e.ordinal()] = new TableColumn<TickerData, String>(e.toString());
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
