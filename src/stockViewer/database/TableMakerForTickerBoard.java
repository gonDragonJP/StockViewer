package stockViewer.database;

public class TableMakerForTickerBoard 
				extends TableMakerForStockViewer{

	static {
		
		primaryKey = "ID";
	}
	
	static String[] fieldData ={
			
			"ID INTEGER",
			"tickerCode INT",
			"marketName VERCHAR(10)",
			"stockName VERCHAR(50)"
	};
	
	public static void makeTable(String name){
		
		tableName = name;
		columnData = fieldData;
		
		make();
	}
}
