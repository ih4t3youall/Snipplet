package dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombre;
	private List<String> snipplets;
	private List<String> tags;

	public void addSnipplet(String snipplet){
		if(snipplets == null){
			
			snipplets = new ArrayList<String>();
			
		}
		
		snipplets.add(snipplet);
	
		
	}
	
	
	public CategoriaDTO(String nombre) {
		this.nombre = nombre;

	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<String> getSnipplets() {
		return snipplets;
	}

	public void setSnipplets(List<String> snipplets) {
		this.snipplets = snipplets;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
