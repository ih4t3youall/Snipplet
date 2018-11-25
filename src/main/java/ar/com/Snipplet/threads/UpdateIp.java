package ar.com.Snipplet.threads;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.services.MessageService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lequerica on 11/25/18.
 */
public class UpdateIp implements Runnable {
    public static final String IP_SERVER="192.168.0.1";
    private MessageService messageService = (MessageService) SpringContext.getContext().getBean("messageService");

    @Override
    public void run() {
        String localHost = "";
        try {
            localHost = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Mensaje mensaje  = new Mensaje(new Pc(IP_SERVER));
        mensaje.setIpDestino(IP_SERVER);
        mensaje.setComando("updateIp");
        messageService.sendMessage(mensaje);


    }
}

