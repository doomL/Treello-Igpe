package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import FranV.Treello.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Priorita;
import model.Progetto;
import model.Sezione;
import model.Task;
import persistence.ProgettoDao;
import persistence.SezioneDao;
import persistence.TaskDao;
import util.DialogMaker;
import util.PriorityComboBox;

/*TODO
Modifica Sezione
Eliminare Task Quando si elimina sezione
Elimina/Modifica Task
Cambiare Sfondo
Sbarrare Task Completati
Cambiare colore Task Priorità
Percentuale Completamento Progetto
Competamento Sezione Colorare Di Verde
Completamento Progetto Colorare Di Verde
*/

public class HomePageController implements Initializable {

	final Stage dialog = new Stage();
	private final PriorityComboBox priorityComboBox = new PriorityComboBox();

	ProgettoDao pDao = new ProgettoDao();

	SezioneDao sDao = new SezioneDao();

	TaskDao tDao = new TaskDao();

	@FXML
	private VBox anchorProjects;

	@FXML
	private AnchorPane anchorPaneParent;

	@FXML
	private AnchorPane anchorSezioni;

	@FXML
	private Label projectTitle;

	@FXML
	private HBox hboxSezione;

	@FXML
	private void newProjectButton() throws IOException, SQLException {
		int nPro = pDao.getUserProjects().size() + 1;
		Optional<String> result = DialogMaker.createDialog("Progetto " + nPro,"Aggiunta Progetti","Aggiungi Progetto","Inserire nome progetto:");
		if (result.isPresent()) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.save(result.get());
			DialogMaker.informationAlert("Progetto Creato con successo!");
			loadProjectsFromDb();
		}
	}



	public void projectR() throws SQLException {
		Optional<String> result = DialogMaker.textInputDialog("Rinominazione Progetti", "Rinomina Progetto", "Inserire nome progetto:");
		if (result.isPresent()) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.update(result.get());
			DialogMaker.informationAlert("Progetto Rinominato con successo!");
			anchorSezioni.setVisible(false);
			loadProjectsFromDb();
		}

	}




	public void projectD() throws SQLException {
		Optional<ButtonType> result = DialogMaker.createDeleteAlert("Eliminazione Progetti", "Elimina Progetto");
		if (result.get() == ButtonType.OK) {
			System.out.println("Entrato");
			System.out.println(result.get());
			pDao.delete();
			DialogMaker.informationAlert("Progetto Eliminato con successo!");
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
			loadTasks(tDao.getTaskSezione(s), aC);
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

	private void loadTasks(List<Task> tasks, Accordion aC) {
		for (Task task : tasks) {
			TitledPane tP = new TitledPane();
			BorderPane bTask = new BorderPane();
			tP.setText(task.getNome());
			tP.setId(Integer.toString(task.getId()));
			bTask.setBottom(new Label(task.getData()));
			ComboBox<Priorita> priorita = priorityComboBox.comboBoxPriorita(task);
			VBox vbox=new VBox();
			vbox.getChildren().add(new Label("Priorità"));
			vbox.getChildren().add(priorita);
			bTask.setTop(vbox);
			CheckBox completata = new CheckBox("Completata");
			completata.setSelected(task.getCompletata());
			taskCompletataListener(task, completata);
			//bTask.setLeft();
			bTask.setCenter(completata);
			tP.setContent(bTask);
			aC.getPanes().add(tP);
		}
	}

	private void taskCompletataListener(Task task, CheckBox completata) {
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
	}

	private ComboBox<Priorita> comboBoxPriorita(Task task) {
		return util.PriorityComboBox.comboBoxPriorita(task);
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
			Optional<ButtonType> result = DialogMaker.createDeleteAlert("Eliminazione Sezione", "Elimina Sezione");
			if (result.get() == ButtonType.OK) {
				System.out.println("Entrato");
				System.out.println(result.get());
				try {
					sDao.delete(Integer.parseInt(n.getId()));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				DialogMaker.informationAlert("Sezione Eliminata con successo!");
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
			Optional<String> result = DialogMaker.textInputDialog("Aggiunta Sezioni", "Aggiungi Sezione", "Inserire nome Sezione:");
			Node t = (Node) event.getTarget();
			if (result.isPresent()) {
				try {
					sDao.save(result.get(), App.getCurrentProgetto().getId());
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				DialogMaker.informationAlert("Sezione Creata con successo!");
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
			Optional<String> result = DialogMaker.textInputDialog("Aggiunta Task", "Aggiungi Task", "Inserire nome Task:");
			Node t = (Node) event.getSource();
			System.out.println("++++++++++++++ "+t.getParent().getId()+" -- "+t.getId());
			if (result.isPresent()) {
				try {
					tDao.save(result.get(), Integer.parseInt(t.getId()));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				DialogMaker.informationAlert("Task Creato con successo!");
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


