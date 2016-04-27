package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JOptionPane;

import dto.CategoriaDTO;

public class Persistencia {

	private String prefix = "C:\\Snipplet\\";
	
	public void crearCategoria(String filename) throws IOException{
		
		File file = new File(prefix+filename);
		if(!existeArchivo(prefix+filename)){
		file.createNewFile();
		}
	}
	
	
	public void inicializarCarpetas(){
		
		
		boolean exists = new File("C:\\Snipplet").exists();
		
		if(!exists)
		new File("C:\\Snipplet").mkdir();
		
		
	}
	
	public boolean existeArchivo(String filename){
		
		
		return new File(filename).exists();
		
	}

	public void Guardar(Object obj,String filename) throws IOException {

		if(existeArchivo(prefix+filename)){
		FileOutputStream fileOut;
		ObjectOutputStream obj_out= null;
		try {
			
			fileOut = new FileOutputStream(prefix+filename);
			obj_out = new ObjectOutputStream(fileOut);
			obj_out.writeObject(obj);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			obj_out.close();
			
		}
		
		}else {
			
			JOptionPane.showMessageDialog(null, "Error el archivo no existe!.");
			
			
		}
	}

	public CategoriaDTO recuperarGuardado(String filename) {

		File f = new File(prefix+filename);
		if(f.exists()){
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(prefix+filename));

			return (CategoriaDTO) ois.readObject();

			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		}
		return null;

	}


	public void getFiles(List<CategoriaDTO> categorias) {
		File file = new File(prefix);
		String[] list = file.list();
		
		
		for (String string : list) {
			CategoriaDTO recuperarGuardado = recuperarGuardado(string);
			if(recuperarGuardado != null){
			categorias.add(recuperarGuardado);
			}else{
				categorias.add(new CategoriaDTO(string));
				
			}
		}
		
	}


	public void eliminarYCrearArchivo(String filename) throws IOException {
		File file = new File(prefix+filename);
		file.delete();
		file.createNewFile();
		
	}


	public void deleteFile(String filename) {
		File file = new File(prefix+filename);
		file.delete();
		
	}
}