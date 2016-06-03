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

import domain.FileConfiguration;
import domain.UserConfiguration;
import dto.CategoriaDTO;

public class Persistencia {

	// test
	// private String prefix =
	// "C:\\Users\\juan.m.lequerica\\Desktop\\snippletsArchives\\";

	// produccion
	private String prefix = "C:\\Snipplet\\";
	// private String uri =
	// "http://www.sourcesistemas.com.ar/index.php/webservices/Snipplet_Webservice/";
	private String uri = "http://www.sourcesistemas.com.ar/index.php/";
	private String snipletFileConfiguration = "C:\\SnippletConfig\\snipletConf";
	private String userConfigurationFix = "C:\\SnippletConfig\\userConfiguration";

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void crearCategoria(String filename) throws IOException {

		File file = new File(prefix + filename);
		if (!existeArchivo(filename)) {
			file.createNewFile();
		}
	}

	public void inicializarCarpetas() throws IOException {

		boolean exists = new File("C:\\Snipplet").exists();
		if (!exists) {
			new File("C:\\Snipplet").mkdir();
			new File("C:\\SnippletConfig").mkdir();
			File file = new File(snipletFileConfiguration);

			file.createNewFile();
			new File(userConfigurationFix).createNewFile();
			
			FileOutputStream in = new FileOutputStream(file);
			ObjectOutputStream writer = new ObjectOutputStream(in);
			FileConfiguration conf = new FileConfiguration();
			conf.setPrefix(prefix);
			conf.setUri(uri);
			writer.writeObject(conf);
			writer.close();
			in.close();

		}

	}

	public void saveNewConfiguration(FileConfiguration fileConfiguration) throws IOException {
		
		File file = new File(snipletFileConfiguration);
		if(file.exists());
		file.delete();
		
		file.createNewFile();
		FileOutputStream os = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(fileConfiguration);
		oos.close();
		os.close();

	}

	public void saveNewUserConfiguration(UserConfiguration userConfiguration) throws IOException {
		 File file = new File(userConfigurationFix);
		 
		 if(file.exists());
		 file.delete();
		 
		 file.createNewFile();
		 FileOutputStream os = new FileOutputStream(file);
		 ObjectOutputStream oos = new ObjectOutputStream(os);
		 oos.writeObject(userConfiguration);
		 oos.close();
		 os.close();
	}

	public FileConfiguration getFileConfig() throws IOException, ClassNotFoundException {
		File file = new File("C:\\SnippletConfig\\snipletConf");
		FileInputStream in = new FileInputStream(file);
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(in);
		FileConfiguration fileConfiguration = (FileConfiguration) ois.readObject();
		prefix = fileConfiguration.getPrefix();
		return fileConfiguration;

	}
	
	

	public UserConfiguration getUserConfig() throws IOException, ClassNotFoundException {
		File file = new File(userConfigurationFix);
		FileInputStream in = new FileInputStream(file);
		@SuppressWarnings("resource")
		ObjectInputStream ois = new ObjectInputStream(in);
		UserConfiguration userConfiguration = (UserConfiguration) ois.readObject();
		return userConfiguration;
		
		
	}


	public boolean existeArchivo(String filename) {

		return new File(prefix + filename).exists();

	}
	
	

	public void guardar(Object obj, String filename) throws IOException {

		if (existeArchivo(filename)) {
			FileOutputStream fileOut;
			ObjectOutputStream obj_out = null;
			try {
				fileOut = new FileOutputStream(prefix + filename);
				obj_out = new ObjectOutputStream(fileOut);
				obj_out.writeObject(obj);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				obj_out.close();

			}

		} else {

			JOptionPane.showMessageDialog(null, "Error el archivo no existe!.");

		}
	}

	public CategoriaDTO recuperarGuardado(String filename) {
		File f = new File(prefix + filename);
		if (f.exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(prefix + filename));
				return (CategoriaDTO) ois.readObject();

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
		}
		return null;

	}

	public CategoriaDTO recuperarArchivoGuardado(File file) {
		if (file.exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(file));
				return (CategoriaDTO) ois.readObject();

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
		}
		return null;

	}

	public File getFileToSend(String filename) {
		File file = new File(prefix + filename);
		return file;

	}

	public void getFiles(List<CategoriaDTO> categorias) {
		File file = new File(prefix);
		
		
		String[] list = file.list();

		for (String string : list) {
			CategoriaDTO recuperarGuardado = recuperarGuardado(string);
			if (recuperarGuardado != null) {
				categorias.add(recuperarGuardado);
			} else {
				categorias.add(new CategoriaDTO(string));
			}
		}

	}

	public String[] listDirectory() {

		return new File(prefix).list();

	}

	public void eliminarYCrearArchivo(String filename) throws IOException {
		File file = new File(prefix + filename);
		file.delete();
		file.createNewFile();
	}

	public void deleteFile(String filename) {
		File file = new File(prefix + filename);
		file.delete();
	}

}