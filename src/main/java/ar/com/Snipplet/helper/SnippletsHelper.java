package ar.com.Snipplet.helper;

import java.io.IOException;

import ar.com.Snipplet.controllers.PanelController;
import ar.com.Snipplet.domain.Snipplet;
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
	
	
	
	public Object[] eliminarRepetidos(Object[] vec, Object[] vec1) {

		int total = vec.length + vec1.length;
		Object[] arreglo = new Object[total];
		System.arraycopy(vec1, 0, arreglo, 0, vec1.length);
		System.arraycopy(vec, 0, arreglo, vec1.length, vec.length);

		Object[] aux = new Object[total];
		int contadorAux = 0;

		for (int i = 0; i < arreglo.length; i++) {

			boolean banderaEncontrado = false;

			for (int j = i + 1; j < arreglo.length; j++) {

				if (arreglo[i].toString().equals(arreglo[j].toString())) {
					banderaEncontrado = true;
					break;
				}

			}

			if (!banderaEncontrado) {
				aux[contadorAux] = arreglo[i];
				contadorAux++;

			}

		}

		return aux;

	}
	
	
	public  AnchorPane getPopulatedPanel(String categoria,Snipplet snipplet) throws IOException {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/views/panel.fxml"));
		AnchorPane load =(AnchorPane) loader.load();
 
		load.setId("numero"+contador);
		PanelController controller =(PanelController) loader.getController();
		controller.setId("numero"+contador);
		controller.setCategoria(categoria);
		controller.getTextArea().setText(snipplet.getContenido());
		controller.getTitulo().setText(snipplet.getTitulo());
		contador++;
		
		return load;
	}
	

}
