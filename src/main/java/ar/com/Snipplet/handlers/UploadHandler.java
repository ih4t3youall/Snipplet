package ar.com.Snipplet.handlers;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.serverconection.ServerConnection;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

import java.io.IOException;

public class UploadHandler implements EventHandler {
    private ListView<String> fxmlListaServer;
    private ServerConnection serverConnection = (ServerConnection) SpringContext.getContext().getBean("serverConnection");
    public UploadHandler(ListView<String> aFxmlListaServer){
        fxmlListaServer= aFxmlListaServer;
    }

    @Override
    public void handle(Event event) {
        //activarCargando();
        try {
            String filename = fxmlListaServer.getSelectionModel().getSelectedItem();
            serverConnection.send_text(filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //desactivarCargando();
    }
}
