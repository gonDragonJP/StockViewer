package stockViewer.trade;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class TradeResultGraph extends DrawModule{

	public TradeResultGraph(Canvas canvas) {
		super(canvas);
		
	}
	
	public ArrayList<TradeData> clearingPointList = new ArrayList<>();
	
	public void refresh(TradeDataList dataList) {
		
		clear();
		if(dataList.size() ==0) return;
		
		clearingPointList = dataList.getClearingPointList();
		setRange();
		
		this.drawVirticalGrid(0, Color.BLACK, 4);
		drawGraph();
	}
	
	private void setRange() {
		
		this.setValueRange(getMinAcount(), getMaxAcount());
		this.setPeriodRange(0, clearingPointList.size());
	}
	
	private double getMaxAcount() {
		
		double maxAcount =0;
		for(TradeData e: clearingPointList) maxAcount = Math.max(e.acount, maxAcount);
		return maxAcount;
	}
	
	private double getMinAcount() {
		
		double minAcount =0;
		for(TradeData e: clearingPointList) minAcount = Math.min(e.acount, minAcount);
		return minAcount;
	}
	
	private void drawGraph() {
		
		double preValue = 0;
		
		for(int i=0; i<clearingPointList.size(); i++) {
			
			double value = clearingPointList.get(i).acount;
			
			this.drawLine(preValue, value , i + 1, Color.RED, 0);
			preValue = value;
		}
	}

}
