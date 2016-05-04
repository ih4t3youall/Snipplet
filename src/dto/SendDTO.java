package dto;

import java.io.Serializable;

public class SendDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5474147272546716473L;
	
	
	public String nombre;
	public String body;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
