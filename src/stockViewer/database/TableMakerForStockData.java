package stockViewer.database;

public class TableMakerForStockData 
					extends TableMakerForStockViewer{

	static {
	
		tableName = "TestData";
	
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
	
	public static void makeTable(){
		
		columnData = fieldData;
		
		make();
	}
}
