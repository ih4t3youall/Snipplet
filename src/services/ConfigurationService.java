package services;

import java.io.IOException;

import javax.swing.JOptionPane;

import domain.FileConfiguration;
import persistencia.Persistencia;

public class ConfigurationService {

	private FileConfiguration fileConfiguration;

	private Persistencia persistencia;

	public String getUri() {

		return fileConfiguration.getUri();

	}

	public String getPrefix() {

		return fileConfiguration.getPrefix();

	}

	public void cargarConfiguracion() throws ClassNotFoundException, IOException {
		FileConfiguration config = persistencia.getConfig();
		
		this.fileConfiguration = config;

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
	
	
	

	public Persistencia getPersistencia() {
		return persistencia;
	}

	public void setPersistencia(Persistencia persistencia) {
		this.persistencia = persistencia;
	}



}
