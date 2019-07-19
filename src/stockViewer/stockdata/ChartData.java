package stockViewer.stockdata;

import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.Global;
import stockViewer.database.DBAccessOfStockDataTable;
import stockViewer.database.DBAccessOfTickerDataTable;

public class ChartData {
	
	public TickerData tickerData;
	
	public ArrayList<StockData> stockDataList;
	
	public ChartData() {
		
		tickerData = new TickerData();
		stockDataList = new ArrayList<StockData>();
	}
	
	public void loadDB(int tickerCode) {
		
		DBAccessOfTickerDataTable da1 = new DBAccessOfTickerDataTable();
		tickerData = da1.getTickerData(tickerCode);
		DBAccessOfStockDataTable da2 = new DBAccessOfStockDataTable(tickerCode);
		da2.setStockDataList(stockDataList);
		
		stockDataList = WeeklyChartComposer.make(stockDataList);
		setSMA();
	}
	
	public Calendar getDate(int index) {
		
		if (index >= stockDataList.size()) return null;
		
		return stockDataList.get(index).calendar;
	}
	
	public int getIndexByDate(Calendar date) {
		
		for(int i=0; i<stockDataList.size(); i++) {
			if(stockDataList.get(i).calendar.compareTo(date) ==0) return i; 
		}
		return -1;
	}
	
	public String getDateString(int index) {
		
		return getCalString(getDate(index));
	}
	
	public String getTooltipStrings(int index) {
		
		if (index >= stockDataList.size()) return null;
		
		StockData stockData = stockDataList.get(index);
	    
		return String.format("period: %s\nhigh: %d\nstart: %d\nclose: %d\nlow: %d", 
				getCalString(stockData.calendar), 
				stockData.highPrice,
				stockData.startPrice,
				stockData.endPrice,
				stockData.lowPrice);
	}
	
	private static String getCalString(Calendar cal) {
		
		return String.valueOf(cal.get(Calendar.YEAR))+"/"
				+String.valueOf(cal.get(Calendar.MONTH)+1)+"/"
				+String.valueOf(cal.get(Calendar.DATE));
	}
	
	public void setSMA() {
		
		for(int i=0; i<stockDataList.size(); i++) {
			
			stockDataList.get(i)._5SMA = getSMA(i, 5);
			stockDataList.get(i)._13SMA = getSMA(i, 13);
			stockDataList.get(i)._25SMA = getSMA(i, 25);
		}
	}
	
	public int getSMA(int index, int range) {
		
		int result = -1;
		int sum = 0;
		
		try {
		
		for(int i=0; i<range; i++) {
			
			sum += stockDataList.get(index -i).endPrice;
		}
		result = sum / range;
		
		}catch(Exception e){
			
		}
		
		return result;
	}
	
	public int getMaxHighPrice(int startIndex, int endIndex) {
	
		int result = -1;
		
		for(int i=startIndex; i<=endIndex; i++) {
			
			if(i>=stockDataList.size()) break;
			result = Math.max(result, stockDataList.get(i).highPrice);
		}
		return result;
	}
	
	public int getMinLowPrice(int startIndex, int endIndex) {
		
		int result = Integer.MAX_VALUE;
		
		for(int i=startIndex; i<=endIndex; i++) {
			
			if(i>=stockDataList.size()) break;
			result = Math.min(result, stockDataList.get(i).lowPrice);
		}
		return result;
	}
	
	public int getMaxHighAmount(int startIndex, int endIndex) {
		
		int result = -1;
		
		for(int i=startIndex; i<=endIndex; i++) {
			
			result = Math.max(result, stockDataList.get(i).amount);
		}
		return result;
	}
}
