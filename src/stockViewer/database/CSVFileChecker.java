package stockViewer.database;

import java.io.File;
import java.util.List;

import stockViewer.StockData;

public class CSVFileChecker {

	
	public void checkFolder(String folderPath, DatabaseAccess da) {
		
		File file = new File(folderPath);
		
		if(file == null) return;
		
		File[] files = file.listFiles();
		for(File e: files){
			
			checkFile(e, da);
			//System.out.println(e.getPath());
		}
	}
	
	private void checkFile(File file, DatabaseAccess da) {
		
		List<StockData> list = null;

		list = new CSVFileReader().getStockDataList(file.getPath());
		
		for(StockData data : list) {
			
			if(!da.checkExistStockData(data)) da.addStockData(data);
		}
	}
}
