package application.application;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


	private Pane root;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/Inicio.fxml"));
			root = (Pane) loader.load();
			
			Scene scene = new Scene(root);
			
            primaryStage.setResizable(false);
            primaryStage.setTitle("Snipplet!");
            InputStream resourceAsStream = getClass().getResourceAsStream("../resources/icono.png");
            if(resourceAsStream != null){
            primaryStage.getIcons().add(new Image(resourceAsStream));
            }
            
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
	
	
	
	

}
