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
}
