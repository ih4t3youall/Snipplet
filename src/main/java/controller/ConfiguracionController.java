package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import context.SpringContext;
import domain.UserConfiguration;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ConfigurationService;
import services.SnippletService;

public class ConfiguracionController implements Initializable {

	
	
	@FXML
	private TextField textHost;
	@FXML
	private Button buttonHost;
	
	@FXML
	private TextField prefixText;
	
	@FXML
	private Button prefixButton;
	
	@FXML
	private Button botonNombre;
	
	@FXML
	private TextField textoNombre;
	
	@FXML
	private TextField textoPassword;
	
	private ConfigurationService configurationService = (ConfigurationService) SpringContext.getContext().getBean("configurationService");
	
	private SnippletService snippletService = (SnippletService) SpringContext.getContext().getBean("snippletService");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		cargarCampos();
		inicializarBotones();
		
		
		
		
		
		
	}

	private void cargarCampos() {
		textHost.setText(configurationService.getUri());
		prefixText.setText(configurationService.getPrefix());
		UserConfiguration userConfiguration = configurationService.getUserConfiguration();
		
		if(userConfiguration != null){
			String username = userConfiguration.getUsername();
			String password = userConfiguration.getPassword();
			textoNombre.setText(username);
			textoPassword.setText(password);
			
		}
		
	}

	private void inicializarBotones() {
		
		
		botonNombre.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				
				String nombre = textoNombre.getText();
				UserConfiguration userConfiguration = new UserConfiguration();
				userConfiguration.setUsername(nombre);
				userConfiguration.setPassword(textoPassword.getText());
				configurationService.cambiarUsuario(userConfiguration);
				JOptionPane.showMessageDialog(null, "Actualizado con exito.");
				
				
				
				
			}
		});
		
	buttonHost.setOnAction(new EventHandler<ActionEvent>() {
		
		@Override
		public void handle(ActionEvent event) {
			
			String nuevoHost = textHost.getText();
			
			configurationService.cambiarHost(nuevoHost);
			JOptionPane.showMessageDialog(null, "Actualizado con exito.");
			
		}
	});
	
	prefixButton.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			
			String newPrefix = prefixText.getText();
			snippletService.crearNuevaCarpeta(newPrefix);
			configurationService.cambiarPrefix(newPrefix);
			JOptionPane.showMessageDialog(null, "Actualizado con exito.");
			
			
		}
	});
	}
	
	
	

}
