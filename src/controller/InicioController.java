package controller;

import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import context.SpringContext;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import services.AService;

public class InicioController implements Initializable {

	@FXML
	private ListView<String> fxmlListView;

	@FXML
	private Menu fxmlMenu;
	
	@FXML
	private Pane panelDerecho;
	
	

	private AService pcService = (AService) SpringContext.getContext()
			.getBean("AService");

	
	protected ObservableList<String> items;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("hasta aca anda");
		crearMenuItems();
		items = FXCollections.observableArrayList();
		fxmlListView.setItems(items);

		fxmlListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				Button button = new Button("boton");
				panelDerecho.getChildren().add(button);

			}
		});
		


//		ObservableList<MenuItem> items2 = fxmlMenu.getItems();
//		for (MenuItem menuItem : items2) {
//			menuItem.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
//				
//				@Override
//				public void handle(ActionEvent event) {
//					
//					String showInputDialog = JOptionPane.showInputDialog("Ingrese nombre categoria");
//					if(!showInputDialog.equals("")){
//					
//					items.add(showInputDialog);
//					}else {
//						
//						JOptionPane.showMessageDialog(null, "Error: no puede ser vacio.");
//						
//						
//					}
//					
//					
//				}
//			});
//		}

	}
	
	
	
	public void crearMenuItems(){
		
		
		MenuItem agregarCategoria = new MenuItem("Agregar Categoria");
		MenuItem eliminarCategoria = new MenuItem("Eliminar Categoria");
		fxmlMenu.getItems().add(agregarCategoria);
		fxmlMenu.getItems().add(eliminarCategoria);
		
		
		
		agregarCategoria.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String showInputDialog = JOptionPane.showInputDialog("Ingrese nombre categoria");
				if(!showInputDialog.equals("")){
				
				items.add(showInputDialog);
				}else {
					
					JOptionPane.showMessageDialog(null, "Error: no puede ser vacio.");
					
					
				}
				
				
			}
		});
		
		
		eliminarCategoria.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
			
				String selectedItem = fxmlListView.getSelectionModel().getSelectedItem();
				items.remove(selectedItem);
				
			}
		});
		
		
	}
	
	

}



