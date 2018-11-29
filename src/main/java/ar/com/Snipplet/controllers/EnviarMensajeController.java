package ar.com.Snipplet.controllers;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.services.MessageService;
import ar.com.Snipplet.services.SnippletService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;
import ar.com.commons.send.services.IpService;
import ar.com.commons.send.socket.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EnviarMensajeController implements Initializable {

    @FXML
    private Button enviar;
    @FXML
    private TextArea mensaje;

    @FXML
    private TextField ip;

    @FXML
    private ComboBox comboIps;

    @FXML
    private TextField nombrePc;

    @FXML
    private Button guardar;

    @FXML
    private Button borrar;

    @FXML
    private Button enviarArchivo;

    private MessageService messageService = (MessageService) SpringContext.getContext().getBean("messageService");
    private IpService ipService= (IpService ) SpringContext.getContext().getBean("ipService");
    private SnippletService snippletService  = (SnippletService) SpringContext.getContext().getBean("snippletService");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Pc> ips = snippletService.getIp();

        comboIps.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Pc pc =(Pc) comboIps.getSelectionModel().getSelectedItem();
                if(pc != null) {
                    nombrePc.setText(pc.getNombreEquipo());
                    ip.setText(pc.getIp());
                }

            }
        });
        if(ips != null) {
            //comboIps.getItems().addAll(ips);
            comboIps.getItems().addAll(ips);
        }

        enviarArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                Pc pc = (Pc)comboIps.getSelectionModel().getSelectedItem();

                if (pc.getIp() != null) {

                    int result = fileChooser.showOpenDialog(null);
                    if (result == 0) {
                        String selectedFile = fileChooser.getSelectedFile().toString();
                        System.out.println(selectedFile);
                        Client client = new Client(selectedFile, pc.getIp());
                        Thread thread = new Thread(client);
                        thread.start();

                    }
                }else{
                    JOptionPane.showMessageDialog(null,"url invalida");
                }
            }
        });

        guardar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!(nombrePc.getText().isEmpty()) && !(ip.getText().isEmpty())){

                    Pc pcCombo = (Pc) comboIps.getSelectionModel().getSelectedItem();

                    if(pcCombo != null && pcCombo.getNombreEquipo().equals(nombrePc.getText())) {
                        pcCombo.setIp(ip.getText());
                        snippletService.updateIp(pcCombo);
                        comboIps.getSelectionModel().select(pcCombo);
                    }else{
                        Pc pc = new Pc(ip.getText());
                        pc.setNombreEquipo(nombrePc.getText());
                        comboIps.getItems().add(pc);
                        comboIps.getSelectionModel().select(pc);
                        snippletService.addIp(pc);
                    }
                }
            }
        });

        borrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pc pc = (Pc) comboIps.getSelectionModel().getSelectedItem();
                comboIps.getItems().remove(pc);
                nombrePc.setText("");
                ip.setText("");
                snippletService.borrarIp(pc);
                Pc pcSelected =(Pc) comboIps.getSelectionModel().getSelectedItem();
               if(pcSelected != null) {
                   ip.setText(pcSelected.getIp());
                   nombrePc.setText(pcSelected.getNombreEquipo());
               }
            }
        });

        enviar.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (!(mensaje.getText().isEmpty())  && (!ip.getText().isEmpty())) {

                    Pc pc = (Pc) comboIps.getSelectionModel().getSelectedItem();
                    Mensaje men= new Mensaje(pc);
                    men.setIpDestino(pc.getIp());
                    men.setMensaje(mensaje.getText());
                    men.setComando("mensajePrompt");
                    messageService.sendMessage(men);

                }

            }
        });
    }
}
