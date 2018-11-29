package ar.com.Snipplet.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ar.com.commons.send.dto.CategoriaDTO;
import ar.com.commons.send.dto.SendDTO;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.domain.UserConfiguration;
import ar.com.Snipplet.persistencia.Persistencia;
import ar.com.Snipplet.services.ConfigurationService;
import ar.com.Snipplet.services.SnippletService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SyncroController implements Initializable {

	private ConfigurationService configurationService = (ConfigurationService) SpringContext.getContext()
			.getBean("configurationService");
	@FXML
	private Button upload;
	@FXML
	private Button download;
	@FXML
	private Button listarServer;
	@FXML
	private Button botonArchivo;
	@FXML
	private ProgressIndicator cargando;
	@FXML
	private Button borrarServer;
	@FXML
	private Button borrar;

	@FXML
	private ListView<String> fxmlListaServer;

	protected ObservableList<String> items;

	private Persistencia persistencia = (Persistencia) SpringContext.getContext().getBean("persistencia");

	private SnippletService snippletService = (SnippletService) SpringContext.getContext().getBean("snippletService");

	private Scene scene;

	private InicioController inicioController;

	public void setScene(Scene scene) {
		this.scene = scene;

	}

	private void activarCargando() {
		scene.setCursor(Cursor.WAIT);

	}

	private void desactivarCargando() {

		scene.setCursor(Cursor.DEFAULT);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		cargando.setVisible(false);
		items = FXCollections.observableArrayList();
		fxmlListaServer.setItems(items);

		botonArchivo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				scene.setCursor(Cursor.WAIT);

				String[] listarDirectorio = snippletService.listarDirectorio();
				removerItemsLista();

				for (String string : listarDirectorio) {

					items.add(string);

				}
				download.setDisable(true);
				upload.setDisable(false);
				borrar.setDisable(true);
				desactivarCargando();
			}
		});

		upload.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				activarCargando();
				try {
					String filename = fxmlListaServer.getSelectionModel().getSelectedItem();
					send_text(filename);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				desactivarCargando();

			}
		});

		download.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				activarCargando();
				CategoriaDTO fromServer;
				try {

					String filename = fxmlListaServer.getSelectionModel().getSelectedItem();

					fromServer = getFromServer(filename);
					snippletService.actualizarCategoria(fromServer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				desactivarCargando();

			}
		});

		listarServer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				activarCargando();
				String[] listar_server = null;
				removerItemsLista();
				try {
					listar_server = listar_server();

					for (String string : listar_server) {
						items.add(string);
					}

					upload.setDisable(true);
					download.setDisable(false);
					borrar.setDisable(true);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				desactivarCargando();
			}

		});

		borrarServer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				activarCargando();
				String[] listar_server = null;
				removerItemsLista();
				try {
					listar_server = listar_server();
					for (String string : listar_server) {
						items.add(string);

					}
					upload.setDisable(true);
					download.setDisable(true);
					borrar.setDisable(false);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				desactivarCargando();

			}
		});

		borrar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String categoria = fxmlListaServer.getSelectionModel().getSelectedItem();
				try {
					borrarFromServer(categoria);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "error al elinimar la categoria");
					e.printStackTrace();
				}

			}
		});

	}

	public void borrarFromServer(String categoria) throws JsonGenerationException, JsonMappingException, IOException{
		
		
		String url = configurationService.getUri() + "deleteCategory";
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		categoriaDTO.setNombre(categoria);
		SendDTO send = new SendDTO();
		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		send.setUsername(userConfiguration.getUsername());
		send.setPassword(userConfiguration.getPassword());
		send.setCategoriaDTO(categoriaDTO);

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(send);
		System.out.println(writeValueAsString);
		RequestBody body = RequestBody.create(JSON, writeValueAsString);

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();

	}

	public CategoriaDTO getFromServer(String filename)
			throws JsonGenerationException, JsonMappingException, IOException {

		String url = configurationService.getUri() + "returnCategory";
		CategoriaDTO recuperarGuardado = new CategoriaDTO();
		recuperarGuardado.setNombre(filename);

		SendDTO send = new SendDTO();
		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		send.setUsername(userConfiguration.getUsername());
		send.setPassword(userConfiguration.getPassword());
		send.setCategoriaDTO(recuperarGuardado);

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(send);
		System.out.println(writeValueAsString);
		RequestBody body = RequestBody.create(JSON, writeValueAsString);

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();

		System.out.println(responseBody);
		SendDTO sendDTO = mapper.readValue(responseBody, SendDTO.class);

		return sendDTO.getCategoriaDTO();

	}

	public String send_text(String filename) throws IOException {

		String url = configurationService.getUri() + "guardarCategoria";
		CategoriaDTO recuperarGuardado = persistencia.recuperarGuardado(filename);
		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		SendDTO send = new SendDTO();
		send.setUsername(userConfiguration.getUsername());
		send.setPassword(userConfiguration.getPassword());
		send.setCategoriaDTO(recuperarGuardado);

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		if (recuperarGuardado != null) {
			OkHttpClient client = new OkHttpClient();
			ObjectMapper mapper = new ObjectMapper();
			String writeValueAsString = mapper.writeValueAsString(send);
			System.out.println(writeValueAsString);
			RequestBody body = RequestBody.create(JSON, writeValueAsString);

			Request request = new Request.Builder().url(url).post(body).build();
			okhttp3.Response response = client.newCall(request).execute();

			String responseBody = response.body().string();
			SendDTO sendDTO = mapper.readValue(responseBody,SendDTO.class);
			//falta eliminar
			snippletService.deleteCategory(sendDTO.getCategoriaDTO().getNombre());
			snippletService.eliminarCategoriaDeCache(sendDTO.getCategoriaDTO().getNombre());
			//snippletService.
			snippletService.actualizarCategoria(sendDTO.getCategoriaDTO());
			inicioController.refreshList();
			return responseBody;
		} else {
			JOptionPane.showMessageDialog(null, "Este archivo no contiene snipplets!");
			return "";
		}
	}

	public String[] listar_server() throws IOException {

		String url = configurationService.getUri() + "listarServer";

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		SendDTO send = new SendDTO();
		send.setUsername(userConfiguration.getUsername());
		send.setPassword(userConfiguration.getPassword());

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		RequestBody body = RequestBody.create(JSON, mapper.writeValueAsString(send));

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();

		String[] directorios = mapper.readValue(responseBody, String[].class);

		return directorios;

	}

	private void removerItemsLista() {

		if (items.size() > 0) {
			int maximo = items.size();
			for (int i = 0; i < maximo; i++) {
				items.remove(0);
			}

		}

	}

	public void setInicioController(InicioController inicioController) {
		this.inicioController = inicioController;
	}
}
