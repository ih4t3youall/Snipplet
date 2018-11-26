package ar.com.Snipplet.threads;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.services.MessageService;
import ar.com.commons.send.airdrop.Mensaje;
import ar.com.commons.send.airdrop.Pc;
import ar.com.commons.send.dto.IpDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by lequerica on 11/25/18.
 */
public class UpdateIp implements Runnable {
    public String ipServer;
    private MessageService messageService = (MessageService) SpringContext.getContext().getBean("messageService");

    public UpdateIp(String ip){
        this.ipServer = ip;

    }
    public void setIp(String ip){
        ipServer = ip;
    }
    @Override
    public void run() {
        InetAddress localHost = null;

        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        IpDTO ipDTO = new IpDTO();
        ipDTO.setIp(localHost.getHostAddress());
        ipDTO.setNombrePc(localHost.getHostName());
        try {
            sendToServer(ipDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendToServer(IpDTO ipDTO ) throws IOException {

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String writeValueAsString = mapper.writeValueAsString(ipDTO);
        System.out.println(writeValueAsString);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, writeValueAsString);
        Request request = new Request.Builder().url(ipServer).post(body).build();
        okhttp3.Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);


    }
}

