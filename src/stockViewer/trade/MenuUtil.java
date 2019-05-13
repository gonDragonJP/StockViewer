package stockViewer.trade;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuUtil {
	
	public interface MenuCallback{
		
	}
	
	private static MenuCallback menuCallback;

	public static MenuBar generateMenu(MenuCallback callable){
		
		menuCallback = callable;
		MenuBar menuBar = new MenuBar();
		
		String[] menuNames = {"Database"};
		Menu[] menus = generateMenuArray(menuNames);
		menuBar.getMenus().addAll(menus);
		
		setDatabaseMenu(menus[0]);
	
		return menuBar;
	}
	
	private static void setDatabaseMenu(Menu menu){
		
		String[] menuItemNames = {"LoadCSVFolder"};
		MenuItem[] menuItems = generateMenuItemArray(menuItemNames);
		menu.getItems().addAll(menuItems);
		
		menuItems[0].setOnAction(event->{});
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
