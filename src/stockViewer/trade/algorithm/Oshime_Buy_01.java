package stockViewer.trade.algorithm;

import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;
import stockViewer.subscreen.Stochastics;

public class Oshime_Buy_01 extends TradingAlgoShell{

	public Oshime_Buy_01(ChartData chartData) {
		super(chartData);
		
	}
	
	private double lossCutPrice;

	@Override
	boolean checkBuyAlgo(int periodIndex) {
		
		final int CHECK_EMA = 25;
		final int CHECK_POST = 13;
		final int STOCHASTICS_RANGE = 5;
		final double LOSS_CUT = 0.95d;
		
		StockData stockData = chartData.stockDataList.get(periodIndex);
		
		int EMA = chartData.getSMA(periodIndex, CHECK_EMA);
		int preEMA = chartData.getSMA(periodIndex - CHECK_POST, CHECK_EMA);
		
		if(EMA<0 || preEMA<0) return false;
		
		if(EMA>preEMA) { //if up-trend
			
			double stochas = Stochastics.getKValue(chartData, periodIndex, STOCHASTICS_RANGE);
			if(stochas >0 && stochas <20) {// if stochastics<20%
			
				lossCutPrice = stockData.endPrice * LOSS_CUT;
				return true;
			} 
		}
		
		return false;
	}

	@Override
	boolean checkSellAlgo(int periodIndex) {
		
		final int STOCHASTICS_RANGE = 4;
		
		if(positionSide != PositionSide.BUY) return false;
		
		StockData stockData = chartData.stockDataList.get(periodIndex);
		
		double stochas = Stochastics.getKValue(chartData, periodIndex, STOCHASTICS_RANGE);
		if(stochas >90) {return true;} // if stochastics>90%
		if(stockData.endPrice < lossCutPrice) {return true;} // loss-cut
		
		return false;
	}

}
