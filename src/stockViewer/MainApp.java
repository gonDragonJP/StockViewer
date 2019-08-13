package stockViewer;
import java.util.ArrayList;
import java.util.List;

import com.quandl.api.java.QDataset;
import com.quandl.api.java.QuandlConnection;

import javafx.application.Application;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import stockViewer.MenuUtil.OnOrOff;
import stockViewer.MenuUtil.PeriodRange;
import stockViewer.MenuUtil.TradeMode;
import stockViewer.database.CSVFileChecker;
import stockViewer.database.CSVFileReader;
import stockViewer.database.DBAccessOfStockDataTable;
import stockViewer.database.TableMakerForStockData;
import stockViewer.filterChart.FilterChart;
import stockViewer.stockdata.StockData;
import stockViewer.subscreen.SubScreenDrawModule;
import stockViewer.subscreen.SubScreenDrawModule.Technical;
import stockViewer.stockdata.ChartData;
import stockViewer.stockdata.ChartData.ChartSpan;
import stockViewer.tickerBoardDialog.TickerBoardDialog;
import stockViewer.tickerBoardDialog.TickerBoardDialog.TickerBoardCallback;
import stockViewer.trade.TradeDialog;
import stockViewer.trade.TradeDialog.TradeCallback;
import stockViewer.trade.algorithm.Oshime_Buy_01;
import stockViewer.trade.algorithm.RandomTest;


public class MainApp extends Application implements MenuUtil.MenuCallback, TickerBoardCallback, TradeCallback{
	
	private static final String AppTitle ="StockViewer ver1.0";
	
	private static final int WinX = 1440;
	private static final int WinY = 720+160;
	
	private DrawModule drawModule;
	private SubScreenDrawModule subDrawModule;
	private ChartData chartData;
	
	private TickerBoardDialog tickerBoardDialog;
	private TradeDialog tradeDialog;
	
	private PeriodRange periodRange = PeriodRange._100;
	private boolean priceZoom = true;
	
	private int selectedPeriod;
	
	public static void main(String[] args){
		
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception{
		
		initStage(stage);
		
		drawModule = new DrawModule(this);
		subDrawModule = new SubScreenDrawModule(MainSceneUtil.subCanvas);
		tickerBoardDialog = new TickerBoardDialog(this, stage);
		tradeDialog = new TradeDialog(this, stage);	
	}
	
	private void initScreen() {
		
		MainSceneUtil.initScrollBar(chartData, periodRange.getRange());
		MainSceneUtil.setTitle(AppTitle + " - " + chartData.getChartTitle());
		drawModule.init(chartData);
		drawScreen();
	}
	
	public void drawScreen() {
		
		if(chartData==null) return;
		
		int startIndex = (int) MainSceneUtil.scrollBar.getValue();
		int endIndex = startIndex + periodRange.getRange() -1;
		
		drawModule.clearScreen();
		drawModule.drawScreen(chartData, startIndex, endIndex, priceZoom);
		
		if(tradeDialog.isShowing()) drawModule.drawTradeMarks(chartData, tradeDialog.tradeDataList);
	
		subDrawModule.clearScreen();
		subDrawModule.drawScreen(chartData, startIndex, endIndex);
	}
	
	public void onCanvasMouseMoved(MouseEvent event) {
		
		if(chartData==null) return;
		
		selectedPeriod = drawModule.getPeriodByXcoord(event.getX());		
		String data = chartData.getTooltipStrings(selectedPeriod);
		MainSceneUtil.tooltip.setText(data);
	}
	
	public void onCanvasMouseClicked(MouseEvent event) {
		
		if(event.getButton() == MouseButton.SECONDARY){
			
			if(event.getClickCount() == 1) {
				
				if(chartData==null) return;
				MainSceneUtil.showContextMenu(chartData, selectedPeriod, event.getScreenX(), event.getScreenY());
			}
		}
	}
	
	public void doTrade(boolean isBuy, int price, int unit) {
		
		if(chartData==null) return;
		
		tradeDialog.addTradeData(chartData.tickerData.tickerCode, chartData.getDate(selectedPeriod), isBuy, price, unit);
		
		//System.out.println(String.format("date %s - isBuy %s - price %d - unit %d"
		//		,chartData.getDateString(selectedPeriod), String.valueOf(isBuy), price , unit ));
	}
	
	private void initStage(Stage stage){
		
		MainSceneUtil.setScene(this, stage);
	
		stage.setTitle(AppTitle);
		stage.setWidth(WinX);
		stage.setHeight(WinY);
		stage.show();
	}

	@Override
	public void setPeriodRange(PeriodRange range) {
		
		periodRange = range;
		initScreen();
	}
	
	@Override
	public void setChartSpan(ChartSpan span) {
		
		chartData.selectChartSpan(span);
		initScreen();
	}

	@Override
	public void setPriceZoom(OnOrOff sw) {
		
		priceZoom = (sw == sw.On);
		drawScreen();
	}

	@Override
	public void openDatabase() {
		
		tickerBoardDialog.setTable();
		tickerBoardDialog.show();
	}
	
	@Override
	public void selectedTicker(int tickerCode, boolean isFilterChart) {
		
		chartData = getChartData(tickerCode);
		tradeDialog.hide();
		drawModule.setFilterChart(isFilterChart);
		initScreen();
	}
	
	private ChartData getChartData(int tickerCode) {
		
		ChartData chartData = new ChartData();	
		chartData.loadDB(tickerCode);
		return chartData;
	}

	@Override
	public void setGraph(CheckBox cb) {
		
		String cbName = cb.getText();
	    boolean sw = cb.isSelected();
	    
	    switch(cbName){
	    
	    case "05-SMA" : drawModule._5SMA_sw = cb.isSelected(); break;
	    case "13-SMA" : drawModule._13SMA_sw = cb.isSelected(); break;
	    case "25-SMA" : drawModule._25SMA_sw = cb.isSelected(); break;
	    
	    case "Envelope" : drawModule.envelope_sw = cb.isSelected(); break;
	    case "LarrysLine" : drawModule.LarrysLine_sw = cb.isSelected(); break;
	    case "RegressionLine" : drawModule.RegressionLine_sw = cb.isSelected(); break;
	    }
	    
	    drawScreen();
	}

	@Override
	public void openTrade(TradeMode tradeMode) {
		
		if(chartData == null) return;
		
		switch(tradeMode) {
		
		case Manual:
			tradeDialog.open(chartData.tickerData.tickerCode);
			break;
		case RandomTest:
			tradeDialog.open(new RandomTest(chartData));
			break;
		case Oshime_01:
			tradeDialog.open(new Oshime_Buy_01(chartData));
			break;
		}
	}

	@Override
	public void requestRedrawScreen() {
		
		drawScreen();
	}

	@Override
	public void setSubScreen(Technical technical) {
		
		subDrawModule.setShowingTechnical(technical);
		drawScreen();
	}

	@Override
	public void openFilterChart(int tickerCode) {
		
		selectedTicker(tickerCode, true);
	}

}
