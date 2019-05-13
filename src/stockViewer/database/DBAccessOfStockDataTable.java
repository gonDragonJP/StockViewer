package stockViewer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.stockdata.StockData;
import stockViewer.Global;
import stockViewer.stockdata.ChartData;

public class DBAccessOfStockDataTable {
	
	private String filePath;
	
	private String tableName;
	
	public DBAccessOfStockDataTable(int tickerCode){
		
		this.filePath = Global.databasePath;
		this.tableName = "table_"+String.valueOf(tickerCode);
	}
	
	public void setStockDataList(ArrayList<StockData> stockDataList){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+tableName+" order by year,month,date;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				stockDataList.add(generateStockData(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private StockData generateStockData(ResultSet resultSet){
		
		StockData stockData = new StockData();
		
		setStockData(stockData, resultSet);
		
		return stockData;
	}
	
	private void setStockData(StockData stockData, ResultSet resultSet){
		
		try {
			stockData.databaseID = resultSet.getInt("ID");
			
			int year = resultSet.getInt("year");
			int month = resultSet.getInt("month");
			int date = resultSet.getInt("date");
			stockData.calendar.set(year, month-1, date); // calendarŒ^‚ÌŒŽ‚Í0-11Žw’è
			
			stockData.startPrice = resultSet.getInt("startPrice");
			stockData.highPrice = resultSet.getInt("highPrice");
			stockData.lowPrice = resultSet.getInt("lowPrice");
			stockData.endPrice = resultSet.getInt("endPrice");
			stockData.amount = resultSet.getInt("amount");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void addStockDataList(ArrayList<StockData> stockDataList){
		
		SQLiteManager.initDatabase(filePath);
		
		for(StockData e: stockDataList){
			
			add(e);
		}
		
		SQLiteManager.closeDatabase();
	}
	
	public void addStockData(StockData stockData){
		
		SQLiteManager.initDatabase(filePath);
			
		add(stockData);
		
		SQLiteManager.closeDatabase();
	}
	
	private void add(StockData stockData){
		
		String sql = "insert into "+tableName+" values(";
		
		sql += "NULL,";
		sql += String.valueOf(stockData.calendar.get(Calendar.YEAR)) +",";
		sql += String.valueOf(stockData.calendar.get(Calendar.MONTH)+1) +","; // CalendarŒ^‚ÍŒŽŽw’è‚ª0-11
		sql += String.valueOf(stockData.calendar.get(Calendar.DATE)) +",";
		sql += String.valueOf(stockData.startPrice) +",";
		sql += String.valueOf(stockData.highPrice) +",";
		sql += String.valueOf(stockData.lowPrice) +",";
		sql += String.valueOf(stockData.endPrice) +",";
		sql += String.valueOf(stockData.amount);
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	public void deleteStockData(StockData stockData){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql = "delete from "+tableName+" where ID=";
		
		sql += String.valueOf(stockData.databaseID);
		
		sql += ";";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
		
		SQLiteManager.closeDatabase();
	}
	
	public boolean checkExistStockData(StockData stockData) {
		
		SQLiteManager.initDatabase(filePath);
		
		String sql = "select * from "+tableName+" where year=";
		sql += String.valueOf(stockData.calendar.get(Calendar.YEAR));
		sql += " and month =" + String.valueOf(stockData.calendar.get(Calendar.MONTH)+1); //CalendarŒ^‚ÍŒŽ‚ª0-11
		sql += " and date =" + String.valueOf(stockData.calendar.get(Calendar.DATE));
		sql += ";";
		
		ResultSet resultSet = SQLiteManager.getResultSet(sql);
		
		boolean result = false;
		
		try {
			result = resultSet.next();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
		
		System.out.println(sql);
		
		SQLiteManager.closeDatabase();
		
		return result;	
	}
	
	
}

