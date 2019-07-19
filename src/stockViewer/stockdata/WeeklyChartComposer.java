package stockViewer.stockdata;

import java.util.ArrayList;
import java.util.Calendar;

public class WeeklyChartComposer {
	
	public static ArrayList<StockData> make(ArrayList<StockData> stockDataList){
		
		ArrayList<StockData> resultList = new ArrayList<>();
		
		int collectingWeekInMonth = -1;
		StockData collectingStockData = null;
		int dataSize = stockDataList.size();
		
		for(int i=0; i<dataSize; i++) {
			
			StockData stockData = stockDataList.get(i);
			Calendar date = stockData.calendar;
			int weekInMonth = date.get(Calendar.DAY_OF_WEEK_IN_MONTH);
			
			if(collectingWeekInMonth != weekInMonth) {
				
				if(collectingStockData != null) resultList.add(collectingStockData);
				
				collectingWeekInMonth = weekInMonth;
				collectingStockData = stockData.clone();
			}
			
			collect(collectingStockData, stockData);
		}
		
		return resultList;
	}
	
	private static void collect(StockData src, StockData collected) {
		
		src.highPrice = Math.max(src.highPrice, collected.highPrice);
		src.lowPrice = Math.min(src.lowPrice, collected.lowPrice);
		src.amount += collected.amount;
		src.endPrice = collected.endPrice;
	}

}