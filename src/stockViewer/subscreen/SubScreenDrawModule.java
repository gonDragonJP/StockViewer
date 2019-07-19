package stockViewer.subscreen;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import stockViewer.stockdata.StockData;
import stockViewer.trade.TradeData;
import stockViewer.MainApp;
import stockViewer.MainSceneUtil;
import stockViewer.stockdata.ChartData;

public class SubScreenDrawModule {
	
	public enum Technical {NONE, STOCHASTICS, PRICEMOVEMENTBAR, DIRECTIONALINDEX, TURNOVER};
	
	private Stochastics stochastics = new Stochastics(this);
	private PriceMovementIndicator priceMovementIndicator = new PriceMovementIndicator(this);
	private Turnover turnover = new Turnover(this);
	
	private GraphicsContext context;
	private double canvasX, canvasY;
	
	public SubScreenDrawModule(Canvas canvas){
		
		this.context = canvas.getGraphicsContext2D();
		
		canvasX = canvas.getWidth();
		canvasY = canvas.getHeight();
	}
	
	private Technical nowShowingTechnical = Technical.NONE;
	
	public void setShowingTechnical(Technical technical) {
		
		nowShowingTechnical = technical;
	}
	
	private double minValue, maxValue;
	private int minPeriod, maxPeriod;
	
	public void setValueRange(double minValue, double maxValue){
		
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	
	public void setPeriodRange(int minPeriod, int maxPeriod){
		
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}
	
	private int getXcoord(int period) {
		
		return  (int) (canvasX / (maxPeriod - minPeriod + 1) * (period - minPeriod));
	}
	
	private double getYcoord(double value) {
		
		return  (canvasY - canvasY / (maxValue - minValue) * (value - minValue));
	}
	
	public int getPeriodByXcoord(double x) {
		
		return (int)(x * (maxPeriod - minPeriod + 1) / canvasX) + minPeriod;
	}
	
	private Color backGroundColor = Color.GAINSBORO;
	
	public void clearScreen() {
		
		context.setFill(backGroundColor);
		context.fillRect(0,0, canvasX, canvasY);
	}
	
	public void drawScreen(ChartData chartData, int startIndex, int endIndex) {
		
		setPeriodRange(startIndex, endIndex);
		
		int minPrice = chartData.getMinLowPrice(startIndex, endIndex);
		int maxPrice = chartData.getMaxHighPrice(startIndex, endIndex);
		
		switch(nowShowingTechnical) {
		
		case NONE: break;
		
		case STOCHASTICS:
			stochastics.init();
			stochastics.draw(chartData, minPeriod, maxPeriod);
			break;
			
		case PRICEMOVEMENTBAR:
			priceMovementIndicator.init(minPrice, maxPrice, Technical.PRICEMOVEMENTBAR);
			priceMovementIndicator.draw(chartData, minPeriod, maxPeriod, Technical.PRICEMOVEMENTBAR);
			break;
			
		case DIRECTIONALINDEX:
			priceMovementIndicator.init(minPrice, maxPrice, Technical.DIRECTIONALINDEX);
			priceMovementIndicator.draw(chartData, minPeriod, maxPeriod, Technical.DIRECTIONALINDEX);
			break;
			
		case TURNOVER:
			turnover.init(chartData.getMaxHighAmount(minPeriod, maxPeriod));
			turnover.draw(chartData, minPeriod, maxPeriod);
			break;
		}
	}
	
	public void drawVirticalGrid(int value, Color color, double dash){
		
		context.setStroke(color);
		context.setLineDashes(dash);
		
		double y = getYcoord(value);
		context.strokeLine(0, y, canvasX, y);	
	}
	
	public void drawLine(double preValue, double value, int period, Color color, double dash) {
		
		double left = getXcoord(period -1);
		double right = getXcoord(period);
		double halfWidth = (right - left) / 2;
		
		double startX = left + halfWidth; 
		double endX = right + halfWidth;		
		double startY = getYcoord(preValue);
		double endY = getYcoord(value);
		
		context.setStroke(color);
		context.setLineDashes(dash);
		context.strokeLine(startX, startY, endX, endY);
	}
	
	public void drawBar(double highValue, double lowValue, int period, Color color) {
		
		double BarWidth = 0.8;
		
		double top = getYcoord(highValue);
		double bottom = getYcoord(lowValue);
		double height = bottom - top;
		
		double left = getXcoord(period);
		double right = getXcoord(period+1);
		double width = (right - left) * BarWidth;
		left = left + ((right - left) - width) / 2;
		
		context.setFill(color);
		context.fillRect(left, top, width, height);
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
		
		context.setFill(Color.GREEN);
		context.fillPolygon(xPoints, yPoints, 3);
	}
}
