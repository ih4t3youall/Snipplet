package ar.com.Snipplet.handlers;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.services.MessageService;
import ar.com.commons.send.airdrop.Constantes;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;
import ar.com.commons.send.services.IpService;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageReceiverHandler implements Runnable{



    private IpService ipService = (IpService) SpringContext.getContext().getBean("ipService");
    private MessageService messageService = (MessageService) SpringContext.getContext().getBean("messageService");
    @Override
    public void run() {

        while (true) {
            ServerSocket socket = null;

            try {
                socket = new ServerSocket(Constantes.PUERTO);
            } catch (IOException e) {
                String error = "Error al cear el socket";
                System.out.println(error);
            }
            System.out.println("Esperando envio....");

            try {
                Socket cliente = socket.accept();

                String ipOtroCliente = cliente.getInetAddress()
                        .getHostAddress();
                System.out.println("Conectado con cliente de " + ipOtroCliente);

                cliente.setSoLinger(true, 10);

                ObjectInputStream buffer = new ObjectInputStream(
                        cliente.getInputStream());

                Mensaje mensaje = (Mensaje) buffer.readObject();

                if (mensaje.getComando().equals("who")) {

                    Pc pcLocal = ipService.obtenerIp();
                    Mensaje mensajeRespuesta = new Mensaje(pcLocal);
                    mensajeRespuesta.setIpDestino(ipOtroCliente);
                    mensajeRespuesta.setComando("autenticar");
                    mensajeRespuesta.setIpDestino(ipOtroCliente);
                    messageService.sendMessage(mensajeRespuesta);

                }




                if (mensaje.getComando().equals("mensajePrompt")) {

                    System.out.println(mensaje.getMensaje());

                    }


            } catch (Exception e) {

                String error = "Error con el serversocket en el puerto : "
                        + Constantes.PUERTO;
                System.out.println(error);

            } finally {

                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.err.println("Error al cerrar el serversocket.");

                }

            }
        }

    }
}
