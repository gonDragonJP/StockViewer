package stockViewer.trade.algorithm;

import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.database.DBAccessOfTradeDataTable;
import stockViewer.database.SQLiteManager;
import stockViewer.database.TableMakerForTradeData;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;
import stockViewer.trade.TradeData;
import stockViewer.trade.TradeDataList;

public abstract class TradingAlgoShell {
	
	public boolean isEnablePyramiding = false;
	
	protected ChartData chartData;
	public TradeDataList tradeDataList = new TradeDataList();
	
	public TradingAlgoShell(ChartData chartData) {
		
		this.chartData = chartData;
		tradeDataList.clear();
		tradeWholeChart();
	}
	
	abstract boolean checkBuyAlgo(int periodIndex);
	abstract boolean checkSellAlgo(int periodIndex);
	
	public boolean isBuySignal(int periodIndex) {
		
		if(!isEnablePyramiding && positionSide == PositionSide.BUY) return false;
		
		return checkBuyAlgo(periodIndex);
	}
	
	public boolean isSellSignal(int periodIndex) {
		
		if(!isEnablePyramiding && positionSide == PositionSide.SELL) return false;
		
		return checkSellAlgo(periodIndex);
	}
	
	public void tradeWholeChart() {
		
		int tickerCode = chartData.tickerData.tickerCode;
		
		for(int i=0; i<chartData.stockDataList.size(); i++) {
			
			StockData stockData = chartData.stockDataList.get(i);
			
			if (isBuySignal(i)) trade(tickerCode, stockData.calendar, true, stockData.endPrice, 1);
			else if (isSellSignal(i)) trade(tickerCode, stockData.calendar, false, stockData.endPrice, 1);
		}
	}
	
	protected enum PositionSide {NONE, BUY, SELL};
	protected PositionSide positionSide = PositionSide.NONE;
	
	private void setPositionSide(PositionSide positionSide) {
		
		this.positionSide = positionSide;
	}
	
	public void trade(int tickerCode, Calendar date, boolean isBuy, int price, int unit) {
		
		TradeData tradeData = new TradeData(tickerCode, date, isBuy, price, unit);
		tradeDataList.add(tradeData);
		
		if(tradeData.sumUnit ==0) setPositionSide(PositionSide.NONE);
		else setPositionSide(isBuy ? PositionSide.BUY : PositionSide.SELL);
	}

}
