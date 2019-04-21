package stockViewer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DrawModule {
	
	public DrawModule(MainApp mainApp){
		
	}
	
	private int minValue, maxValue;
	private int minPeriod, maxPeriod;
	
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
		
		return  (int) (width / (maxPeriod - minPeriod) * (period - minPeriod));
	}
	
	private int getYcoord(int value) {
		
		double height = MainSceneUtil.CanvasY;
		
		return  (int) (height - height / (maxValue - minValue) * (value - minValue));
	}
	
	
	public void clearScreen(){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setFill(Color.ALICEBLUE);
		gc.fillRect(0,0,MainSceneUtil.CanvasX, MainSceneUtil.CanvasY);
	}
	
	public void drawVirticalGrid(int value){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setStroke(Color.ROYALBLUE);
		
		int y = getYcoord(value);
		gc.strokeLine(0, y, MainSceneUtil.CanvasX, y);
		
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
}
