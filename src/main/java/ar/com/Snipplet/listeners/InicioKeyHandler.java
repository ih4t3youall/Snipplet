package ar.com.Snipplet.listeners;

import ar.com.Snipplet.controllers.InicioController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class InicioKeyHandler implements EventHandler<KeyEvent> {

	private InicioController inicioController;
	
	public InicioKeyHandler(InicioController inicioController) {
		this.inicioController = inicioController;
	}

	@Override
	public void handle(KeyEvent key) {
		
		
		if(key.getCode().toString().equals("F3")){
			inicioController.search();
			inicioController.requestFocus();
		}
		
		if(key.getCode().toString().equals("F5")){
			inicioController.refreshList();

		}
		
		
	}

}
