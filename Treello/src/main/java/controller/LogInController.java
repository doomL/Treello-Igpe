package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import FranV.Treello.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Utente;
import persistence.UtenteDao;

public class LogInController implements Initializable{
	
	UtenteDao uD = new UtenteDao();
	
	@FXML
	private TextField username;
	@FXML
	private TextField password;	
	@FXML
	private void signIn() throws SQLException, IOException { 
		Utente utente = new Utente(username.getText(),password.getText());
		System.out.println(utente.toString());
		boolean correct = uD.login(utente); 
		if(!correct) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Username o Password Errati!");
			a.show();
			username.clear();
			password.clear();
			return;
		}   
		System.out.println("Sei dentro");
		App.setCurrentUser(utente);
		App.setRoot("homePage");
	}
	
    @FXML
    private void switchToSignUp() throws IOException {
        App.setRoot("SignUpPage");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}
