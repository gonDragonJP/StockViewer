package stockViewer.database;

public class TableMakerForTradeData extends TableMakerForStockViewer{

	static {
		
		primaryKey = "ID";
	}
	
	static String[] fieldData ={
			
			"ID INTEGER",
			"tickerCode INT",
			"year INT",
			"month INT",
			"date INT",
			"isBuy BOOLEAN",
			"price INT",
			"unit INT"
	};
	
//	public static void main(String[] args) {
//		
//		columnData = fieldData;
//		
//		make();
//	}
	
	public static void makeTable(String name){
		
		tableName = name;
		columnData = fieldData;
		
		make();
	}
}
