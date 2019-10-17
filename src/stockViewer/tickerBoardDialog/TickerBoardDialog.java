package stockViewer.tickerBoardDialog;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import stockViewer.Global;
import stockViewer.database.CSVFileChecker;
import stockViewer.database.DBAccessOfTickerDataTable;
import stockViewer.database.TableMakerForStockData;
import stockViewer.stockdata.TickerData;
import stockViewer.tickerBoardDialog.GenerateTable.TableCallback;
import stockViewer.tickerBoardDialog.MenuUtil.MenuCallback;

public class TickerBoardDialog extends Stage implements MenuCallback, TableCallback{
	
	public interface TickerBoardCallback{
		
		void selectedTicker(int tickerCode, boolean isFilterChart);
	}
	
	private TickerBoardCallback tickerBoardCallback;

	private MenuBar menuBar;
	private TableView<TickerData> tableView;
	private ArrayList<TickerData> dataList;
	
	public TickerBoardDialog(TickerBoardCallback callable, Window parentWnd) {
		
		tickerBoardCallback = callable;
		this.initOwner(parentWnd);
		
		menuBar = MenuUtil.generateMenu(this);
		tableView = GenerateTable.gen(this);
		dataList = new ArrayList<>();
		
		setScene();
	}
	
	private void setScene() {
		
		this.setTitle("TickerBoardDialog");
		this.setWidth(500);
		this.setHeight(300);
		this.setResizable(false);
		
		VBox root = new VBox();
		root.getChildren().addAll(menuBar, tableView);
		Scene scene = new Scene(root);
		this.setScene(scene);
	}
	
	public void setTable(){
		
		dataList.clear();	
		loadTickerBoard();
		
		ObservableList<TickerData> tableData = FXCollections.observableArrayList();
		tableData.setAll(dataList);
		tableView.itemsProperty().setValue(tableData);
	}
	
	private void loadTickerBoard() {
		
		//TableMakerForTickerBoard.makeTable("tickerBoard");
		DBAccessOfTickerDataTable da = new DBAccessOfTickerDataTable();
		da.setTickerBoardList(dataList);
	}

	@Override
	public void loadCSVFolder() {
		
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("SelectÅ@TickerÅ@CSVFolder");
		dc.setInitialDirectory(new File(Global.CSVdataDir));
		
		File file = dc.showDialog(this);
		setDBTableFromCSVFolder(file.getName());
		
		setTable();
	}
	
	private void setDBTableFromCSVFolder(String folderName) {
	
		String tableName = "table_"+folderName;
		
		TableMakerForStockData.makeTable(tableName);
		
		new CSVFileChecker().checkFolder(Global.CSVdataDir + folderName);
	}

	@Override
	public void tableClicked(MouseEvent event) {
		
		if(event.getButton() == MouseButton.PRIMARY){
			
			if(event.getClickCount() == 2) {
				
				selectTableRow();
				this.hide();
			}
		}
	}
	
	private void selectTableRow(){
		
		TickerData data = tableView.getSelectionModel().getSelectedItem();
		
		tickerBoardCallback.selectedTicker(data.tickerCode, false);
	}
}
