package ar.com.Snipplet.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import ar.com.commons.send.dto.CategoriaDTO;

import ar.com.Snipplet.domain.SourceObject;
import ar.com.Snipplet.helper.SnippletsHelper;
import ar.com.Snipplet.persistencia.Persistencia;
import ar.com.commons.send.dto.SnippletDTO;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SnippletService {

	private Persistencia persistencia;

	//snipplets en memoria
	private List<CategoriaDTO> categorias;

	private SnippletsHelper snippletHelper;

	private ConfigurationService configurationService;

	private VBox vbox;

	public SnippletService() {
		categorias = new ArrayList<CategoriaDTO>();
	}

	public void firstTime() {

		try {
			persistencia.inicializarCarpetas();
		} catch (IOException e) {
			// TODO Matar la aplicacion
			e.printStackTrace();
		}

	}

	public void config() {
		try {
			
			configurationService.cargarConfiguracion();
			

		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(null, "Error al cargar la configuracion.");
		
			
			e.printStackTrace();
		}

	}

	public String[] listarDirectorio() {

		return persistencia.listDirectory();

	}
	

	public void cleanCategoryList(String categoria) {
		
		CategoriaDTO searchCategory = searchCategory(categoria);
		
		List<SnippletDTO> snipplets = searchCategory.getSnipplets();
		
		if (snipplets !=  null )
			snipplets.clear();
		
	}
	
	/**
	 * devuelve una categoria especifica
	 * @param categoria , nombre de la categoria de la cual quiero recibir la lista
	 */
	private CategoriaDTO searchCategory(String categoria){
		
		for (CategoriaDTO categoriaDTO : categorias) {
			
			if(categoriaDTO.getNombre().equals(categoria))
					return categoriaDTO;
			
			
		}
		
		return null;
		
	}
	

	public List<AnchorPane> loadSnippletsPorCategoria(String categoria) {
		AnchorPane populatedPanel = null;

		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			List<SnippletDTO> snippletByCategory = getSnippletByCategory(categoria);
			if (snippletByCategory != null) {
				for (SnippletDTO snipplet : snippletByCategory) {
					populatedPanel = new AnchorPane();
					populatedPanel = snippletHelper.getPopulatedPanel(categoria, snipplet,false);
					panels.add(populatedPanel);

				}

			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return panels;

	}
	

	public List<AnchorPane> loadSnipplets(List<SnippletDTO> snippletByCategory, String categoria) {
		AnchorPane populatedPanel = null;

		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			if (snippletByCategory != null) {
				for (SnippletDTO snipplet : snippletByCategory) {
					populatedPanel = new AnchorPane();
					
					populatedPanel = snippletHelper.getPopulatedPanel(categoria, snipplet,false);
					panels.add(populatedPanel);

				}

			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return panels;

	}
	
	
	public List<AnchorPane> loadSnippletsForSearch(List<SnippletDTO> snippletByCategory){
		
		AnchorPane populatedPanel = null;

		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			if (snippletByCategory != null) {
				for (SnippletDTO snipplet : snippletByCategory) {
					populatedPanel = new AnchorPane();
					snipplet.setNombreCategoria(snipplet.getNombreCategoria());
					populatedPanel = snippletHelper.getPopulatedPanel(snipplet.getNombreCategoria(), snipplet,true);
					panels.add(populatedPanel);

				}

			} else {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return panels;
		
	}
	

	private List<SnippletDTO> getSnippletByCategory(String categoria) {

		for (CategoriaDTO categoriaDTO : categorias) {
			if (categoriaDTO.getNombre().equals(categoria)) {
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


	public void cargarArchivos() {

		categorias.clear();
		persistencia.getFiles(categorias);

	}

	public List<CategoriaDTO> getCategorias() {

		return categorias;
	}

	private CategoriaDTO getCategoriaDTO(String filename) {

		for (CategoriaDTO categoriaDTO : categorias) {

			if (categoriaDTO.getNombre().equals(filename)) {
				return categoriaDTO;
			}

		}
		return null;

	}

	public void agregarSnipplet(SnippletDTO snipplet, String categoria) {
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		categoriaDTO.setSnipplets(getSnippletByCategory(categoria));
		categoriaDTO.addSnipplet(snipplet);

	}
	
	
	public void snippletRepetido(SnippletDTO snipplet){
		int index =0;
		boolean flag =  false;
		List<SnippletDTO> snipplets = getSnippletByCategory(snipplet.getNombreCategoria());
		
		for(int i = 0 ; i < snipplets.size();i++){
			
			if(snipplets.get(i).getTitulo().equals(snipplet.getTitulo())){
				index = i;
				flag = true;
				break;
			}
			
		}
		
		if(flag){
			
			snipplets.remove(index);
			snipplets.add(snipplet);
			
			
		} 
		
		
		
		
	}
	
	public List<SnippletDTO> searchInCategory(String palabra,String categoria){
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		
		
		List<SnippletDTO> snipplets = new ArrayList<SnippletDTO>();
		for (SnippletDTO snip : categoriaDTO.getSnipplets()) {
			
			boolean buscarTexto = snip.buscarTexto(palabra);
			if(buscarTexto)
			snipplets.add(snip);
		}
		
		return snipplets;
		
		
		
	}
	
	public List<SnippletDTO> searchAll(String palabras){
		
		
		List<SnippletDTO> snippletsSearch = new LinkedList<SnippletDTO>();
		
		List<SnippletDTO> allSnipplets = getAllSnipplets();
		
		for (SnippletDTO allSnipplet : allSnipplets) {
			
			if(allSnipplet.buscarTexto(palabras)){
				snippletsSearch.add(allSnipplet);
				
			}
			
		}
		
						
		return snippletsSearch;
		
		
	}
	
	
	public List<SnippletDTO> getAllSnipplets(){
		
		List<SnippletDTO> snipplets = new LinkedList<SnippletDTO>();
		for (CategoriaDTO categoriaDTO : categorias) {
			
			
			for (SnippletDTO snipplet : categoriaDTO.getSnipplets()) {
				snipplet.setNombreCategoria(categoriaDTO.getNombre());
				snipplets.add(snipplet);
				
				
			}
			
			
		}
		
		return snipplets;
		
		
		
		
	}


	public void guardarCopiaSourceSistemas(SourceObject[] fromServer) {

		File file = new File(configurationService.getFileConfiguration().getConfigurationPrefix()+"mensajesSource");
		
		
			
			if (file.exists()) {
				ObjectInputStream ois = null;
				try {
					ois = new ObjectInputStream(new FileInputStream(file));
					SourceObject[] readObject =(SourceObject[]) ois.readObject();
					
					
					
					Object[] eliminarRepetidos = snippletHelper.eliminarRepetidos(readObject,fromServer);
					
					persistencia.guardar(eliminarRepetidos, "mensajesSource");
					


				} catch (Exception e) {
					e.printStackTrace();

				} finally {
					try {
						if (ois != null)
							ois.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else {
				
				
				try {
					file.createNewFile();
					
					FileOutputStream fileOut;
					ObjectOutputStream obj_out = null;
					try {
						fileOut = new FileOutputStream(file);
						obj_out = new ObjectOutputStream(fileOut);
						obj_out.writeObject(fromServer);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						obj_out.close();

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		
		
		
		
		
	}

	

	public void guardarCategoria(String text, String filename) throws Exception {
		CategoriaDTO categoriaDTO = getCategoriaDTO(filename);
		if (categoriaDTO.getNombre() == null) {
			throw new Exception("nose");

		}
		try {
			persistencia.eliminarYCrearArchivo(categoriaDTO.getNombre());
			errorMultiplesGuardados(categoriaDTO);

			persistencia.guardar(categoriaDTO, categoriaDTO.getNombre());
		} catch (IOException e) {
			System.out.println("error al guardar");
		}

	}

	private boolean existeEnLista(String titulo, List<SnippletDTO> lista) {

		for (SnippletDTO snip : lista) {

			if (titulo.equals(snip.getTitulo()))
				return true;

		}

		return false;

	}

	public void actualizarCategoria(CategoriaDTO categoriaDTOFromServer) {

		boolean exists = persistencia.existeArchivo(categoriaDTOFromServer.getNombre());
		CategoriaDTO categoriaLocal=null;
		if (exists) {
			categoriaLocal = getCategoriaDTO(categoriaDTOFromServer.getNombre());

			List<SnippletDTO> localSnipplets = categoriaLocal.getSnipplets();
			List<SnippletDTO> snippletsfromServer = categoriaDTOFromServer.getSnipplets();
			//sfs snipplet from server
			List<SnippletDTO> aAgregar = new LinkedList<SnippletDTO>();
			for (SnippletDTO sfs : snippletsfromServer) {
				
				//local snipplet
				for (SnippletDTO ls : localSnipplets) {
					if(sfs.getTitulo().equals(ls.getTitulo())){
						
						if(!sfs.getContenido().equals(ls.getContenido())){
							
							int dialogButton = JOptionPane.YES_NO_OPTION;
							int dialogResult = JOptionPane.showConfirmDialog (null,"El snipplet: "+ ls.getTitulo()+" es distinto al del server, dejas el del server?","Warning",dialogButton);
							if(dialogResult == JOptionPane.YES_OPTION){
							  aAgregar.add(sfs);
							}
							
						}
						
					}
					
				}
				
				if(aAgregar.size() > 0){
					for (SnippletDTO snipplet : aAgregar) {
						
						deleteSnippletFromList(localSnipplets,snipplet);
					}
					
					for (SnippletDTO snipplet : aAgregar) {
						
						localSnipplets.add(snipplet);
						
					}
					
					
				}
				
				
			}


		}

		try {
			if(categoriaLocal == null){
				categoriaLocal = categoriaDTOFromServer;
			}
			persistencia.eliminarYCrearArchivo(categoriaLocal.getNombre());
			errorMultiplesGuardados(categoriaLocal);

			persistencia.guardar(categoriaLocal, categoriaLocal.getNombre());
			
		} catch (IOException e) {
			System.out.println("error al guardar");
		}

	}

	private void deleteSnippletFromList(List<SnippletDTO> localSnipplets, SnippletDTO victim) {

		int cont = 0;
		int victimNumber = 0;
		for (SnippletDTO snipplet : localSnipplets) {
			if(snipplet.getTitulo().equals(victim.getTitulo())){
				victimNumber = cont;
				
			}
			cont++;
		}
		
		localSnipplets.remove(victimNumber);
		
		
		
		
	}

	public void deleteCategory(String filename) {
		persistencia.deleteFile(filename);

	}

	private void errorMultiplesGuardados(CategoriaDTO categoriaDTO) {

		List<SnippletDTO> snipplets = new ArrayList<SnippletDTO>();

		for (SnippletDTO snipplet : categoriaDTO.getSnipplets()) {

			if (!snipplet.equals(null))
				snipplets.add(snipplet);

		}

		categoriaDTO.setSnipplets(snipplets);

	}

	public void deleteSnippletFromList(String id, String categoria, String text) {

		ObservableList<Node> children = vbox.getChildren();
		int cont = 0;
		for (Node node : children) {

			if (node.getId().equals(id)) {

				break;
			}
			cont++;
		}

		children.remove(cont);

		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);

		List<SnippletDTO> snipplets = categoriaDTO.getSnipplets();
		cont = 0;
		for (SnippletDTO snipplet : snipplets) {
			if (snipplet.getContenido().equals(text)) {
				break;
			}
			cont++;
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

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void crearNuevaCarpeta(String newPrefix) {
		persistencia.createFolder(newPrefix);
		
	}



}
