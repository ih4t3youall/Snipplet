package ar.com.Snipplet.serverconection;

import ar.com.Snipplet.domain.UserConfiguration;
import ar.com.Snipplet.persistencia.Persistencia;
import ar.com.Snipplet.services.ConfigurationService;
import ar.com.commons.send.dto.CategoriaDTO;
import ar.com.commons.send.dto.SendDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.*;
import java.io.IOException;

public class ServerConnection {

    private ConfigurationService configurationService ;


    private Persistencia persistencia ;


    public String send_text(String filename) throws IOException {

        String url = configurationService.getUri() + "guardarCategoria";
        CategoriaDTO recuperarGuardado = persistencia.recuperarGuardado(filename);
        UserConfiguration userConfiguration = configurationService.getUserConfiguration();
        SendDTO send = new SendDTO();
        send.setUsername(userConfiguration.getUsername());
        send.setPassword(userConfiguration.getPassword());
        send.setCategoriaDTO(recuperarGuardado);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        if (recuperarGuardado != null) {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            String writeValueAsString = mapper.writeValueAsString(send);
            System.out.println(writeValueAsString);
            RequestBody body = RequestBody.create(JSON, writeValueAsString);

            Request request = new Request.Builder().url(url).post(body).build();
            okhttp3.Response response = client.newCall(request).execute();

            String responseBody = response.body().string();
            JOptionPane.showMessageDialog(null, responseBody);
            return responseBody;
        } else {
            JOptionPane.showMessageDialog(null, "Este archivo no contiene snipplets!");
            return "";

        }
    }


    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setPersistencia(Persistencia persistencia) {
        this.persistencia = persistencia;
    }
}
