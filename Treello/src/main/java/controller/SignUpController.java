package controller;

import java.io.IOException;
import java.sql.SQLException;

import FranV.Treello.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Utente;
import persistence.UtenteDao;

public class SignUpController {

	UtenteDao uD = new UtenteDao();
	
	@FXML
	private TextField email;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button signUp;
	
	@FXML
	private void createUser() throws SQLException, IOException {
		Utente utente = new Utente(username.getText(),email.getText(),password.getText());
		uD.save(utente);
		Alert a = new Alert(AlertType.INFORMATION);
		 // set content text
        a.setContentText("Utente Registrato!");
        a.show();
        App.setRoot("LogInPage");
        
	}
	
    @FXML
    private void switchToSignIn() throws IOException {
        App.setRoot("LogInPage");
    }
}