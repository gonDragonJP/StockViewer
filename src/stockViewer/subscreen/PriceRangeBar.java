package stockViewer.subscreen;

import javafx.scene.paint.Color;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;

public class PriceRangeBar {
	
	private static final int RANGE_PERIOD = 5;
	
	public static int getSumTrueRange(ChartData chartData, int periodIndex, int rangePeriod) {
		
		int sumTrueRange =0;
		
		try {
			
		for(int i=0; i<rangePeriod; i++) {
			
			int preEndPrice = chartData.stockDataList.get(periodIndex - i - 1).endPrice;
			StockData stockData = chartData.stockDataList.get(periodIndex - i);
			sumTrueRange += stockData.getTrueRange(preEndPrice);
		}
		}catch(Exception e) {
			
			return -1;
		}
		
		return sumTrueRange;
	}
	
	public static double getAveTrueRange(ChartData chartData, int periodIndex, int rangePeriod) {
		
		int sumTrueRange = getSumTrueRange(chartData, periodIndex, rangePeriod);
		
		if(sumTrueRange == -1) return -1;
		
		return  (double)sumTrueRange / rangePeriod;
	}
	
	public static int getSumDirectionalMovement(boolean isPlusDM, ChartData chartData, int periodIndex, int rangePeriod) {
		
		int sumDM =0;
		
		try {
			
		for(int i=0; i<rangePeriod; i++) {
			
			StockData preStockData = chartData.stockDataList.get(periodIndex - i - 1);
			StockData stockData = chartData.stockDataList.get(periodIndex - i);
			
			int DM = stockData.getDirectionalMovement(preStockData.highPrice, preStockData.lowPrice);
			
			if(DM>0) sumDM += isPlusDM ? DM : 0 ;
			else sumDM += isPlusDM ? 0 : DM;
		}
		}catch(Exception e) {
			
			return 0;
		}
		
		return sumDM;
	}
	
	public static double getDirectionalIndicator(boolean isPlusDM, ChartData chartData, int periodIndex, int rangePeriod) {
		
		int sumTrueRange = getSumTrueRange(chartData, periodIndex, rangePeriod);
		
		if(sumTrueRange == -1 | sumTrueRange == 0) return 0;
		
		int sumDirectionalMovement = getSumDirectionalMovement(isPlusDM, chartData, periodIndex, rangePeriod);
		
		return (double)sumDirectionalMovement / sumTrueRange * 100;
	}
	
	
	private SubScreenDrawModule module;
	
	public PriceRangeBar(SubScreenDrawModule module) {
		
		this.module = module;
	}
	
	public void init() {
		
		module.setValueRange(-200, 200);
	}
	
	public void draw(ChartData chartData, int minPeriod, int maxPeriod) {
		
		//module.drawVirticalGrid(0, Color.BLACK, 2);
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			double aveTrueRange, DI_plus, DI_minus;
			
			aveTrueRange = getAveTrueRange(chartData, i, RANGE_PERIOD);
			DI_plus = getDirectionalIndicator(true, chartData, i, RANGE_PERIOD);
			DI_minus = -getDirectionalIndicator(false, chartData, i, RANGE_PERIOD);
			
			double plusPow = DI_plus * aveTrueRange / 100;
			double minusPow = DI_minus * aveTrueRange / 100;
			
			module.drawBar(aveTrueRange/2, -aveTrueRange/2, i, Color.BLACK);
			
			if(plusPow > minusPow) {
				module.drawBar(plusPow/2 , -plusPow/2 , i, Color.RED);
				module.drawBar(minusPow/2 , -minusPow/2 , i, Color.BLUE);
			}
			else {
				module.drawBar(minusPow/2 , -minusPow/2 , i, Color.BLUE);
				module.drawBar(plusPow/2 , -plusPow/2 , i, Color.RED);
			}
		}
	}	

}
