import java.io.File;

public class Main {

	public static void main(String[] args) {
		

		try {
			new CSVFileReader().getStockDataList("2127_2019.csv");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
