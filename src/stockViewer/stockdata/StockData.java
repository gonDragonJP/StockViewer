package stockViewer.stockdata;

import java.util.Calendar;

public class StockData{
	
	public int databaseID;
	public Calendar calendar;
	public int startPrice,highPrice,lowPrice,endPrice;
	public int amount;
	
	public int _5SMA, _13SMA, _25SMA;
	
	public StockData() {
		
		calendar = Calendar.getInstance();
		calendar.clear();
	}
	
	public StockData clone() {
		
		StockData clone = new StockData();
		clone.databaseID = databaseID;
		clone.calendar = (Calendar)calendar.clone();
		clone.startPrice = startPrice;
		clone.highPrice = highPrice;
		clone.lowPrice = lowPrice;
		clone.endPrice = endPrice;
		clone.amount = amount;
		
		return clone;
	}
	
	public int getPriceRange() {
		
		return highPrice - lowPrice;
	}
	
	public int getTrueRange(int preEndPrice) {
		
		return Math.max(highPrice, preEndPrice) - Math.min(lowPrice, preEndPrice);
	}
	
	public int getDirectionalMovement(int preHighPrice, int preLowPrice) {
		
		int DM_Plus = highPrice - preHighPrice;
		int DM_Minus = preLowPrice - lowPrice;
		
		DM_Plus = DM_Plus >0 ? DM_Plus : 0;
		DM_Minus = DM_Minus >0 ? DM_Minus : 0;
		
		int DM = DM_Plus > DM_Minus ? DM_Plus : - DM_Minus;
		
		return DM;
	}
}
