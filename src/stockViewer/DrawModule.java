package stockViewer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class DrawModule {
	
	public DrawModule(MainApp mainApp){
		
	}
	
	public void drawScreen(){
		
		clearScreen();
		drawCanvasGrid();
	}
	
	public void clearScreen(){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillRect(0,0,MainSceneUtil.CanvasX, MainSceneUtil.CanvasY);
	}
	
	public void drawCanvasGrid(){
		
		GraphicsContext gc = MainSceneUtil.canvas.getGraphicsContext2D();
		final int gridSize = 100;
		gc.setStroke(Color.ALICEBLUE);
		
		double sliderValue = 0;
		int startY = ((int)(sliderValue / gridSize)+1) * gridSize;
		
		for(int i=startY; i < startY + MainSceneUtil.CanvasY; i+=gridSize){
			
			double drawY = MainSceneUtil.CanvasY - (i - sliderValue);	
		
			gc.strokeLine(0, drawY, MainSceneUtil.CanvasX, drawY);
		}
	}
}
