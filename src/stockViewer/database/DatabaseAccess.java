package stockViewer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.StockData;

public class DatabaseAccess {
	
	private String filePath;
	
	private String tableName;
	
	public DatabaseAccess(String filePath, String tableName){
		
		this.filePath = filePath;
		this.tableName = tableName;
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
			stockData.calendar.set(year, month, date);
			
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
		sql += String.valueOf(stockData.calendar.get(Calendar.MONTH)) +",";
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
}

