package stockViewer;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stockViewer.stockdata.ChartData;

public class MainSceneUtil {
	
	public static final int CanvasX = 1280;
	public static final int CanvasY = 640;
	public static final int SubCanvasY = 160;
	
	public static Canvas canvas = new Canvas(CanvasX, CanvasY);
	public static Canvas subCanvas = new Canvas(CanvasX, SubCanvasY);
	public static ScrollBar scrollBar = new ScrollBar();
	public static Tooltip tooltip = new Tooltip();
	public static ContextMenu contextMenu = new ContextMenu();
	public static TextField tradePriceText = new TextField(), tradeUnitText = new TextField();
	
	public static int tradePrice, tradeUnit;
	
	private static MainApp mainApp;
	
	public static void setScene(MainApp appArg, Stage stage){
		
		mainApp = appArg;
		
		initCanvas();
		initContextMenu();
		
		VBox box = new VBox();
		box.setPadding(new Insets(0));
		box.setSpacing(0);
		box.getChildren().add(MenuUtil.generateMenu(mainApp));
		box.getChildren().addAll(canvas, scrollBar, subCanvas);
		
		Pane root = box;
		Scene scene = new Scene(root);	
		stage.setScene(scene);
	}
	
	private static void initCanvas() {
		
		Font tipFont = new Font(16);
		tooltip.setFont(tipFont);
		Tooltip.install(canvas, tooltip);
		canvas.setOnMouseMoved(event -> mainApp.onCanvasMouseMoved(event));	
		canvas.setOnMouseClicked(event -> mainApp.onCanvasMouseClicked(event));
	}
	
	public static void initScrollBar(ChartData tickerData, int periodRange) {

		 scrollBar.setOrientation(Orientation.HORIZONTAL);
		 scrollBar.setMin(0);
		 scrollBar.setMax(tickerData.stockDataList.size()-periodRange);
		 scrollBar.setValue(0);
		 
		 scrollBar.valueProperty().addListener((ObservableValue<? extends Number> ov, 
		            Number old_val, Number new_val) -> {
		               
		            	mainApp.drawScreen();
		        });  
	}
	
	private static void initContextMenu(){
		
		MenuItem[] menuItem  = new MenuItem[3];
		for(int i=0; i<menuItem.length; i++) menuItem[i] = new MenuItem();
		
		String[] menuText = {"Buy", "Sell", "Cancel"};
		
		for(int i=0; i<menuItem.length; i++) menuItem[i].setText(menuText[i]);
		menuItem[0].setOnAction(e ->{doTrade(true);});
		menuItem[1].setOnAction(e ->{doTrade(false);});
		
		CustomMenuItem[] customMenuItem  = new CustomMenuItem[2];
		HBox hb = new HBox(), hb2 = new HBox();	
		
		tradePriceText.setPrefWidth(60);
		hb.setSpacing(10);
		hb.getChildren().addAll(new Text("Price :"), tradePriceText);
		customMenuItem[0] = new CustomMenuItem(hb);	
		tradeUnitText.setPrefWidth(60);
		hb2.setSpacing(10);
		hb2.getChildren().addAll(new Text("Unit  :"), tradeUnitText);
		customMenuItem[1] = new CustomMenuItem(hb2);
		
		contextMenu.getItems().addAll(menuItem);
		contextMenu.getItems().addAll(customMenuItem);
	}
	
	public static void showContextMenu(ChartData chartData, int period, double screenX, double screenY) {
		
		int endPrice = chartData.stockDataList.get(period).endPrice;
		tradePriceText.setText(String.valueOf(endPrice));
		tradeUnitText.setText("1");
		contextMenu.show(canvas, screenX, screenY);
	}
	
	private static void doTrade(boolean isBuy) {
		
		try {
			
		tradePrice = Integer.valueOf(tradePriceText.getText());
		tradeUnit = Integer.valueOf(tradeUnitText.getText());
		
		}catch(Exception e) {
		}
		mainApp.doTrade(isBuy, tradePrice, tradeUnit);
	}
}
