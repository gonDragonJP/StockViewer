package stockViewer;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application{
	
	private static final int WinX = 1280;
	private static final int WinY = 720;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		
		initStage(stage);
		
		List<StockData> list = null;

	
		
		list = new CSVFileReader().getStockDataList("2127_2017.csv");
		
		for(StockData data : list) {
			
			System.out.println(data.lowPrice);
		}
		
		DrawModule drawModule = new DrawModule(this);
		drawModule.clearScreen();
		drawModule.setValueRange(0, 8000);
		drawModule.setPeriodRange(0, 300);
		drawModule.drawVirticalGrid(2000);
		
		int period = 0;
		
		for(StockData data : list) {
			
			drawModule.drawPriceBar(data, period);
			period++;
		}
	}
	
	private void initStage(Stage stage){
		
		MainSceneUtil.setScene(this, stage);
	
		stage.setTitle("StockViewer ver1.0");
		stage.setWidth(WinX);
		stage.setHeight(WinY);
		stage.show();
	}
	
}