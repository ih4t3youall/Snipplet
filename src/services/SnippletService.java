package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.Snipplet;
import dto.CategoriaDTO;
import helper.SnippletsHelper;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import persistencia.Persistencia;

public class SnippletService {

	private Persistencia persistencia;

	private List<CategoriaDTO> categorias;

	private SnippletsHelper snippletHelper;

	private VBox vbox;

	public SnippletService() {
		categorias = new ArrayList<CategoriaDTO>();
	}
	
	
	
	public void firstTime(){
		
		persistencia.inicializarCarpetas();
		
	}

	public AnchorPane obtenerSnippletsPorCategoria(String categoria) {
		AnchorPane emptyPanel = null;
		try {
			emptyPanel = snippletHelper.getEmptyPanel(categoria);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return emptyPanel;

	}
	
	public List<AnchorPane> loadSnippletsPorCategoria(String categoria) {
		AnchorPane populatedPanel = null;
		
		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			List<Snipplet> snippletByCategory = getSnippletByCategory(categoria);
			if(snippletByCategory != null){
			for (Snipplet snipplet : snippletByCategory) {
				populatedPanel = new AnchorPane();
				populatedPanel = snippletHelper.getPopulatedPanel(categoria,snipplet);
				panels.add(populatedPanel);
				
				
			}
			
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return panels;

	}
	
	
	private List<Snipplet> getSnippletByCategory(String categoria){
		
		for (CategoriaDTO categoriaDTO : categorias) {
				if(categoriaDTO.getNombre().equals(categoria)){
					return categoriaDTO.getSnipplets();
				}
		}
		return null;
		
		
	}

	public void crearNuevoSnipplet(String nuevaCategoria) {

		categorias.add(new CategoriaDTO(nuevaCategoria));
		try {
			persistencia.crearCategoria(nuevaCategoria);
		} catch (IOException e) {
			System.out.println("error archivo");
		}

	}

	public void recuperarPrueba(String filename) {

		persistencia.recuperarGuardado(filename);

	}
	
	
	public void cargarArchivos() {
		
		persistencia.getFiles(categorias);
		
	}

	public List<CategoriaDTO> getCategorias(){
		
		return categorias;
	}
	
	
	
	private CategoriaDTO getCategoriaDTO(String filename){
		
		for (CategoriaDTO categoriaDTO : categorias) {
			
			if(categoriaDTO.getNombre().equals(filename)){
				return categoriaDTO;
			}
			
		}
		return null;
		
	}
	
	
	public void agregarSnipplet(Snipplet snipplet, String categoria) {
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		categoriaDTO.addSnipplet(snipplet);
		
	}
	

	public void guardarPrueba(String text, String filename) throws Exception {
		CategoriaDTO categoriaDTO = getCategoriaDTO(filename);
		if(categoriaDTO.getNombre() == null){
			throw  new Exception("nose");
			
		}
		try {
			persistencia.eliminarYCrearArchivo(categoriaDTO.getNombre());
			errorMultiplesGuardados(categoriaDTO);
			
			
			persistencia.Guardar(categoriaDTO, categoriaDTO.getNombre());
		} catch (IOException e) {
			System.out.println("error al guardar");
		}
		

	}
	
	public void deleteCategory(String filename) {
		persistencia.deleteFile(filename);
		
	}
	
	
	
	
	private void errorMultiplesGuardados(CategoriaDTO categoriaDTO) {
		
		List<Snipplet> snipplets = new ArrayList<Snipplet>();
		
		for (Snipplet snipplet : categoriaDTO.getSnipplets()) {
			
			if(!snipplet.equals(null))
				snipplets.add(snipplet);
			
		}
		
		categoriaDTO.setSnipplets(snipplets);
		
		
	}

	public void deleteSnippletFromList(String id, String categoria, String text){
		
		
		ObservableList<Node> children = vbox.getChildren();
		int cont=0;
		for (Node node : children) {
			
			if(node.getId().equals(id)){
				
				break;
			}
			cont++;
		}
		
		children.remove(cont);
		
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		
		List<Snipplet> snipplets = categoriaDTO.getSnipplets();
		cont =0;
		for (Snipplet snipplet : snipplets) {
			if(snipplet.getContenido().equals(text)){
				break;
			}
			cont ++;
		}
		
		categoriaDTO.getSnipplets().remove(cont);
		
		
	}

	public Persistencia getPersistencia() {
		return persistencia;
	}

	public void setPersistencia(Persistencia persistencia) {
		this.persistencia = persistencia;
	}

	public SnippletsHelper getSnippletHelper() {
		return snippletHelper;
	}

	public void setSnippletHelper(SnippletsHelper snippletHelper) {
		this.snippletHelper = snippletHelper;
	}

	public VBox getVbox() {
		return vbox;
	}

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}





	
}
