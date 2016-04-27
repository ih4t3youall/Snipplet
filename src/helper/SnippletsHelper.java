package helper;

import java.io.IOException;

import controller.PanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;



public class SnippletsHelper {

	
	int contador = 0;
	
	public  AnchorPane getEmptyPanel(String categoria) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/panel.fxml"));
		AnchorPane load =(AnchorPane) loader.load();
 
		load.setId("numero"+contador);
		PanelController controller =(PanelController) loader.getController();
		controller.setId("numero"+contador);
		controller.setCategoria(categoria);
		contador++;
		
		return load;
	}
	
	
	public  AnchorPane getPopulatedPanel(String categoria,String snipplet) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/panel.fxml"));
		AnchorPane load =(AnchorPane) loader.load();
 
		load.setId("numero"+contador);
		PanelController controller =(PanelController) loader.getController();
		controller.setId("numero"+contador);
		controller.setCategoria(categoria);
		controller.getTextArea().setText(snipplet);
		contador++;
		
		return load;
	}
	

}
