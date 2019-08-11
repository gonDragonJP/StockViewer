package stockViewer.analysis;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.paint.Color;
import stockViewer.DrawModule;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;

public class RegressionLine {
	
	private static final int regressionPeriod = 25;
	private static final int skipPeriod = 1;
	
	private DrawModule drawModule;
	
	public RegressionLine(DrawModule drawModule) {
		
		this.drawModule = drawModule;
	}
	
	ArrayList<RegressionLineParameter> regressionParamList;
	ArrayList<LineCoord> lineCoordList;
	
	public void init(ChartData chartData) {
		
		regressionParamList = getLineParamList(chartData);
		lineCoordList = getLineCoordList(regressionParamList);
	}
	
	public void draw(ChartData chartData, int startIndex, int endIndex) {
		
		for(LineCoord e: lineCoordList) {
			
			if((startIndex<= e.endIndex && e.startIndex<=endIndex))
				drawModule.drawLine(e.startPrice, e.endPrice, e.startIndex, e.endIndex, Color.rgb(128, 128, 128),3);
		}
	}
	
	private class RegressionLineParameter {
		
		int lineCenterIndex;
		double averagePrice, inclination; 
		
		public RegressionLineParameter(int lineCenterIndex, double averagePrice, double inclination) {
			
			this.lineCenterIndex = lineCenterIndex;
			this.averagePrice = averagePrice;	this.inclination = inclination;
		}
	}
	
	private class LineCoord {
		
		int startIndex, endIndex;
		int startPrice, endPrice;
		
		public LineCoord(int startIndex, int startPrice, int endIndex, int endPrice) {
			this.startIndex = startIndex;	this.endIndex = endIndex;
			this.startPrice = startPrice;	this.endPrice = endPrice;
		}
	}
	
	private ArrayList<LineCoord> getLineCoordList(ArrayList<RegressionLineParameter> lineParamList) {
		
		ArrayList<LineCoord> list = new ArrayList<>();
		
		for(RegressionLineParameter e: lineParamList) {
			
			int a = (regressionPeriod - 1 )/2;
			int startIndex = e.lineCenterIndex - a;
			int endIndex = e.lineCenterIndex + a;
			int startPrice = (int)(e.averagePrice - e.inclination * a);
			int endPrice = (int)(e.averagePrice + e.inclination * a);
			
			list.add(new LineCoord(startIndex, startPrice, endIndex, endPrice));
		}
		return list;
	}

	private ArrayList<RegressionLineParameter> getLineParamList(ChartData chartData){
		
		ArrayList<RegressionLineParameter> list = new ArrayList<>();
		
		double periodVariance = getPeriodVariance();
		
		for(int i=0; i<chartData.stockDataList.size(); i+=skipPeriod) {
			
			double priceAverage = chartData.getSMA(i, regressionPeriod);
			if (priceAverage == -1) continue;
			
			double inclination = getCoVariance(chartData.stockDataList,i,priceAverage) / periodVariance;
			int a = (regressionPeriod - 1 )/2;
			
			list.add(new RegressionLineParameter(i-a, priceAverage, inclination));
		}
		
		return list;
	}
	
	private double getCoVariance(ArrayList<StockData> dataList, int index, double priceAverage) {
		
		double result = 0;
		
		int a = (regressionPeriod - 1 )/2; 
		
		for(int i=0; i<regressionPeriod; i++) {
			
			result += (regressionPeriod - i - a - 1) * (dataList.get(index-i).endPrice - priceAverage);
		}
		
		return result;
	}
	
	private double getPeriodVariance() {
		
		double result = 0;
		
		int a = (regressionPeriod - 1 )/2; 
		
		for(int i=0; i<regressionPeriod; i++) {
			
			result += Math.pow((i-a),2);
		}
		
		return result;
	}
}
