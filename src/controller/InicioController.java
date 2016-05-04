package controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import context.SpringContext;
import dto.CategoriaDTO;
import helper.SnippletsHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.SnippletService;

public class InicioController implements Initializable {

	@FXML
	private ListView<String> fxmlListView;

	@FXML
	private Menu fxmlMenu;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private Button botonMas;

	@FXML
	private Button botonMenos;
	
	@FXML
	private Button refresh;

	private String id;

	private VBox vbox = (VBox) SpringContext.getContext().getBean("vbox");

	private SnippletService snippletService = (SnippletService) SpringContext.getContext().getBean("snippletService");

	private SnippletsHelper snippletHelper = (SnippletsHelper) SpringContext.getContext().getBean("snippletHelper");

	protected ObservableList<String> items;

	private void activarButtons() {
		botonMas.setDisable(false);
		botonMenos.setDisable(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println("hasta aca anda");
		snippletService.firstTime();
		configure();
		items = FXCollections.observableArrayList();
		fxmlListView.setItems(items);

//		snippletService.cargarArchivos();
//
//		for (CategoriaDTO categoria : snippletService.getCategorias()) {
//
//			items.add(categoria.getNombre());
//		}
		refreshList();

		fxmlListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				List<AnchorPane> panels = snippletService.loadSnippletsPorCategoria(newValue);
				activarButtons();
				int size = vbox.getChildren().size();

				for (int i = 0; i < size; i++) {
					vbox.getChildren().remove(0);
				}

				if (panels != null) {

					for (AnchorPane anchorPane : panels) {

						vbox.getChildren().add(0, anchorPane);
					}
				}

			}
		});

	}
	
	
	
	public void refreshList(){
		
		snippletService.cargarArchivos();
		removerItemsLista();
		for (CategoriaDTO categoria : snippletService.getCategorias()) {

			items.add(categoria.getNombre());
		}
		
	}
	
	
	private void removerItemsLista() {
		
		if(items.size() > 0){
			int maximo = items.size();
			for (int i = 0; i < maximo; i++) {
				items.remove(0);
			} 
		
			
			
		}
		
		
	}

	public void configure() {

		crearMenuItems();
		crearBotones();

		scrollPane.setContent(vbox);

	}

	private void crearBotones() {
		
		refresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
//				snippletService.cargarArchivos();

				refreshList();
//				for (CategoriaDTO categoria : snippletService.getCategorias()) {
//
//					items.add(categoria.getNombre());
//				}

			}
		});

		botonMas.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AnchorPane root = null;
				try {
					String categoria = fxmlListView.getSelectionModel().getSelectedItem();
					root = snippletHelper.getEmptyPanel(categoria);
				} catch (IOException e) {
					e.printStackTrace();
				}

				vbox.getChildren().add(0, root);

			}
		});

		botonMenos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String nombreCategoria = fxmlListView.getSelectionModel().getSelectedItem();
				items.remove(nombreCategoria);
				snippletService.deleteCategory(nombreCategoria);

			}

		});

	}

	public void crearMenuItems() {

		MenuItem agregarCategoria = new MenuItem("Agregar Categoria");
		MenuItem guardarEnLaNube = new MenuItem("Administrar nube");
		
		fxmlMenu.getItems().add(agregarCategoria);
		fxmlMenu.getItems().add(guardarEnLaNube);

		
		guardarEnLaNube.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				FXMLLoader loader = new FXMLLoader();
				 Stage secondaryStage = new Stage();
	            loader.setLocation(getClass().getResource("/views/ServerSyncro.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
				
				
				Scene scene = new Scene(root);
				secondaryStage.setResizable(false);
				secondaryStage.setScene(scene);
				secondaryStage.show();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error !");
					
					e.printStackTrace();
				}
				
			}
		});
		
		
		agregarCategoria.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String nuevaCategoria = JOptionPane.showInputDialog("Ingrese nombre categoria");
				if (!nuevaCategoria.equals("")) {

					snippletService.crearNuevoSnipplet(nuevaCategoria);
					boolean flag = true;
					for (String string : items) {
						if (string.equals(nuevaCategoria)) {
							flag = false;
						}

					}
					if (flag) {
						items.add(nuevaCategoria);
					} else {

						JOptionPane.showMessageDialog(null, "el nombre de categoria ya existe!");
					}

				} else {

					JOptionPane.showMessageDialog(null, "Error: no puede ser vacio.");

				}

			}
		});

		botonMenos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
