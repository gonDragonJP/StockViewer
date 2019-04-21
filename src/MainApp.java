import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application{
	
	private static final int WinX = 1280;
	private static final int WinY = 640;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		
		List<CSVFileReader.StockData> list = null;

		list = new CSVFileReader().getStockDataList("2127_2019.csv");
		
		for(CSVFileReader.StockData data : list) {
			
			System.out.println(data.amount);
		}
	}
	
}
