package stockViewer;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import stockViewer.stockdata.StockData;
import stockViewer.trade.TradeData;
import stockViewer.analysis.LarrysLine;
import stockViewer.analysis.RegressionLine;
import stockViewer.stockdata.ChartData;

public class DrawModule {
	
	public DrawModule(MainApp mainApp){
		
	}
	
	private int minValue, maxValue;
	private int minPeriod, maxPeriod;
	
	public boolean _5SMA_sw,_13SMA_sw ,_25SMA_sw, envelope_sw, LarrysLine_sw, RegressionLine_sw;
	
	private LarrysLine larrysLine = new LarrysLine(this);
	private RegressionLine regressionLine = new RegressionLine(this);
	
	private Color backGroundColor = Color.BLACK;
	
	public void init(ChartData chartData) {
		
		initLarrysLine(chartData);
		initRegressionLine(chartData);
	}
	
	public void setFilterChart(boolean isFilterChart) {
		
		backGroundColor = isFilterChart ? Color.MISTYROSE : Color.ALICEBLUE;
	}
	
	private void initLarrysLine(ChartData chartData) {
		
		larrysLine.init(chartData);
	}
	
	private void initRegressionLine(ChartData chartData) {
		
		regressionLine.init(chartData);
	}
	
	public void setValueRange(int minValue, int maxValue){
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public void setPeriodRange(int minPeriod, int maxPeriod){
		
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}
	
	private int getXcoord(int period) {
		
		double width = MainSceneUtil.CanvasX;
		
		return  (int) (width / (maxPeriod - minPeriod + 1) * (period - minPeriod));
	}
	
	private int getYcoord(int value) {
		
		double height = MainSceneUtil.CanvasY;
		
		return  (int) (height - height / (maxValue - minValue) * (value - minValue));
	}
	
	public int getPeriodByXcoord(double x) {
		
		double width = MainSceneUtil.CanvasX;
		
		return (int)(x * (maxPeriod - minPeriod + 1) / width) + minPeriod;
	}
	
	public void clearScreen(){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setFill(backGroundColor);
		gc.fillRect(0,0,MainSceneUtil.CanvasX, MainSceneUtil.CanvasY);
	}
	
	public void drawPricePercentGrids() {
		
		for(float i=0.9f; i>0.4f; i-=0.1f) {
			
			drawVirticalGrid((int)(maxValue * (i+0.05f)), Color.rgb(128, 128, 200), 5);
			drawVirticalGrid((int)(maxValue * i), Color.rgb(128, 128, 200), 0);
			
		}
	}
	
	public void drawVirticalGrid(int value, Color color, double dash){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineDashes(dash);
		
		int y = getYcoord(value);
		gc.strokeLine(0, y, MainSceneUtil.CanvasX, y);
		
	}
	
	public void drawScreen(ChartData chartData, int startIndex, int endIndex, boolean priceZoom) {
		
		int minPrice = chartData.getMinLowPrice(startIndex, endIndex);
		int maxPrice = chartData.getMaxHighPrice(startIndex, endIndex);
		
		int margin = (int)((maxPrice - minPrice) * .05d);
		
		setPeriodRange(startIndex, endIndex);
		if(priceZoom) setValueRange(minPrice - margin , maxPrice + margin);
		else setValueRange(0 , maxPrice);
		
		if (!priceZoom) drawPricePercentGrids();
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			if(i>chartData.stockDataList.size()-1) break;
			
			StockData data = chartData.stockDataList.get(i);
			drawPriceBar(data, i);
		}
		
		drawTrendLine(chartData);
	}
	
