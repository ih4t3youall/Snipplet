package ar.com.Snipplet.services;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import ar.com.Snipplet.domain.FileConfiguration;
import ar.com.Snipplet.domain.UserConfiguration;
import ar.com.Snipplet.persistencia.Persistencia;
import ar.com.commons.send.airdrop.Pc;

public class ConfigurationService {

	private FileConfiguration fileConfiguration;

	private UserConfiguration userConfiguration;

	private Persistencia persistencia;

	public String getUri() {

		return fileConfiguration.getUri();

	}

	public void guardarIp(List<Pc> ips) {

		persistencia.guardarIp(ips);
	}

	public List<Pc> getIps(){
		return persistencia.getIps();
	}

	public String getPrefix() {

		return fileConfiguration.getPrefix();

	}

	public void cargarConfiguracion() throws ClassNotFoundException, IOException {
		FileConfiguration config = persistencia.getConfig();
		UserConfiguration userConfig = persistencia.getUserConfig();

		this.userConfiguration = userConfig;
		this.fileConfiguration = config;

	}

	public void cargarConfiguracionIp(){



	}
	public String getHomeDirectory(){
		return persistencia.getHomeDirectory();
	}

	public FileConfiguration getFileConfiguration() {
		return fileConfiguration;
	}

	public void setFileConfiguration(FileConfiguration fileConfiguration) {
		this.fileConfiguration = fileConfiguration;
	}

	public void cambiarHost(String nuevoHost) {

		fileConfiguration.setUri(nuevoHost);

		try {
			persistencia.saveNewConfiguration(fileConfiguration);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar la configuracion.");

			e.printStackTrace();
		}

	}

	public void cambiarPrefix(String newPrefix) {
		fileConfiguration.setPrefix(newPrefix);
		persistencia.setPrefix(newPrefix);

		try {
			persistencia.saveNewConfiguration(fileConfiguration);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar la configuracion.");

			e.printStackTrace();
		}

	}

	// TODO doing
	public void cambiarUsuario(UserConfiguration userConfiguration) {

		this.userConfiguration = userConfiguration;

		try {
			persistencia.saveNewUserConfiguration(userConfiguration);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al actualizar la configuracion.");

			e.printStackTrace();
		}

	}

	public UserConfiguration getUserConfiguration() {
		return userConfiguration;
	}

	public void setUserConfiguration(UserConfiguration userConfiguration) {
		this.userConfiguration = userConfiguration;
	}

	public Persistencia getPersistencia() {
		return persistencia;
	}

	public void setPersistencia(Persistencia persistencia) {
		this.persistencia = persistencia;
	}

}
