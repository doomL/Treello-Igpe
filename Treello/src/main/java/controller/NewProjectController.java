package controller;

import java.io.IOException;
import java.sql.SQLException;

import FranV.Treello.App;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import persistence.ProgettoDao;

public class NewProjectController {

	@FXML
	private Button annulla;
	@FXML
	private TextField nameProject;
	
	public AnchorPane homeAnchorProjects;
	
	public int numProjects;
	


	ProgettoDao pDao = new ProgettoDao();
	@FXML
	private void annullaProgetto() {
		Stage stage = (Stage) annulla.getScene().getWindow();
		stage.close();
		System.out.println("Annulla");
	}

	/*@FXML
	private void salvaProgetto() throws SQLException {
		System.out.println("Entrato");
		System.out.println(nameProject.getText());
		pDao.save(nameProject.getText());
		Alert a = new Alert(AlertType.INFORMATION);
		// set content text
		a.setContentText("Progetto Creato con successo!");
		a.show();
		Stage stage = (Stage) annulla.getScene().getWindow();
		stage.close();
		System.out.println("Salva");
//		Scene scene = scrollProjects.getScene();
//		AnchorPane anchorPane = (AnchorPane) scene.lookup("#anchorProjects");
		Button b = new Button();
		b.setText(nameProject.getText());
//		b.setMaxWidth(anchorProjects.getPrefWidth());
		b.setMinHeight(50);
		//b.setId(Integer.toString());
		//b.setOnAction(eH);
		System.out.println(b.getMinHeight() + " " + numProjects);
		b.setTranslateY((pDao.getUserProjects().size()-1) * b.getMinHeight());
		homeAnchorProjects.getChildren().add(b);
		
	}*/
}
