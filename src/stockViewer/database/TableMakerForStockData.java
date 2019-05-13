package stockViewer.database;

public class TableMakerForStockData 
					extends TableMakerForStockViewer{

	static {
	
		primaryKey = "ID";
	}
	
	static String[] fieldData ={
			
			"ID INTEGER",
			"year INT",
			"month INT",
			"date INT",
			"startPrice INT",
			"highPrice INT",
			"lowPrice INT",
			"endPrice INT",
			"amount INT"
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
