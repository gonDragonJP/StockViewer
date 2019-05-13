package stockViewer.database;

import java.io.File;
import java.util.List;

import stockViewer.stockdata.StockData;
import stockViewer.stockdata.ChartData;

public class CSVFileChecker {	
	
	private int readTickerCode;
	private String readMarketName, readStockName;
	
	public void checkFolder(String folderPath) {
		
		File file = new File(folderPath);
		
		if(file == null) return;
		
		File[] files = file.listFiles();
		for(File e: files){
			
			checkFile(e);
		}
	}
	
	private void checkFile(File file) {

		ChartData chartData = new CSVFileReader().getChartData(file.getPath());
		
		DBAccessOfTickerDataTable da1 = new DBAccessOfTickerDataTable();
		if(da1.getTickerData(chartData.tickerData.tickerCode) == null) da1.addTickerData(chartData.tickerData);
		
		DBAccessOfStockDataTable da2 = new DBAccessOfStockDataTable(chartData.tickerData.tickerCode);
		for(StockData data : chartData.stockDataList) {
			
			if(!da2.checkExistStockData(data)) da2.addStockData(data);
		}
		
	}
}
