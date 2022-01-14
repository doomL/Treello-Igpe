package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import FranV.Treello.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Priorita;
import model.Progetto;
import model.Sezione;
import model.Task;
import persistence.ProgettoDao;
import persistence.SezioneDao;
import persistence.TaskDao;


public class HomePageController implements Initializable {

	final Stage dialog = new Stage();

	ProgettoDao pDao = new ProgettoDao();

	SezioneDao sDao = new SezioneDao();

	TaskDao tDao = new TaskDao();

	Integer currentSectionId;
	
	@FXML
	private ScrollPane scrollProjects;
	@FXML
	private VBox anchorProjects;

	@FXML
	private AnchorPane anchorPaneParent;

	@FXML
	private ScrollPane scrollSezioni;
	@FXML
	private AnchorPane anchorSezioni;

	@FXML
	private Label projectTitle;

	@FXML
	private HBox hboxSezione;
	
	@FXML
	private void newProjectButton() throws IOException, SQLException {
		int nPro = pDao.getUserProjects().size() + 1;
		TextInputDialog dialog = new TextInputDialog("Progetto " + nPro);
		dialog.setTitle("Aggiunta Progetti");
		dialog.setHeaderText("Aggiungi Progetto");
		dialog.setContentText("Inserire nome progetto:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.save(result.get());
			Alert a = new Alert(AlertType.INFORMATION);
			// set content text
			a.setContentText("Progetto Creato con successo!");
			a.show();
			System.out.println("Salva");
			loadProjectsFromDb();
		}
	}

