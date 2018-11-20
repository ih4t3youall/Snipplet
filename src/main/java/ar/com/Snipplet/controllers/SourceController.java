package ar.com.Snipplet.controllers;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import ar.com.commons.send.dto.SendDTO;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.domain.SourceObject;
import ar.com.Snipplet.domain.UserConfiguration;
import ar.com.Snipplet.services.ConfigurationService;
import ar.com.Snipplet.services.SnippletService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SourceController implements Initializable{


	@FXML
	private Label nombre;
	@FXML
	private Label mail;
	@FXML
	private Label texto;
	@FXML
	private Label asunto;
	@FXML
	private Label leido;
	@FXML
	private Button buttonLeft;
	@FXML
	private Button buttonRight;
	
	private int contador;
	
	private SourceObject[] fromServer;
	
	private ConfigurationService configurationService = (ConfigurationService) SpringContext.getContext()
			.getBean("configurationService");
	
	private SnippletService snippletService = (SnippletService) SpringContext.getContext().getBean("snippletService");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		contador =0;
		
		
		
		try {
			fromServer = getFromServer();
			snippletService.guardarCopiaSourceSistemas(fromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(fromServer.length > 0){
			
			mostrarMensaje(fromServer[0]);
			
		}else {
			
			JOptionPane.showMessageDialog(null, "No hay mensajes que mostrar.");
			
		}
		
		
		buttonLeft.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(contador != 0){
					contador --;
					mostrarMensaje(fromServer[contador]);
				}
				
			}
		});
		
		
		buttonRight.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(contador+1 != fromServer.length){
					contador ++;
					mostrarMensaje(fromServer[contador]);
				}
				
			}
		});
		
	}
	
	
	private void mostrarMensaje(SourceObject sourceObject ){
		
		nombre.setText(sourceObject.getNombre());
		mail.setText(sourceObject.getMail());
		texto.setText(sourceObject.getTexto());
		asunto.setText(sourceObject.getAsunto());
		leido.setText(sourceObject.getLeido());
	}
	
	private SourceObject[] getFromServer() throws JsonGenerationException, JsonMappingException, IOException{
		
		String url = configurationService.getUri() + "index.php/webservices/Snipplet_Webservice/source_sistemas/";
		
		SendDTO send = new SendDTO();
		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		send.setUsername(userConfiguration.getUsername());
		send.setPassword(userConfiguration.getPassword());
		
		
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();
		String writeValueAsString = mapper.writeValueAsString(send);
		System.out.println(writeValueAsString);
		RequestBody body = RequestBody.create(JSON, writeValueAsString);

		Request request = new Request.Builder().url(url).post(body).build();
		okhttp3.Response response = client.newCall(request).execute();

		@SuppressWarnings("unused")
		String responseBody = response.body().string();
		System.out.println(responseBody);
		SourceObject[] sourceObject = mapper.readValue(responseBody, SourceObject[].class);
//
		return sourceObject;
		
	}
	
	
}
