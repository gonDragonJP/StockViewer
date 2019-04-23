package stockViewer.database;


public class TableMakerForStockViewer extends SQLiteManager{
	
	static String dbPath = ".\\stockDatabase\\testStockDatabase.db";
	
	static String tableName;
	
	static String primaryKey;
	
	static String[] columnData;
	
	public static void make(){
		
		SQLiteManager instance = new SQLiteManager();
		
		instance.initDatabase(dbPath);
		String columnsDef = instance.getColumnsDef(columnData, primaryKey);
		instance.createTable(tableName, columnsDef);
		instance.closeDatabase();
	}
}