	public void projectR() throws SQLException {
		TextInputDialog dialog = new TextInputDialog();
		
		/*dialog.setTitle("Rinominazione Progetti");
		dialog.setHeaderText("Rinomina Progetto");
		dialog.setContentText("Inserire nome progetto:");*/
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.update(result.get());
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText("Progetto Rinominato con successo!");
			a.show();
			System.out.println("Salva");
			anchorSezioni.setVisible(false);
			loadProjectsFromDb();
		}

	}

	public void projectD() throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Eliminazione Progetti");
		alert.setHeaderText("Elimina Progetto");
		alert.setContentText("Sei sicuro ?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.delete();
			Alert a = new Alert(AlertType.INFORMATION);
			a.setContentText("Progetto Eliminato con successo!");
			a.show();
			System.out.println("Salva");
			anchorSezioni.setVisible(false);
			loadProjectsFromDb();
		}
	}

	EventHandler<ActionEvent> eH = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			SplitMenuButton button = (SplitMenuButton) event.getTarget();
			hboxSezione.getChildren().clear();
			anchorSezioni.setVisible(true);
			System.out.println("Hello World! " + button.getId());
			try {
				loadSezioni(button.getId());

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	};
	private void loadSezioni(String projectId) throws SQLException {
		List<Sezione> sezioni = sDao.getSezioniProgetto(projectId);
		projectTitle.setText(App.getCurrentProgetto().getNome());
		for (Sezione s : sezioni) {
			List<Task> tasks = tDao.getTaskSezione(s); 
			BorderPane bP = new BorderPane();
			MenuButton mB = new MenuButton();
			Accordion aC = new Accordion();
			mB.setText(s.getNome());
			mB.setFont(Font.font("Constantia", FontWeight.EXTRA_BOLD, 20.0));
			MenuItem eliminaS = new MenuItem("Elimina Sezione");
			eliminaS.setId(Integer.toString(s.getId()));
			eliminaS.setOnAction(eliS);
			MenuItem modificaS = new MenuItem("Modifica Sezione");
			//modificaS.setOnAction(modS());
			mB.getItems().add(eliminaS);
			mB.getItems().add(modificaS);
			// mB.setFont(new Font("Constantia", 20.0));
			mB.setId(Integer.toString(s.getId()));
			for (Task task : tasks) {
				TitledPane tP = new TitledPane();
				BorderPane bTask = new BorderPane();
				tP.setText(task.getNome());
				tP.setId(Integer.toString(task.getId()));
				bTask.setBottom(new Label(task.getData()));
				ComboBox<Priorita> priorita = comboBoxPriorita(task);
				VBox vbox=new VBox();
				vbox.getChildren().add(new Label("Priorità"));
				vbox.getChildren().add(priorita);
				bTask.setTop(vbox);
				CheckBox completata = new CheckBox("Completata");
				completata.setSelected(task.getCompletata());
				completata.selectedProperty().addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						try {
							tDao.updateCompletata(task.getId(),newValue);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				});
				//bTask.setLeft();
				bTask.setCenter(completata);
				tP.setContent(bTask);
				aC.getPanes().add(tP);
			}
			TitledPane nuovoTask = new TitledPane("➕ Nuovo task", null);
			/*
			 * Node arrow = nuovoTask.lookup(".arrow"); arrow.setVisible(false);
			 */
			System.out.println("§§§§§§§§§§§§§§§ "+s.getId());
			nuovoTask.setId(Integer.toString(s.getId()));
			nuovoTask.setOnMouseClicked(aggiungiTask);
			aC.getPanes().add(nuovoTask);
			bP.setTop(mB);
			bP.setCenter(aC);
			hboxSezione.getChildren().add(bP);
		}
		Button aggiungiSezione = new Button();
		aggiungiSezione.setText("➕ Nuova Sezione");
		aggiungiSezione.setId(projectId);
		hboxSezione.getChildren().add(aggiungiSezione);
		aggiungiSezione.setOnMouseClicked(aggiungiSex);
	}

	private ComboBox<Priorita> comboBoxPriorita(Task task) {
		ObservableList<Priorita> options =FXCollections.observableArrayList(
				new Priorita("Bassa",-1),new Priorita("Normale",0),new Priorita("Alta",1),new Priorita("Massima",2));
		ComboBox<Priorita> priorita = new ComboBox<Priorita>(options);		
		priorita.valueProperty().addListener((obs, oldVal, newVal)->
				{
					try {
						tDao.updatePriorita(task.getId(),newVal.getnPriorita());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		priorita.setConverter(new StringConverter<Priorita>() {
			
			@Override
			public String toString(Priorita object) {
				if(object!=null)
					return object.getP();
				return null;
			}
			
			@Override
			public Priorita fromString(String string) {
				return null;
			}
		});
		priorita.getSelectionModel().select(task.getPriorita()+1);
		return priorita;
	}

	EventHandler<ActionEvent> modS = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
	
			
			}
	};
	
	EventHandler<ActionEvent> eliS = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			MenuItem n = (MenuItem) event.getTarget();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Eliminazione Sezione");
			alert.setHeaderText("Elimina Sezione");
			alert.setContentText("Sei sicuro ?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				System.out.println("Entrato");
				System.out.println(result.get());
				try {
					sDao.delete(Integer.parseInt(n.getId()));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				Alert a = new Alert(AlertType.INFORMATION);
				a.setContentText("Sezione Eliminata con successo!");
				a.show();
				System.out.println("Salva");
				hboxSezione.getChildren().clear();
				try {
					loadSezioni(Integer.toString(App.getCurrentProgetto().getId()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			}
	};		
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorSezioni.setVisible(false);
		loadProjectsFromDb();
	}

	public void loadProjectsFromDb() {

		anchorProjects.getChildren().clear();
		try {
			List<Progetto> list = pDao.getUserProjects();
			for (Progetto progetto : list) {
				System.out.println(progetto.toString());
				SplitMenuButton b = new SplitMenuButton();
				b.setText(progetto.getNome());
//				b.setMaxWidth(anchorProjcts.getPrefWidth());
				b.setMinHeight(50);
				b.setId(Integer.toString(progetto.getId()));
				b.setOnAction(eH);
				anchorPaneParent.setLeftAnchor(b, 0.0);
				anchorPaneParent.setRightAnchor(b, 0.0);
//				System.out.println(b.getMinHeight() + " " + numProjects);
//				b.setTranslateY(numProjects++ * b.getMinHeight());
				anchorProjects.getChildren().add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	EventHandler<MouseEvent> aggiungiSex = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Aggiunta Sezioni");
			dialog.setHeaderText("Aggiungi Sezione");
			dialog.setContentText("Inserire nome Sezione:");
			Optional<String> result = dialog.showAndWait();
			Node t = (Node) event.getTarget();
			if (result.isPresent()) {
				try {
					sDao.save(result.get(), App.getCurrentProgetto().getId());
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				Alert a = new Alert(AlertType.INFORMATION);
				// set content text
				a.setContentText("Sezione Creato con successo!");
				a.show();
				System.out.println("Salva");
				System.out.println("Aggiungi Sezione");
				hboxSezione.getChildren().clear();
				try {
					loadSezioni(Integer.toString(App.getCurrentProgetto().getId()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}

	};
		
	EventHandler<MouseEvent> aggiungiTask = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Aggiunta Task");
			dialog.setHeaderText("Aggiungi Task");
			dialog.setContentText("Inserire nome Task:");
			Optional<String> result = dialog.showAndWait();
			Node t = (Node) event.getTarget();
			System.out.println("++++++++++++++ "+t.getParent().getId()+" -- "+t.getId());
			if (result.isPresent()) {
				try {										
					tDao.save(result.get(), Integer.parseInt(t.getParent().getId()));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				Alert a = new Alert(AlertType.INFORMATION);
				// set content text
				a.setContentText("Task Creato con successo!");
				a.show();
				System.out.println("Salva");
				System.out.println("Aggiungi Task");
				hboxSezione.getChildren().clear();
				try {
					loadSezioni(Integer.toString(App.getCurrentProgetto().getId()));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}

	};
}
