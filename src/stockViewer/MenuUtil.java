package stockViewer;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;

public class MenuUtil {
	
	public enum PeriodRange{
		_50(50), _100(100), _200(200), _300(300), _500(500);
		
		int range;
		
		PeriodRange(int range) {this.range = range;}
		
		public int getRange() {return range;}
	}
	
	public enum OnOrOff{
		On, Off
	}
	
	public interface MenuCallback{
	
		void openDatabase();
		
		void setPeriodRange(PeriodRange range);
		void setPriceZoom(OnOrOff sw);
		
		void setGraph(CheckBox cb);
		
		void openTrade();
	}
	
	private static MenuCallback menuCallback;

	public static MenuBar generateMenu(MenuCallback callable){
		
		menuCallback = callable;
		MenuBar menuBar = new MenuBar();
		
		String[] menuNames = {"Database","Scope","TrendLine","Trade"};
		Menu[] menus = generateMenuArray(menuNames);
		menuBar.getMenus().addAll(menus);
		
		setDatabaseMenu(menus[0]);
		setScopeMenu(menus[1]);
		setTrendLineMenu(menus[2]);
		setTradeMenu(menus[3]);
	
		return menuBar;
	}
	
	private static void setDatabaseMenu(Menu menu){
		
		String[] menuItemNames = {"Open Database"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->{menuCallback.openDatabase();});
	}
	
	private static void setScopeMenu(Menu menu){
		
		String[] menuNames = {"ShowingPeriodRange", "PriceZoom"};
		Menu[] menus = generateMenuArray(menuNames);
		menu.getItems().addAll(menus);
		
		setPeriodRangeMenu(menus[0]);
		setPriceZoomMenu(menus[1]);
	}
	
	private static void setPeriodRangeMenu(Menu menu){
		
		MenuItem[] menuItems = generateMenuItemArray(PeriodRange.values());
		menu.getItems().addAll(menuItems);
		
		for(PeriodRange e: PeriodRange.values()){
			
			menuItems[e.ordinal()].setOnAction(event -> {menuCallback.setPeriodRange(e);});
		}
	}
	
	private static void setPriceZoomMenu(Menu menu){
		
		MenuItem[] menuItems = generateMenuItemArray(OnOrOff.values());
		menu.getItems().addAll(menuItems);
		
		for(OnOrOff e: OnOrOff.values()){
			
			menuItems[e.ordinal()].setOnAction(event -> {menuCallback.setPriceZoom(e);});
		}
	}
	
	private static void setTradeMenu(Menu menu){
		
		String[] menuItemNames = {"Open Trade"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->{menuCallback.openTrade();});
	}
	
	private static void setTrendLineMenu(Menu menu){
		
		String[] menuItemNames = {"05-SMA", "13-SMA", "25-SMA", "Envelope"};
		
		for(int i=0; i<menuItemNames.length; i++){
			
			CheckBox cb = new CheckBox(menuItemNames[i]);
			Paint paint = Paint.valueOf("#000000");
			cb.setTextFill(paint); // textFillセットしないとマウスオーバーするまで見えないバグ？
			
			CustomMenuItem cmi = new CustomMenuItem(cb);
			cmi.setOnAction(event -> {menuCallback.setGraph(cb);});
			menu.getItems().add(cmi);
		}
	}
	
	private static Menu[] generateMenuArray(String[] menuNames){
		
		Menu[] menus = new Menu[menuNames.length];
		for(int i=0; i<menus.length; i++){
			menus[i] = new Menu(menuNames[i]);
		}
		return menus;
	}
	
	private static MenuItem[] generateMenuItemArray(Object[] menuItemEnums){
		
		MenuItem[] menuItems = new MenuItem[menuItemEnums.length];
		for(int i=0; i<menuItems.length; i++){
			menuItems[i] = new MenuItem(menuItemEnums[i].toString());
		}
		return menuItems;
	}
}
