package stockViewer.trade;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawModule {
	
	private GraphicsContext context;
	private double canvasX, canvasY;
	
	public DrawModule(Canvas canvas){
		
		this.context = canvas.getGraphicsContext2D();
		
		canvasX = canvas.getWidth();
		canvasY = canvas.getHeight();
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
}
