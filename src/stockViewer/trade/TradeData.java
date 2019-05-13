package stockViewer.trade;

import java.util.Calendar;

public class TradeData {

	String tradingDate, buyOrSell, profitText;
	
	public Calendar date = Calendar.getInstance();
	public boolean isBuy;
	
	public int databaseID =-1, tickerCode, price, unit;
	
	public int sumUnit, acount, profit;
	
	public TradeData() {
		
		this.date.clear();
		init();
	}
	
	public TradeData(int tickerCode, Calendar date, boolean isBuy, int price, int unit) {
		
		this.date.clear();
		
		this.tickerCode = tickerCode;
		this.date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
		this.isBuy = isBuy;
		this.price = price;
		this.unit = unit;
		
		init();
	}
	
	public void init() {
		
		this.tradingDate = getCalString(date);
		this.buyOrSell = isBuy ? "Buy" : "Sell";
		this.profitText= "";
	}
	
	private static String getCalString(Calendar cal) {
		
		return String.valueOf(cal.get(Calendar.YEAR))+"/"
				+String.valueOf(cal.get(Calendar.MONTH)+1)+"/"
				+String.valueOf(cal.get(Calendar.DATE));
	}

	public int getSumUnit(int sumUnit) {
		
		return sumUnit + (isBuy ? unit : -unit);
	}
	
	public int getAcount(int acount) {
		
		return acount - price * (isBuy ? unit : -unit);
	}
	
	public void setProfit(int preBalancedAcount) {
		
		if (sumUnit==0) {
		
			profit = acount - preBalancedAcount;
			profitText = "("+String.valueOf(profit)+")";
		}else {
			profit = 0;
			profitText = "";
		}
	}
}
