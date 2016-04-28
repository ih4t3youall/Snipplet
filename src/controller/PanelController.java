package controller;

import java.net.URL;
import java.util.ResourceBundle;

import context.SpringContext;
import domain.Snipplet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import services.SnippletService;

public class PanelController implements Initializable {

	@FXML
	private Button delete;

	@FXML
	private Button guardar;

	@FXML
	private TextArea textArea;

	@FXML
	private Button add;

	@FXML
	private Button copy;

	@FXML
	private TextField titulo;

	private String id;

	private String categoria;

	private SnippletService snnipletService = (SnippletService) SpringContext.getContext().getBean("snippletService");

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();

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
				if (text != null) {

					try {
						snnipletService.guardarPrueba(text, categoria);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		add.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Snipplet snipplet = new Snipplet();
				snipplet.setContenido(textArea.getText());
				snipplet.setTitulo(titulo.getText());
				if (snipplet.getContenido() != null)
					snnipletService.agregarSnipplet(snipplet, categoria);

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

}