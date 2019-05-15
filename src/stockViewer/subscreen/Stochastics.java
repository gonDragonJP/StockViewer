package stockViewer.subscreen;

import javafx.scene.paint.Color;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;

public class Stochastics {
	
	private static final int RANGE_PERIOD = 5;
	
	private static final int SLOW_PERIOD = 3;
	
	public static double getKValue(ChartData chartData, int periodIndex, int rangePeriod) {
		
		int highestPrice=0, lowestPrice=Integer.MAX_VALUE;
		int endPrice = -1;
		
		try {
			
		endPrice = chartData.stockDataList.get(periodIndex).endPrice;	
			
		for(int i=0; i<rangePeriod; i++) {
			
			StockData stockData = chartData.stockDataList.get(periodIndex - i);
			highestPrice = Math.max(stockData.highPrice, highestPrice);
			lowestPrice = Math.min(stockData.lowPrice, lowestPrice);
		}
		}catch(Exception e) {
			
			return -1;
		}
		
		double priceRange = highestPrice - lowestPrice;
		double kValue = (endPrice - lowestPrice) / priceRange * 100;
		
		return kValue;
	}
	
	public static double getDValue(ChartData chartData, int periodIndex, int rangePeriod, int slowPeriod) {
		
		double sumValue = 0;
		
		for(int i=0; i<slowPeriod; i++) {
			double kValue = getKValue(chartData, periodIndex - i, rangePeriod);
			if(kValue == -1) return -1;
			sumValue += kValue;
		}
		
		return sumValue / slowPeriod;
	}
	
	private SubScreenDrawModule module;
	
	public Stochastics(SubScreenDrawModule module) {
		
		this.module = module;
	}
	
	public void init() {
		
		module.setValueRange(0, 100);
	}
	
	public void draw(ChartData chartData, int minPeriod, int maxPeriod) {
		
		module.drawVirticalGrid(75, Color.BLACK, 2);
		module.drawVirticalGrid(50, Color.BLACK, 2);
		module.drawVirticalGrid(25, Color.BLACK, 2);
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			double value, preValue;
			
			value = getKValue(chartData, i, RANGE_PERIOD);
			preValue = getKValue(chartData, i-1, RANGE_PERIOD);
			if (value>=0 && preValue>=0) module.drawLine(preValue, value, i, Color.RED,4);
			
			value = getDValue(chartData, i, RANGE_PERIOD, SLOW_PERIOD);
			preValue = getDValue(chartData, i-1, RANGE_PERIOD, SLOW_PERIOD);
			if (value>=0 && preValue>=0) module.drawLine(preValue, value, i, Color.RED,0);
		}
	}

}
