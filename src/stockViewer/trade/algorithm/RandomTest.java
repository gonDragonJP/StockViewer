package stockViewer.trade.algorithm;

import stockViewer.stockdata.ChartData;

public class RandomTest extends TradingAlgoShell{

	public RandomTest(ChartData chartData) {
		super(chartData);
		
	}

	@Override
	boolean checkBuyAlgo(int periodIndex) {
		
		if(Math.random()<0.5) {return true;}
		
		return false;
	}

	@Override
	boolean checkSellAlgo(int periodIndex) {
		
		if(Math.random()<0.5) {return true;}
		
		return false;
	}

}
