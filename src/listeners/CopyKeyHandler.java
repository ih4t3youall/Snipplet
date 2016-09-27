package listeners;

import controller.PanelController;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class CopyKeyHandler implements EventHandler<KeyEvent>{

	
	private PanelController panelCOntroller;
	public CopyKeyHandler(PanelController controller) {
		this.panelCOntroller = controller;
	}

	@Override
	public void handle(KeyEvent key) {
	System.out.println(key.getCode());
		
		if(key.getCode().toString().equals("F4"))
			System.out.println("estoy copiando");
		
		
	}

	
	
}
