package ar.com.Snipplet.domain;
import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties({"nombreCategoria"})
public class Snipplet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombreCategoria;
	private String titulo;
	private String contenido;
	
	

	public void setNombreCategoria(String nombreCategoria){
		this.nombreCategoria=nombreCategoria;
	}
	public String getNombreCategoria(){
		return this.nombreCategoria;
		
	}
	
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

	@Override
	public String toString(){
		return this.titulo;
		
	}
	
	public boolean buscarTexto(String palabra) {

		if (contenido.trim().toLowerCase().indexOf(palabra.trim().toLowerCase()) != -1 || titulo.trim().toLowerCase().indexOf(palabra.trim().toLowerCase()) != -1) {

			return true;
		} else {

			return false;

		}

	}

}
