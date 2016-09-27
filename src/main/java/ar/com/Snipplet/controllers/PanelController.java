package ar.com.Snipplet.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import ar.com.Snipplet.context.SpringContext;
import ar.com.Snipplet.domain.Snipplet;
import ar.com.Snipplet.services.SnippletService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class PanelController implements Initializable {

	@FXML
	private Button delete;

	@FXML
	private Button guardar;

	@FXML
	private TextArea textArea;

	@FXML
	private Button copy;

	@FXML
	private TextField titulo;

	private String id;

	private String categoria;

	private VBox vbox = (VBox) SpringContext.getContext().getBean("vbox");

	private SnippletService snnipletService = (SnippletService) SpringContext.getContext().getBean("snippletService");

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();

	private CopyKeyHandler copyHandler;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		copy.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				content.putString(textArea.getText());
				clipboard.setContent(content);

			}
		});

		guardar.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				String text = textArea.getText();
				ObservableList<Node> list = vbox.getChildren();
				// elimino todos los sniplets de la categoria seleccionada
				snnipletService.cleanCategoryList(categoria);
				for (int i = 0; i < list.size(); i++) {

					AnchorPane anchorPane = (AnchorPane) list.get(i);
					ObservableList<Node> node = anchorPane.getChildren();
					TextArea textArea = (TextArea) node.get(1);
					TextField textField = (TextField) node.get(4);

					String titulo = textField.getText();
					String contenido = textArea.getText();

					Snipplet snipplet = new Snipplet();
					snipplet.setTitulo(titulo);
					snipplet.setContenido(contenido);

					snnipletService.agregarSnipplet(snipplet, categoria);

				}
				try {
					snnipletService.guardarCategoria("", categoria);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				snnipletService.deleteSnippletFromList(id, categoria, textArea.getText());

			}
		});

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	public TextField getTitulo() {
		return titulo;
	}

	public void setTitulo(TextField titulo) {
		this.titulo = titulo;
	}

	public void setListener(CopyKeyHandler copyHandler) {

		this.copyHandler = copyHandler;
		
	}

}