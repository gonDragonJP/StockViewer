package stockViewer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import stockViewer.Global;
import stockViewer.stockdata.StockData;
import stockViewer.trade.TradeData;
import stockViewer.trade.TradeDataList;

public class DBAccessOfTradeDataTable {
	
	private String filePath;
	
	private String tableName;
	
	public DBAccessOfTradeDataTable(){
		
		this.filePath = Global.databasePath;
		this.tableName = "tradeRecord_Manual";
	}
	
	public void setTradeDataList(int tickerCode, TradeDataList tradeDataList){
		
		tradeDataList.clear();
		
		SQLiteManager.initDatabase(filePath);
		
		String sql;
		ResultSet resultSet;
		
		sql = "select * from "+ tableName + " where tickerCode =" + String.valueOf(tickerCode); 
		sql += " order by year,month,date;";
		resultSet = SQLiteManager.getResultSet(sql);
		 
		try {
			while(resultSet.next()){
				
				tradeDataList.add(generateTradeData(resultSet));
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		SQLiteManager.closeDatabase();
	}
	
	private TradeData generateTradeData(ResultSet resultSet){
		
		TradeData tradeData = new TradeData();
		
		setTradeData(tradeData, resultSet);
		
		return tradeData;
	}
	
	private void setTradeData(TradeData tradeData, ResultSet resultSet){
		
		try {
			
			tradeData.databaseID = resultSet.getInt("ID");
			tradeData.tickerCode = resultSet.getInt("tickerCode");
			int year = resultSet.getInt("year");
			int month = resultSet.getInt("month");
			int date = resultSet.getInt("date");
			tradeData.date.set(year, month-1, date); // calendarŒ^‚ÌŒŽ‚Í0-11Žw’è
			tradeData.isBuy = resultSet.getBoolean("isBuy");
			tradeData.price = resultSet.getInt("price");
			tradeData.unit = resultSet.getInt("unit");
			tradeData.init();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public void addTradeData(TradeData tradeData){
		
		SQLiteManager.initDatabase(filePath);
			
		add(tradeData);
		
		SQLiteManager.closeDatabase();
	}
	
	private void add(TradeData tradeData){
		
		String sql = "insert into "+ tableName +" values(";
		sql += "NULL,";
		sql += String.valueOf(tradeData.tickerCode) +",";
		sql += String.valueOf(tradeData.date.get(Calendar.YEAR)) +",";
		sql += String.valueOf(tradeData.date.get(Calendar.MONTH)+1) +","; // CalendarŒ^‚ÍŒŽŽw’è‚ª0-11
		sql += String.valueOf(tradeData.date.get(Calendar.DATE)) +",";
		sql += (tradeData.isBuy ? "1" : "0") +",";
		sql += String.valueOf(tradeData.price) +",";
		sql += String.valueOf(tradeData.unit);
		sql += ");";
		
		//System.out.println(sql);
		
		SQLiteManager.update(sql);
	}
	
	public void deleteTradeData(int databaseID){
		
		SQLiteManager.initDatabase(filePath);
		
		String sql = "delete from "+ tableName +" where ID=";
		
		sql += String.valueOf(databaseID);
		
		sql += ";";
		
		//System.out.println(sql);
		
		SQLiteManager.update(sql);
		
		SQLiteManager.closeDatabase();
	}

}
