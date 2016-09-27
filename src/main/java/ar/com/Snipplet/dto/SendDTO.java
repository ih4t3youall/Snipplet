package ar.com.Snipplet.dto;

import java.io.Serializable;

public class SendDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5474147272546716473L;

	public String username;
	public String password;
	public CategoriaDTO categoriaDTO;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public CategoriaDTO getCategoriaDTO() {
		return categoriaDTO;
	}

	public void setCategoriaDTO(CategoriaDTO categoriaDTO) {
		this.categoriaDTO = categoriaDTO;
	}

}
