package ar.com.Snipplet.domain;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

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
	
	public boolean buscarTexto(String palabrasAUX ){

        StringTokenizer token = new StringTokenizer(palabrasAUX);
        List<String> palabras = new LinkedList<String>();

        while(token.hasMoreTokens()){
            palabras.add(token.nextToken());
        }



        int cantidad = palabras.size();
        int contador = 0;

        for (String palabra :palabras ) {

            boolean flag = false;
            if(contenido.trim().toLowerCase().indexOf(palabra) != -1){
                flag = true;

            }
            if(titulo.trim().toLowerCase().indexOf(palabra) != -1){
                flag = true;

            }

            if(flag){
                contador++;
                flag = false;
            }




        }

        if(contador == cantidad){
            return true;
        }else{
            return false;
        }


    }


}
