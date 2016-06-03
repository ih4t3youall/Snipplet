package domain;

import java.io.Serializable;

public class SourceObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private String mail;
	private String asunto;
	private String texto;
	private String leido;
	
	

	public SourceObject(String nombre, String mail, String asunto, String texto, String leido) {
		super();
		this.nombre = nombre;
		this.mail = mail;
		this.asunto = asunto;
		this.texto = texto;
		this.leido = leido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getLeido() {
		return leido;
	}

	public void setLeido(String leido) {
		this.leido = leido;
	}
	
	@Override 
	public String toString(){
		
		return nombre+mail+asunto+texto+leido;
		
		
	}

}
