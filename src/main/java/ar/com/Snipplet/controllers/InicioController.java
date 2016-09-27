package ar.com.Snipplet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.domain.Snipplet;
import ar.com.Snipplet.dto.CategoriaDTO;
import ar.com.Snipplet.helper.SnippletsHelper;
import ar.com.Snipplet.services.SnippletService;
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
	private Button botonBuscar;

	@FXML
	private Button refresh;
	
	@FXML
	private Button botonReset;

	@FXML
	private Menu fxmlEdit;
	

	private String id;

	private VBox vbox = (VBox) SpringContext.getContext().getBean("vbox");

	private SnippletService snippletService = (SnippletService) SpringContext
			.getContext().getBean("snippletService");

	private SnippletsHelper snippletHelper = (SnippletsHelper) SpringContext
			.getContext().getBean("snippletHelper");

	protected ObservableList<String> items;

	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Started up!");
		snippletService.firstTime();
		configure();
		items = FXCollections.observableArrayList();
		fxmlListView.setItems(items);
		
		refreshList();
		
		

		fxmlListView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> observable,	String oldValue, String newValue) {
						cargarPorCategoria(newValue);

					}
				});

	}

	public void refreshList() {
		//scene.setOnKeyPressed(new InicioKeyHandler(this));
		snippletService.cargarArchivos();
		removerItemsLista();
		for (CategoriaDTO categoria : snippletService.getCategorias()) {

			items.add(categoria.getNombre());
		}

	}
	
	public void requestFocus(){
		fxmlListView.requestFocus();
	}

	private void removerItemsLista() {

		if (items.size() > 0) {
			int maximo = items.size();
			for (int i = 0; i < maximo; i++) {
				items.remove(0);
			}

		}

	}

	public void configure() {

		crearMenuItems();
		crearBotones();
		snippletService.config();

		scrollPane.setContent(vbox);

	}

	private void crearBotones() {

		refresh.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				refreshList();

			}
		});

		botonMas.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AnchorPane root = null;
				try {
					String categoria = fxmlListView.getSelectionModel()
							.getSelectedItem();
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

				String nombreCategoria = fxmlListView.getSelectionModel()
						.getSelectedItem();
				items.remove(nombreCategoria);
				snippletService.deleteCategory(nombreCategoria);

			}

		});

	}

	public void crearMenuItems() {

		MenuItem agregarCategoria = new MenuItem("Agregar Categoria");
		MenuItem guardarEnLaNube = new MenuItem("Administrar nube");
		MenuItem configuracion = new MenuItem("Configuracion");

		fxmlMenu.getItems().add(agregarCategoria);
		fxmlMenu.getItems().add(guardarEnLaNube);
		fxmlMenu.getItems().add(configuracion);


		configuracion.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FXMLLoader loader = new FXMLLoader();
				Stage secondaryStage = new Stage();
				loader.setLocation(getClass().getResource(
						"/views/Configuracion.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();

					Scene scene = new Scene(root);
					secondaryStage.setResizable(false);
					secondaryStage.setTitle("Configuracion");
					secondaryStage.setScene(scene);
					secondaryStage.show();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error !");

					e.printStackTrace();
				}

			}
		});

		guardarEnLaNube.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FXMLLoader loader = new FXMLLoader();
				Stage secondaryStage = new Stage();
				loader.setLocation(getClass().getResource(
						"/views/ServerSyncro.fxml"));
				AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
					SyncroController controller = (SyncroController) loader
							.getController();
					Scene scene = new Scene(root);
					controller.setScene(scene);

					secondaryStage.setResizable(false);
					secondaryStage.setTitle("Nube!");
					secondaryStage.setScene(scene);
					secondaryStage.show();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error !");

					e.printStackTrace();
				}

			}
		});

		agregarCategoria
				.setOnAction(new javafx.event.EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {

						String nuevaCategoria = JOptionPane
								.showInputDialog("Ingrese nombre categoria");
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

								JOptionPane.showMessageDialog(null,
										"el nombre de categoria ya existe!");
							}

						} else {

							JOptionPane.showMessageDialog(null,
									"Error: no puede ser vacio.");

						}

					}
				});

		botonBuscar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
			search();

			}
		});
		
		botonReset.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				String categoria = fxmlListView.getSelectionModel().getSelectedItem();
				
				cargarPorCategoria(categoria);

			}
		});

	}
	
	//es public porque tambien accede desde el keylistener
	public void search(){
		String palabraABuscar = JOptionPane.showInputDialog("Buscar:");
		if(palabraABuscar != null){
		String categoria = fxmlListView.getSelectionModel().getSelectedItem();
		List<Snipplet> buscarEnCategorias = snippletService.searchAll(palabraABuscar);
		mostrarSnippletsDeBusqueda(buscarEnCategorias,categoria);
		}
	}
	
	
	private void cargarPorCategoria(String categoria){
		List<AnchorPane> panels = snippletService.loadSnippletsPorCategoria(categoria);
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

	private void mostrarSnippletsDeBusqueda(List<Snipplet> snipplets,String categoria) {

		
		List<AnchorPane> panels = snippletService.loadSnipplets(snipplets,categoria);
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

	private void activarButtons() {
		botonMas.setDisable(false);
		botonMenos.setDisable(false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setListener(Stage primaryStage) {
		this.stage = primaryStage;
		
	}


}