package stockViewer.subscreen;

import javafx.scene.paint.Color;
import stockViewer.stockdata.ChartData;
import stockViewer.subscreen.SubScreenDrawModule.Technical;

public class Turnover {
	
	private SubScreenDrawModule module;
	
	public Turnover(SubScreenDrawModule module) {
		
		this.module = module;
	}
	
	public void init(int maxAmount) {
	
		module.setValueRange(0, maxAmount); 
	}
	
	public void draw(ChartData chartData, int minPeriod, int maxPeriod) {
		
		for(int i=minPeriod; i<=maxPeriod; i++) {
				
			double amount = chartData.stockDataList.get(i).amount;
			module.drawBar(amount, 0, i, Color.DARKGREEN);
		}
	}
	
	private void drawDXLine(int periodIndex, double preDX, double DX) {
		
		module.drawLine(preDX, DX, periodIndex, Color.BLUEVIOLET , 0);
	}

}
