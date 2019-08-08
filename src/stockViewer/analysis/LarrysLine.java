
package stockViewer.analysis;

import java.util.ArrayList;
import java.util.Calendar;

import javafx.scene.paint.Color;
import stockViewer.DrawModule;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.StockData;

public class LarrysLine {
	
	public static ArrayList<StockData> getExtPriceStockList(ArrayList<StockData> srcList, boolean isHighExt){
		
		ArrayList<StockData> dstList = new ArrayList<>();
		for(int i=0; i<srcList.size(); i++) {
		
			if(isHighExt && isExtHighPrice(srcList,i)) dstList.add(srcList.get(i));
			if(!isHighExt && isExtLowPrice(srcList,i)) dstList.add(srcList.get(i));
		}
		
		return dstList;
	}
	
	public static boolean isExtHighPrice(ArrayList<StockData> stockDataList, int index) {
		
		try {
		
		int preHighPrice = stockDataList.get(index-1).highPrice;
		int highPrice = stockDataList.get(index).highPrice;
		int postHighPrice = stockDataList.get(index+1).highPrice;
			
		if (preHighPrice < highPrice && highPrice > postHighPrice) return true;
			
		}catch(Exception e) {
			
			return false;
		}
		
		return false;
	}
	
	public static boolean isExtLowPrice(ArrayList<StockData> stockDataList, int index) {
		
		try {
		
		int preLowPrice = stockDataList.get(index-1).lowPrice;
		int lowPrice = stockDataList.get(index).lowPrice;
		int postLowPrice = stockDataList.get(index+1).lowPrice;
			
		if (preLowPrice > lowPrice && lowPrice < postLowPrice) return true;
			
		}catch(Exception e) {
			
			return false;
		}
		
		return false;
	}
	
	private DrawModule drawModule;
	
	public LarrysLine(DrawModule drawModule) {
		
		this.drawModule = drawModule;
	}
	
	ArrayList<StockData> shortTermHighExtList, shortTermLowExtList;
	ArrayList<StockData> middleTermHighExtList, middleTermLowExtList;
	ArrayList<LineCoord> lineCoordList;
	
	public void init(ChartData chartData) {
		
		shortTermHighExtList = getExtPriceStockList(chartData.stockDataList, true);
		middleTermHighExtList = getExtPriceStockList(shortTermHighExtList, true);
		
		shortTermLowExtList = getExtPriceStockList(chartData.stockDataList, false);
		middleTermLowExtList = getExtPriceStockList(shortTermLowExtList, false);
		
		lineCoordList = getLineCoordList(middleTermHighExtList, middleTermLowExtList);
	}
	
	public void draw(ChartData chartData, int startIndex, int endIndex) {
		
		for(StockData e: shortTermHighExtList) {
			
			int i= chartData.getIndexByDate(e.calendar);
			if(i!=-1 && startIndex<=i && i<=endIndex) drawModule.drawExtPriceMark(true, e.highPrice, i, 1);
		}
		
		for(StockData e: shortTermLowExtList) {
			
			int i= chartData.getIndexByDate(e.calendar);
			if(i!=-1 && startIndex<=i && i<=endIndex) drawModule.drawExtPriceMark(false, e.lowPrice, i, 1);
		}
		
		for(StockData e: middleTermHighExtList) {
		
			int i= chartData.getIndexByDate(e.calendar);
			if(i!=-1 && startIndex<=i && i<=endIndex) drawModule.drawExtPriceMark(true, e.highPrice, i, 3);
		}
		
		for(StockData e: middleTermLowExtList) {
			
			int i= chartData.getIndexByDate(e.calendar);
			if(i!=-1 && startIndex<=i && i<=endIndex) drawModule.drawExtPriceMark(false, e.lowPrice, i, 3
					);
		}
		
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
