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
	
	private GraphicsContext context;
	private double canvasX, canvasY;
	
	private Color backGroundColor = Color.GAINSBORO;
	
	private Stochastics stocastics = new Stochastics(this);
	private PriceRangeBar priceRangeBar = new PriceRangeBar(this);
	
	
	public SubScreenDrawModule(Canvas canvas){
		
		this.context = canvas.getGraphicsContext2D();
		
		canvasX = canvas.getWidth();
		canvasY = canvas.getHeight();
	}
	
	private int minValue, maxValue;
	private int minPeriod, maxPeriod;
	
	public boolean _5SMA_sw,_13SMA_sw ,_25SMA_sw, envelope_sw;
	
	
	public void setValueRange(int minValue, int maxValue){
		
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
	
	public void clear() {
		
		context.setFill(backGroundColor);
		context.fillRect(0,0, canvasX, canvasY);
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
	
	public void drawScreen(ChartData chartData) {
		
		//stocastics.init();
		priceRangeBar.init();
		//stocastics.draw(chartData, minPeriod, maxPeriod);
		priceRangeBar.draw(chartData, minPeriod, maxPeriod);
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
