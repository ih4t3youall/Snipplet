package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		// TODO implemmentar traer los snipplets por categoria
		AnchorPane emptyPanel = null;
		try {
			emptyPanel = snippletHelper.getEmptyPanel(categoria);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO lleno el panel
		return emptyPanel;

	}
	
	public List<AnchorPane> loadSnippletsPorCategoria(String categoria) {
		// TODO implemmentar traer los snipplets por categoria
		AnchorPane populatedPanel = null;
		
		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			List<String> snippletByCategory = getSnippletByCategory(categoria);
			if(snippletByCategory != null){
			for (String string : snippletByCategory) {
				populatedPanel = new AnchorPane();
				populatedPanel = snippletHelper.getPopulatedPanel(categoria,string);
				panels.add(populatedPanel);
				
				
			}
			
			}else{
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO lleno el panel
		return panels;

	}
	
	
	private List<String> getSnippletByCategory(String categoria){
		
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
			// TODO Auto-generated catch block
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
	
	
	public void agregarSnipplet(String text, String categoria) {
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		categoriaDTO.addSnipplet(text);
		
	}
	

	public void guardarPrueba(String text, String filename) throws Exception {
		CategoriaDTO categoriaDTO = getCategoriaDTO(filename);
//		categoriaDTO.addSnipplet(text);
		if(categoriaDTO.getNombre() == null){
			//FIXME hace una exception nueva
			throw  new Exception("nose");
			
		}
		try {
			persistencia.eliminarYCrearArchivo(categoriaDTO.getNombre());
			errorMultiplesGuardados(categoriaDTO);
			
			
			persistencia.Guardar(categoriaDTO, categoriaDTO.getNombre());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error al guardar");
		}
		

	}
	
	public void deleteCategory(String filename) {
		persistencia.deleteFile(filename);
		
	}
	
	
	
	
	private void errorMultiplesGuardados(CategoriaDTO categoriaDTO) {
		
		List<String> strings = new ArrayList<String>();
		
		for (String string : categoriaDTO.getSnipplets()) {
			
			if(!string.equals(null))
				strings.add(string);
			
		}
		
		categoriaDTO.setSnipplets(strings);
		
		
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
		
		List<String> snipplets = categoriaDTO.getSnipplets();
		cont =0;
		for (String snipplet : snipplets) {
			if(snipplet.equals(text)){
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
