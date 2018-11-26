package ar.com.Snipplet.services;

import ar.com.Snipplet.threads.UpdateIp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PingIpService {
    ScheduledExecutorService executor ;
    private ConfigurationService configurationService;

    public PingIpService(){

    }


    public void startPing(){

        Runnable task = new UpdateIp(configurationService.getUri()+"actualizarIp");
        int initialDelay = 0;
        int period = 30;
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MINUTES);

    }
    public void stopPingServer(){

        executor.shutdownNow();
    }
    public void changeAddresAndRestart(String nuevoHost){

        Runnable task = new UpdateIp(nuevoHost+"actualizarIp");
        int initialDelay = 0;
        int period = 30;
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MINUTES);


    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
