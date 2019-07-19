package stockViewer.subscreen;

import javafx.scene.paint.Color;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;
import stockViewer.subscreen.SubScreenDrawModule.Technical;

public class PriceMovementIndicator {
	
	private static final int RANGE_PERIOD = 10;
	
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
	
	public static double getDirectionalMovementIndex(ChartData chartData, int periodIndex, int rangePeriod) {
		
		double DI_plus = getDirectionalIndicator(true, chartData, periodIndex, RANGE_PERIOD);
		double DI_minus = -getDirectionalIndicator(false, chartData, periodIndex, RANGE_PERIOD);
		
		if((DI_plus + DI_minus) == 0) return -1;
		
		return Math.abs(DI_plus - DI_minus) / (DI_plus + DI_minus) * 100;
	}
	
	private SubScreenDrawModule module;
	
	public PriceMovementIndicator(SubScreenDrawModule module) {
		
		this.module = module;
	}
	
	public void init(int minPrice, int maxPrice, Technical technical) {
		
		switch(technical) {
		
		case PRICEMOVEMENTBAR:
			double range = (maxPrice + minPrice) / 2 * 0.025d;
			module.setValueRange(-range, range);
			break;
			
		case DIRECTIONALINDEX:
			module.setValueRange(0, 100);
			break;
		}
	}
	
	public void draw(ChartData chartData, int minPeriod, int maxPeriod, Technical technical) {
		
		if(technical == Technical.DIRECTIONALINDEX) module.drawVirticalGrid(25, Color.BLACK, 2);
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			switch(technical) {
			
			case PRICEMOVEMENTBAR:
				
				double aveTrueRange, DI_plus, DI_minus;
				
				aveTrueRange = getAveTrueRange(chartData, i, RANGE_PERIOD);
				DI_plus = getDirectionalIndicator(true, chartData, i, RANGE_PERIOD);
				DI_minus = -getDirectionalIndicator(false, chartData, i, RANGE_PERIOD);
				
				drawPriceMovementBar(i, aveTrueRange, DI_plus, DI_minus);
				break;
				
			case DIRECTIONALINDEX:
				
				double DX, preDX;
				
				DX = getDirectionalMovementIndex(chartData, i, RANGE_PERIOD);
				preDX = getDirectionalMovementIndex(chartData, i-1, RANGE_PERIOD);
				if(DX == -1| preDX==-1) continue;
				
				drawDXLine(i, preDX, DX);
				break;
			}
		}
	}
	
	private void drawPriceMovementBar(int periodIndex, double aveTrueRange, double DI_plus, double DI_minus) {
		
		if(aveTrueRange == -1) return;
		
		double plusPow = DI_plus * aveTrueRange / 100;
		double minusPow = DI_minus * aveTrueRange / 100;
		
		module.drawBar(aveTrueRange/2, -aveTrueRange/2, periodIndex, Color.BLACK);
		
		if(plusPow > minusPow) {
			module.drawBar(plusPow/2 , -plusPow/2 , periodIndex, Color.RED);
			module.drawBar(minusPow/2 , -minusPow/2 , periodIndex, Color.BLUE);
		}
		else {
			module.drawBar(minusPow/2 , -minusPow/2 , periodIndex, Color.BLUE);
			module.drawBar(plusPow/2 , -plusPow/2 , periodIndex, Color.RED);
		}
	}
	
	private void drawDXLine(int periodIndex, double preDX, double DX) {
		
		module.drawLine(preDX, DX, periodIndex, Color.BLUEVIOLET , 0);
	}

}
