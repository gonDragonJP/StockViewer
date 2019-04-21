import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;

public class CSVFileReader {
	
	public class StockData{
		
		public String stockName, marketName;
		public int tickerCode;
		public Date date;
		public int startPrice,highPrice,lowPrice,endPrice;
		public int amount;
	}

	public List<StockData> getStockDataList(String fileName) throws Exception {
		
		int tickerCode;
		String stockName="", marketName="";
        
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
        		stockData.date = getDate(data[0]);
        		
        		
        		
        		//System.out.println(String.valueOf(Integer.parseInt(data[2])));
        	}
        	
        	lineNumber++;
        }
		return null;
    }
	
	private Date getDate(String token) {
		
		String data[];
		data = token.split("-");
		
		
		
		return null;
	}
	
	private int getValue(String token) {
		
		int length = token.length();
		int value = Integer.parseInt(token.substring(1, length-1));
		
		return value;	
	}
	
}
