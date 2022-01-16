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
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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

import javax.xml.crypto.Data;

/*TODO
Sbarrare Task Completati
Competamento Sezione Colorare Di Verde
Cambiare Sfondo
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
	private ProgressIndicator progressIndicator;
	@FXML
	private ScrollPane scrollSezione;

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
			Button button = (Button) event.getTarget();
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
	Double contTot=0.0;
	Double max=0.0;
	private void loadSezioni(String projectId) throws SQLException {

		List<Sezione> sezioni = sDao.getSezioniProgetto(projectId);
		projectTitle.setText(App.getCurrentProgetto().getNome());
		contTot=0.0;
		max=0.0;
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
			modificaS.setId(Integer.toString(s.getId()));
			modificaS.setOnAction(modS);
			mB.getItems().add(eliminaS);
			mB.getItems().add(modificaS);
			// mB.setFont(new Font("Constantia", 20.0));
			mB.setId(Integer.toString(s.getId()));
			List<Task> tasks=tDao.getTaskSezione(s);
			contTot+=loadTasks(tasks, aC);
			max+=tasks.size();
			TitledPane nuovoTask = new TitledPane("➕ Nuovo task", null);
			System.out.println("§§§§§§§§§§§§§§§ "+s.getId());
			nuovoTask.setId(Integer.toString(s.getId()));
			nuovoTask.setOnMouseClicked(aggiungiTask);
			aC.getPanes().add(nuovoTask);
			bP.setTop(mB);
			bP.setCenter(aC);
			hboxSezione.getChildren().add(bP);
		}
		progressChanger(contTot, max);

		Button aggiungiSezione = new Button();
		aggiungiSezione.setText("➕ Nuova Sezione");
		aggiungiSezione.setId(projectId);
		hboxSezione.getChildren().add(aggiungiSezione);
		aggiungiSezione.setOnMouseClicked(aggiungiSex);
	}

	private void progressChanger(Double contTot, Double max) {
		if(contTot !=0)
			progressIndicator.setProgress(contTot / max);
		else
			progressIndicator.setProgress(0.0);
		if(contTot/max==1)
			anchorProjects.lookup("#"+App.getCurrentProgetto().getId()).setStyle("-fx-base: green");
		else
			anchorProjects.lookup("#"+App.getCurrentProgetto().getId()).setStyle(null);
	}

	private int loadTasks(List<Task> tasks, Accordion aC) {
		int cont=0;
		for (Task task : tasks) {
			TitledPane tP = new TitledPane();
			BorderPane bTask = new BorderPane();
			//TODO Task Completata Strikethorugh
			//Text taskName=new Text(task.getNome());
			//taskName.setStrikethrough(true);
			tP.setText(task.getNome());
			tP.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			if(task.getCompletata())

			tP.setStyle(".text -fx-strikethrough: true;");
			tP.setId(Integer.toString(task.getId()));

			VBox dataCreazione=new VBox();
			dataCreazione.getChildren().add(new Label("Data Creazione"));
			dataCreazione.getChildren().add(new Label(task.getData()));
			bTask.setBottom(dataCreazione);

			setPrioriotaColor(task.getPriorita(), tP);
			ComboBox<Priorita> priorita = priorityComboBox.comboBoxPriorita(task,tP);
			VBox vbox=new VBox();
			vbox.getChildren().add(new Label("Priorità"));
			vbox.getChildren().add(priorita);
			bTask.setTop(vbox);

			CheckBox completata = new CheckBox("Completata");
			completata.setSelected(task.getCompletata());
			taskCompletataListener(task, completata);
			if(task.getCompletata())
				cont++;

			//bTask.setLeft();
			bTask.setCenter(completata);
			Button eliminaTask=new Button("X");
			eliminaTask.setStyle("-fx-base: red");
			eliminaTask.setId(Integer.toString(task.getId()));
			eliminaTask.setOnMouseClicked(delTask);
			bTask.setRight(eliminaTask);
			tP.setContent(bTask);
			aC.getPanes().add(tP);
		}
		return cont;
	}

	public static void setPrioriotaColor(int priorita, TitledPane tP) {
		if(priorita==-1){
			tP.setStyle("-fx-base: blue");
		}
		else if(priorita==0){
			tP.setStyle("-fx-base: green");
		}
		else if(priorita==1){
			tP.setStyle("-fx-base: yellow");
		}
		else if(priorita==2){
			tP.setStyle("-fx-base: red");
		}
	}

	private void taskCompletataListener(Task task, CheckBox completata) {
		completata.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,Boolean newValue) {
				if(newValue)
					progressChanger(++contTot, max);
				else
					progressChanger(--contTot, max);

					try {
					tDao.updateCompletata(task.getId(),newValue);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}

	private ComboBox<Priorita> comboBoxPriorita(Task task,TitledPane tP) {
		return util.PriorityComboBox.comboBoxPriorita(task,tP);
	}

	EventHandler<ActionEvent> modS = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Optional<String> result = DialogMaker.textInputDialog("Rinominazione Sezioni", "Rinomina Sezione", "Inserire nome Sezione:");
			MenuItem t = (MenuItem) event.getTarget();
			if (result.isPresent()) {
				System.out.println("Entrato");
				System.out.println(result.get());
				try {
					sDao.update(Integer.parseInt(t.getId()),result.get());
					DialogMaker.informationAlert("Sezione Rinominata con successo!");
					loadSezioni(Integer.toString(App.getCurrentProgetto().getId()));
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			}

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
	EventHandler<MouseEvent> delTask = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {
			System.out.println("EliminaTask");
			Button n = (Button) event.getSource();
			Optional<ButtonType> result = DialogMaker.createDeleteAlert("Eliminazione Task", "Elimina Task");
			if (result.get() == ButtonType.OK) {
				System.out.println("Entrato");
				System.out.println(result.get());
				try {
					tDao.delete(Integer.parseInt(n.getId()));
				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();
				}
				DialogMaker.informationAlert("Task Eliminata con successo!");
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
				Button b = new Button();
				b.setText(progetto.getNome());
//				b.setMaxWidth(anchorProjcts.getPrefWidth());
				b.setMinHeight(50);
				b.setId(Integer.toString(progetto.getId()));
				b.setOnAction(eH);
				anchorPaneParent.setLeftAnchor(b, 0.0);
				anchorPaneParent.setRightAnchor(b, 0.0);
//				System.out.println(b.getMinHeight() + " " + numProjects);
//				b.setTranslateY(numProjects++ * b.getMinHeight());
				//AnchorPane.setLeftAnchor(anchorProjects,20.0);
				//AnchorPane.setRightAnchor(anchorProjects,20.0);
				AnchorPane.setLeftAnchor(anchorPaneParent,20.0);
				AnchorPane.setRightAnchor(anchorPaneParent,0.0);
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


