package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import domain.Snipplet;
import domain.SourceObject;
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

	public List<AnchorPane> loadSnippletsPorCategoria(String categoria) {
		AnchorPane populatedPanel = null;

		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			List<Snipplet> snippletByCategory = getSnippletByCategory(categoria);
			if (snippletByCategory != null) {
				for (Snipplet snipplet : snippletByCategory) {
					populatedPanel = new AnchorPane();
					populatedPanel = snippletHelper.getPopulatedPanel(categoria, snipplet);
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
	

	public List<AnchorPane> loadSnipplets(List<Snipplet> snippletByCategory,String categoria) {
		AnchorPane populatedPanel = null;

		List<AnchorPane> panels = new ArrayList<AnchorPane>();
		try {
			if (snippletByCategory != null) {
				for (Snipplet snipplet : snippletByCategory) {
					populatedPanel = new AnchorPane();
					populatedPanel = snippletHelper.getPopulatedPanel(categoria, snipplet);
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
	

	private List<Snipplet> getSnippletByCategory(String categoria) {

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

	public void agregarSnipplet(Snipplet snipplet, String categoria) {
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		categoriaDTO.addSnipplet(snipplet);

	}
	
	public List<Snipplet> buscarEnCategorias(String palabra,String categoria){
		CategoriaDTO categoriaDTO = getCategoriaDTO(categoria);
		
		
		List<Snipplet> snipplets = new ArrayList<Snipplet>();
		for (Snipplet snip : categoriaDTO.getSnipplets()) {
			
			boolean buscarTexto = snip.buscarTexto(palabra);
			if(buscarTexto)
			snipplets.add(snip);
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

	private boolean existeEnLista(String titulo, List<Snipplet> lista) {

		for (Snipplet snip : lista) {

			if (titulo.equals(snip.getTitulo()))
				return true;

		}

		return false;

	}

	public void actualizarCategoria(CategoriaDTO categoriaDTO) {

		boolean exists = persistencia.existeArchivo(categoriaDTO.getNombre());

		if (exists) {
			CategoriaDTO categoriaVieja = getCategoriaDTO(categoriaDTO.getNombre());

			List<Snipplet> oldSnipplets = categoriaVieja.getSnipplets();
			List<Snipplet> snipplets = categoriaDTO.getSnipplets();

			for (Snipplet snipplet : oldSnipplets) {

				if (!existeEnLista(snipplet.getTitulo(), snipplets)) {
					// no existe
					snipplets.add(snipplet);

				}

			}

		}

		try {
			persistencia.eliminarYCrearArchivo(categoriaDTO.getNombre());
			errorMultiplesGuardados(categoriaDTO);

			persistencia.guardar(categoriaDTO, categoriaDTO.getNombre());
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

		List<Snipplet> snipplets = categoriaDTO.getSnipplets();
		cont = 0;
		for (Snipplet snipplet : snipplets) {
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
