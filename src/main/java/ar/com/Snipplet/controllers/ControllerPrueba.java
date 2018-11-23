package ar.com.Snipplet.controllers;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.services.MessageService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.services.IpService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerPrueba implements Initializable {

    @FXML
    private Button enviar;
    @FXML
    private TextArea mensaje;

    @FXML
    private TextField ip;


    private MessageService messageService = (MessageService) SpringContext.getContext().getBean("messageService");
    private IpService ipService= (IpService ) SpringContext.getContext().getBean("ipService");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        enviar.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!(mensaje.getText().isEmpty())  && (!ip.getText().isEmpty())) {
                    Mensaje men= new Mensaje(ipService.obtenerIp());
                    men.setIpDestino(ip.getText());
                    men.setMensaje(mensaje.getText());
                    men.setComando("mensajePrompt");
                    messageService.sendMessage(men);

                }

            }
        });
    }
}