	public void drawTradeMarks(ChartData chartData, ArrayList<TradeData> tradeList) {
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			if(i>chartData.stockDataList.size()-1) break;
			
			Calendar date = chartData.stockDataList.get(i).calendar;
			
			for(TradeData e: tradeList) {
				
				if(e.date.compareTo(date)==0) {
					drawTradeMark(e.isBuy, e.price, i);
					
				}
			}
		}
	}
	
	private void drawTrendLine(ChartData chartData) {
		
		if(LarrysLine_sw) larrysLine.draw(chartData, minPeriod, maxPeriod);
		if(RegressionLine_sw) regressionLine.draw(chartData, minPeriod, maxPeriod);
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
			
			if(i>chartData.stockDataList.size()-1) continue;
			if(i==0) continue;
			
			int price, prePrice;
			
			if(_5SMA_sw) {
				price = chartData.stockDataList.get(i)._5SMA;
				prePrice = chartData.stockDataList.get(i-1)._5SMA;
				if (price>0 && prePrice>0) drawLine(prePrice, price, i, Color.rgb(64, 255, 64),0);
			}
			
			if(_13SMA_sw) {
				price = chartData.stockDataList.get(i)._13SMA;
				prePrice = chartData.stockDataList.get(i-1)._13SMA;
				if (price>0 && prePrice>0) drawLine(prePrice, price, i, Color.rgb(32, 210, 32),0);
				}
			
			if(_25SMA_sw) {
				price = chartData.stockDataList.get(i)._25SMA;
				prePrice = chartData.stockDataList.get(i-1)._25SMA;
				if (price>0 && prePrice>0) drawLine(prePrice, price, i, Color.rgb(0, 180, 0),0);
				}
			
			if(envelope_sw) {
				price = (int)(chartData.stockDataList.get(i)._13SMA * 1.05f);
				prePrice = (int)(chartData.stockDataList.get(i-1)._13SMA * 1.05f);
				if (price>0 && prePrice>0) drawLine(prePrice, price, i, Color.rgb(128, 210, 128),5);
				
				price = (int)(chartData.stockDataList.get(i)._13SMA * 0.95f);
				prePrice = (int)(chartData.stockDataList.get(i-1)._13SMA * 0.95f);
				if (price>0 && prePrice>0) drawLine(prePrice, price, i, Color.rgb(128, 210, 128),5);
				}
		}
	}
	
	public void drawPriceBar(StockData stockData, int period) {
		
		drawLeg(stockData, period);
		drawBar(stockData, period);
	}
	
	private void drawLeg(StockData stockData, int period) {
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		
		double top = getYcoord(stockData.highPrice);
		double bottom = getYcoord(stockData.lowPrice);
		double height = bottom - top;
		
		double left = getXcoord(period);
		double right = getXcoord(period+1);
		double width = (right - left) / 4;
		left = left + width * 3 / 2;
		
		gc.setFill(Color.BLACK);
		gc.fillRect(left, top, width, height);
	}
	
	private void drawBar(StockData stockData, int period) {
		
		double BarWidth = 0.5;
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		
		int higher, lower;
		
		if(stockData.startPrice > stockData.endPrice) {
			
			higher = stockData.startPrice;
			lower = stockData.endPrice;
			gc.setFill(Color.BLUE);
		}else {
			
			higher = stockData.endPrice;
			lower = stockData.startPrice;
			gc.setFill(Color.RED);
		}
		
		double top = getYcoord(higher);
		double bottom = getYcoord(lower);
		double height = bottom - top;
		
		double left = getXcoord(period);
		double right = getXcoord(period+1);
		double width = (right - left) / 3 * 2;
		left = left + width  / 4;
		
		gc.fillRect(left, top, width, height);
	}
	
	public void drawLine(int prePrice, int price, int period, Color color, double dash) {
		
		double startX = getXcoord(period -1);
		double endX = getXcoord(period);
		double startY = getYcoord(prePrice);
		double endY = getYcoord(price);
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineDashes(dash);
		gc.setLineWidth(1);
		gc.strokeLine(startX, startY, endX, endY);
	}
	
	public void drawLine(int prePrice, int price, int prePeriod, int period, Color color, double dash) {
		
		double startX = (getXcoord(prePeriod) + getXcoord(prePeriod+1))/2;
		double endX = (getXcoord(period) + getXcoord(period+1))/2;
		double startY = getYcoord(prePrice);
		double endY = getYcoord(price);
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setStroke(color);
		gc.setLineDashes(dash);
		gc.setLineWidth(1);
		gc.strokeLine(startX, startY, endX, endY);
	}
	
	private void drawTradeMark(boolean isBuy, int price, int period) {
		
		double X1 = getXcoord(period)-5;
		double X2 = getXcoord(period+1)+5;
		double X3 = (X1+X2)/2;
		double Y1 = getYcoord(price);
		double h = (X2-X1)/2*Math.sqrt(3);
		double Y2 = Y1 + (isBuy ? h : -h);
		double xPoints[] = {X3, X1, X2};
		double yPoints[] = {Y1, Y2, Y2};
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setFill(Color.GREEN);
		gc.fillPolygon(xPoints, yPoints, 3);
	}
	
	public void drawExtPriceMark(boolean isHighExt, int price, int period, double lineWidth) {
		
		double X1 = getXcoord(period);
		double X2 = getXcoord(period+1);
		double d = X2-X1;
		double Y1 = getYcoord(price);
		double Y2 = isHighExt ? Y1-d : Y1;
		double startAngle = isHighExt ? 0 : 180;
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setStroke(Color.GREEN);
		gc.setLineDashes(0);
		gc.setLineWidth(lineWidth);
		gc.strokeArc(X1, Y2, d, d, startAngle, 180, ArcType.OPEN);
		
	}
}
