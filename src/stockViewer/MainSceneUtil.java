package stockViewer;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainSceneUtil {
	
	public static final int CanvasX = 1280;
	public static final int CanvasY = 640;
	
	public static Canvas canvas = new Canvas(CanvasX, CanvasY);
	
	private static MainApp mainApp;
	
	public static void setScene(MainApp appArg, Stage stage){
		
		mainApp = appArg;
		
		HBox box = new HBox();
		box.setPadding(new Insets(0));
		box.setSpacing(0);
		box.getChildren().add(canvas);
		
		Pane root = box;
		Scene scene = new Scene(root);	
		stage.setScene(scene);
	}

}
