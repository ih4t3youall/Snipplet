package ar.com.Snipplet.services;

import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.services.EnviarMensaje;

public class MessageService {


    public MessageService(){
    }


    Thread t = new Thread();
    EnviarMensaje enviarMensaje ;
    public void sendMessage(Mensaje mensaje){
        t = new Thread( new EnviarMensaje(mensaje));
        t.start();
    }


}
