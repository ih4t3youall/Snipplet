package ar.com.Snipplet.application;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ar.com.Snipplet.controllers.InicioController;
import ar.com.Snipplet.handlers.MessageReceiverHandler;
import ar.com.Snipplet.listeners.InicioKeyHandler;
import ar.com.Snipplet.threads.UpdateIp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {


	private Pane root;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/views/Inicio.fxml"));

			root = (Pane) loader.load();
			InicioController controller = loader.getController();

			controller.setListener(primaryStage);
			
			Scene scene = new Scene(root);
			scene.setOnKeyPressed(new InicioKeyHandler(controller));

            primaryStage.setResizable(false);
            primaryStage.setTitle("Snipplet! v1.5");
            System.out.println("Snipplet version 1.5");
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});
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
