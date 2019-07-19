package stockViewer.trade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import stockViewer.MainSceneUtil;
import stockViewer.database.DBAccessOfTradeDataTable;
import stockViewer.database.TableMakerForTradeData;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.TickerData;
import stockViewer.trade.GenerateTable.ResultTableField;
import stockViewer.trade.GenerateTable.TableCallback;
import stockViewer.trade.MenuUtil.MenuCallback;
import stockViewer.trade.algorithm.TradingAlgoShell;

public class TradeDialog extends Stage implements MenuCallback, TableCallback{
	
	public static final double DIALOG_WIDTH = 500, DIALOG_HEIGHT = 780;
	public static final double TABLE_HEIGHT = 200, RESULT_TABLE_HEIGHT = 280;
	public static final double GRAPH_WIDTH = 450, GRAPH_HEIGHT = 200;
	
	public interface TradeCallback{
		
		void requestRedrawScreen();
	}
	
	private TradeCallback tradeCallback;

	private MenuBar menuBar;
	private TableView<TradeData> tableView;
	public TradeDataList tradeDataList = new TradeDataList();
	private TableView<ResultTableField> resultTable;
	private ResultData resultData = new ResultData();;
	
	private ContextMenu contextMenu = new ContextMenu();
	
	private Canvas graphCanvas = new Canvas(GRAPH_WIDTH, GRAPH_HEIGHT);
	private TradeResultGraph resultGraph = new TradeResultGraph(graphCanvas);
	
	private enum TradeMode{MANUAL, ALGORITHM};
	private TradeMode tradeMode;
	
	public TradeDialog(TradeCallback callable, Window parentWnd) {
		
		tradeCallback = callable;
		this.initOwner(parentWnd);
		
		menuBar = MenuUtil.generateMenu(this);
		tableView = GenerateTable.gen(this);
		resultTable = GenerateTable.gen(resultData);
		
		this.setOnHidden(event -> {tradeCallback.requestRedrawScreen();});
		
		setScene();
		initContextMenu();
	}
	
	public void open(int tickerCode) {
		
		tradeMode = TradeMode.MANUAL;
		DBAccessOfTradeDataTable dba = new DBAccessOfTradeDataTable();
		dba.setTradeDataList(tickerCode, tradeDataList);
		this.show();
		updateTable();
	}
	
	public void open(TradingAlgoShell algoShell) {
		
		tradeMode = TradeMode.ALGORITHM;
		tradeDataList = algoShell.tradeDataList;
		this.show();
		updateTable();
	}
	
	private void setScene() {
		
		this.setTitle("TradeDialog");
		this.setWidth(DIALOG_WIDTH);
		this.setHeight(DIALOG_HEIGHT);

		VBox root = new VBox();
		root.getChildren().addAll(menuBar, tableView, graphCanvas, resultTable);
		Scene scene = new Scene(root);
		this.setScene(scene);
	}
	
	private void initContextMenu(){
		
		MenuItem[] menuItem  = new MenuItem[1];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"delete"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->{deleteTradeData(tableView.getSelectionModel().getSelectedItem());});
	
		contextMenu.getItems().addAll(menuItem);
	}
	
	private void updateTable(){
		
		ObservableList<TradeData> tableData = FXCollections.observableArrayList();
		tableData.setAll(tradeDataList);
		tableView.itemsProperty().setValue(tableData);
		
		tradeCallback.requestRedrawScreen();
		resultGraph.refresh(tradeDataList);
		resultData.calc(resultGraph.clearingPointList);
		resultTable.refresh();
	}
	
	public void addTradeData(int tickerCode, Calendar date, boolean isBuy, int price, int unit) {
	
		TradeData tradeData = new TradeData(tickerCode, date, isBuy, price, unit);
		
		TableMakerForTradeData.makeTable("tradeRecord_Manual");
		DBAccessOfTradeDataTable dba = new DBAccessOfTradeDataTable();
		dba.addTradeData(tradeData);
		
		dba.setTradeDataList(tickerCode, tradeDataList);
		updateTable();
	}
	
	private void deleteTradeData(TradeData tradeData) {
		
		DBAccessOfTradeDataTable dba = new DBAccessOfTradeDataTable();
		dba.deleteTradeData(tradeData.databaseID);
		
		dba.setTradeDataList(tradeData.tickerCode, tradeDataList);
		updateTable();
	}

	@Override
	public void tableClicked(MouseEvent event) {
		
		if(event.getButton() == MouseButton.SECONDARY){
			
			if(event.getClickCount() == 1) {
				
				tableView.setContextMenu(contextMenu);
			}
		}
	}

}
