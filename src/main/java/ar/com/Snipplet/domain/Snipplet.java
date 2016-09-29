package ar.com.Snipplet.domain;
import java.io.Serializable;

public class Snipplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String titulo;
	private String contenido;
	
	

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public boolean buscarTexto(String palabra) {

		if (contenido.trim().toLowerCase().indexOf(palabra.trim().toLowerCase()) != -1 || titulo.trim().toLowerCase().indexOf(palabra.trim().toLowerCase()) != -1) {

			return true;
		} else {

			return false;

		}

	}

}
