package stockViewer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.Global;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.TickerData;

public class DBAccessOfTickerDataTable {

	private String filePath;
	
	private String tableName;
	
	public DBAccessOfTickerDataTable(){
		
		this.filePath = Global.databasePath;
		this.tableName = "tickerBoard";
	}
	
	public TickerData getTickerData(int tickerCode) {
		
		TickerData tickerData = null;
		
		SQLiteManager.initDatabase(filePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+ tableName +" where tickerCode=";
		sql += String.valueOf(tickerCode);
		sql += ";";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				tickerData = generateTickerData(resultSet);
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
		
		return tickerData;
	}
	
	public void setTickerBoardList(ArrayList<TickerData> tickerBoardList){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+tableName+" order by tickerCode;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				tickerBoardList.add(generateTickerData(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private TickerData generateTickerData(ResultSet resultSet){
		
		TickerData tickerData = new TickerData();
		
		setTickerData(tickerData, resultSet);
		
		return tickerData;
	}
	
	private void setTickerData(TickerData tickerData, ResultSet resultSet){
		
		try {
			tickerData.tickerCode = resultSet.getInt("tickerCode");
			
			tickerData.marketName = resultSet.getString("marketName");
			
			tickerData.stockName = resultSet.getString("stockName");
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void addTickerData(TickerData tickerData){
		
		SQLiteManager.initDatabase(filePath);
			
		add(tickerData);
		
		SQLiteManager.closeDatabase();
	}
	
	private void add(TickerData tickerData){
		
		String sql = "insert into "+tableName+" values(";
		
		sql += "NULL,";
		sql += String.valueOf(tickerData.tickerCode) +",";
		sql += "'"+ tickerData.marketName +"',";
		sql += "'"+ tickerData.stockName +"'";
		
		sql += ");";
		
		System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
}
