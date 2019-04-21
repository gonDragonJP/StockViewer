package stockViewer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CSVFileReader {
	
	public List<StockData> getStockDataList(String fileName) {
		
		List<StockData> stockDataList = new ArrayList<StockData>();
		
		int tickerCode;
		String stockName="", marketName="";
        
		try {
			
		Reader reader = new InputStreamReader(new FileInputStream(fileName));
        BufferedReader br = new BufferedReader(reader);
        
        int lineNumber =0;
        String line, data[];
        
        while((line = br.readLine()) != null) {
        	
        	switch(lineNumber) {
        	
        	case 0: 
        		data = line.split(",");
        		data = data[0].split(" ");
        		tickerCode = Integer.parseInt(data[0]);
        		marketName = data[1];
        		stockName = data[2];
        		break;
        	
        	case 1: break;
        		
        	default:
        		
        		data = line.split(",");
        		
        		StockData stockData = new StockData();
        		stockData.stockName = stockName;
        		stockData.marketName = marketName;
        		stockData.calendar = getCalendar(data[0]);
        		stockData.startPrice = getValue(data[1]);
        		stockData.highPrice = getValue(data[2]);
        		stockData.lowPrice = getValue(data[3]);
        		stockData.endPrice = getValue(data[4]);
        		stockData.amount = getValue(data[5]);
        		
        		stockDataList.add(stockData);
        	}
        	
        	lineNumber++;
        }
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return stockDataList;
    }
	
	private Calendar getCalendar(String token) {
		
		Calendar calendar = Calendar.getInstance();
		int year, month, date;
		
		token = token.substring(1, token.length()-1);
		String data[];
		data = token.split("-");
		year = Integer.parseInt(data[0]);
		month = Integer.parseInt(data[1]);
		date = Integer.parseInt(data[2]);
		
		calendar.set(year, month, date);
		
		return calendar;
	}
	
	private int getValue(String token) {
		
		int length = token.length();
		int value = Integer.parseInt(token.substring(1, length-1));
		
		return value;	
	}
	
}
