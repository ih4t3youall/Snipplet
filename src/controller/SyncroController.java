package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import context.SpringContext;
import dto.CategoriaDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import persistencia.Persistencia;
import services.SnippletService;

public class SyncroController implements Initializable {

//	String uri = "http://localhost:83/sourcesistemas/index.php/webservices/Snipplet_Webservice/";
	String uri = "http://www.sourcesistemas.com.ar/index.php/webservices/Snipplet_Webservice/";

	@FXML
	private Button upload;
	@FXML
	private Button download;
	@FXML
	private Button listarServer;
	@FXML
	private Button botonArchivo;
	
	@FXML
	private ListView<String> fxmlListaServer;
	
	protected ObservableList<String> items;

	private Persistencia persistencia = (Persistencia) SpringContext.getContext().getBean("persistencia");
	
	private SnippletService snippletService =  (SnippletService) SpringContext.getContext().getBean("snippletService");
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		items = FXCollections.observableArrayList();
		fxmlListaServer.setItems(items);
		
		
		
		botonArchivo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
			String[] listarDirectorio = snippletService.listarDirectorio();
			removerItemsLista();
			
			
			for (String string : listarDirectorio) {
				
				items.add(string);
				
			}
			download.setDisable(true);
			upload.setDisable(false);
			
				
			}
		});
		
		
		upload.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String send_text = "";
				try {
					String filename = fxmlListaServer.getSelectionModel().getSelectedItem();
					send_text = send_text(filename);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				textArea.setText("");
//				textArea.setText(send_text);

			}
		});

		download.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {

				CategoriaDTO fromServer;
				try {
					
					
					String filename = fxmlListaServer.getSelectionModel().getSelectedItem();
					
					fromServer = getFromServer(filename);
					snippletService.actualizarCategoria(fromServer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		listarServer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				
				String[] listar_server = null;
				removerItemsLista();
				try {
					listar_server=	listar_server();
					
				
				for (String string : listar_server) {
					items.add(string);
					
					
				}
				upload.setDisable(true);
				download.setDisable(false);
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			
		});
		
		
		
		
		
	}

	
	
	public CategoriaDTO getFromServer(String filename)
			throws JsonGenerationException, JsonMappingException, IOException {

		String url = uri + "download/";
		CategoriaDTO recuperarGuardado = new CategoriaDTO();
		recuperarGuardado.setNombre(filename);

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(recuperarGuardado);
		System.out.println(writeValueAsString);
		RequestBody body = RequestBody.create(JSON, writeValueAsString);

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();

		CategoriaDTO categoriaDTO = mapper.readValue(responseBody, CategoriaDTO.class);

		return categoriaDTO;

	}

	public String send_text(String filename) throws IOException {

		String url = uri + "guardar_archivo/";
		CategoriaDTO recuperarGuardado = persistencia.recuperarGuardado(filename);

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(recuperarGuardado);
		System.out.println(writeValueAsString);
		RequestBody body = RequestBody.create(JSON, writeValueAsString);

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();


		return responseBody;
	}

	public String[] listar_server() throws IOException {

		String url = uri + "listar_archivos/";

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		RequestBody body = RequestBody.create(JSON, "");

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		String responseBody = response.body().string();
		
		 String[] directorios = mapper.readValue(responseBody, String[].class);
		
		return directorios;

	}
	
	private void removerItemsLista() {
		
		if(items.size() > 0){
			int maximo = items.size();
			for (int i = 0; i < maximo; i++) {
				items.remove(0);
			} 
		
			
			
		}
		
		
	}

}
