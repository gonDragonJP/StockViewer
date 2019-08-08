package stockViewer.analysis;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.paint.Color;
import stockViewer.DrawModule;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;

public class RegressionLine {
	
	private DrawModule drawModule;
	
	public RegressionLine(DrawModule drawModule) {
		
		this.drawModule = drawModule;
	}
	
	ArrayList<LineCoord> lineCoordList;
	
	public void init(ChartData chartData) {
		
	}
	
	public void draw(ChartData chartData, int startIndex, int endIndex) {
		
		for(int i=1; i<lineCoordList.size(); i++) {
			
			int preIndex = chartData.getIndexByDate(lineCoordList.get(i-1).date);
			int index = chartData.getIndexByDate(lineCoordList.get(i).date);
			int prePrice = lineCoordList.get(i-1).price;
			int price = lineCoordList.get(i).price;
			
			if((startIndex<= index && preIndex<=endIndex))
				drawModule.drawLine(prePrice, price, preIndex, index, Color.rgb(0, 0, 0),0);
		}
	}
	
	private class LineCoord {
		
		Calendar date;
		int price;
		
		public LineCoord(Calendar date, int price) {
			this.date = date;
			this.price = price;
		}
	}
	
	private ArrayList<LineCoord> getLineCoordList(ArrayList<StockData> highExtList, ArrayList<StockData> lowExtList){
		
		ArrayList<LineCoord> list = new ArrayList<>();
		
		for(StockData e: highExtList) list.add(new LineCoord(e.calendar, e.highPrice));
		
		for(StockData e: lowExtList) {
			
			boolean inserted = false;
			for(int i=0; i<list.size(); i++) {
				if(e.calendar.compareTo(list.get(i).date)<0) {
					list.add(i, new LineCoord(e.calendar, e.lowPrice)); 
					inserted = true; 
					break;
				}
			}
			if(!inserted) list.add(new LineCoord(e.calendar, e.lowPrice));
		}
		return list;
	}

}
