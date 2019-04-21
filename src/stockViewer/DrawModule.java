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
}
