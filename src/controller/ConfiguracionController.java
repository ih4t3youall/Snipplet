package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import context.SpringContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ConfigurationService;

public class ConfiguracionController implements Initializable {

	
	
	@FXML
	private TextField textHost;
	@FXML
	private Button buttonHost;
	
	@FXML
	private TextField prefixText;
	
	@FXML
	private Button prefixButton;
	
	private ConfigurationService configurationService = (ConfigurationService) SpringContext.getContext().getBean("configurationService");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		cargarCampos();
		inicializarBotones();
		
		
		
		
		
		
	}

	private void cargarCampos() {
		textHost.setText(configurationService.getUri());
		prefixText.setText(configurationService.getPrefix());
		
		
	}

	private void inicializarBotones() {
	buttonHost.setOnAction(new EventHandler<ActionEvent>() {
		
		@Override
		public void handle(ActionEvent event) {
			
			String nuevoHost = textHost.getText();
			
			configurationService.cambiarHost(nuevoHost);
			JOptionPane.showMessageDialog(null, "actualizado con exito.");
			
		}
	});
	
	prefixButton.setOnAction(new EventHandler<ActionEvent>() {

		@Override
		public void handle(ActionEvent event) {
			
			String newPrefix = prefixText.getText();
			configurationService.cambiarPrefix(newPrefix);
			JOptionPane.showMessageDialog(null, "actualizado con exito.");
			
			
		}
	});
	}
	
	
	

}
