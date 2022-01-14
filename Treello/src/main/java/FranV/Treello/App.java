package FranV.Treello;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Progetto;
import model.Utente;
import persistence.DbManager;

import java.io.IOException;
import java.sql.SQLException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static Utente currentUser;
    
    private static Progetto currentProgetto;
    
    public static Utente getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(Utente currentUser) {
		App.currentUser = currentUser;
	}

	@Override
    public void start(Stage stage) throws IOException, SQLException{
    	DbManager dbManager = new DbManager();
    	dbManager.init();
        scene = new Scene(loadFXML("LogInPage"), 640, 480);       
        stage.setScene(scene);     
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

	public static Progetto getCurrentProgetto() {
		return currentProgetto;
	}

	public static void setCurrentProgetto(Progetto currentProgetto) {
		App.currentProgetto = currentProgetto;
	}

}