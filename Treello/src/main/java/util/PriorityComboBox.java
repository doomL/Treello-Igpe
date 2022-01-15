package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import model.Priorita;
import model.Task;
import persistence.TaskDao;

import java.sql.SQLException;

public class PriorityComboBox {
    private static TaskDao tDao = new TaskDao();

    public static ComboBox<Priorita> comboBoxPriorita(Task task) {
        ObservableList<Priorita> options = FXCollections.observableArrayList(
                new Priorita("Bassa", -1), new Priorita("Normale", 0), new Priorita("Alta", 1), new Priorita("Massima", 2));
        ComboBox<Priorita> priorita = new ComboBox<Priorita>(options);
        priorita.valueProperty().addListener((obs, oldVal, newVal) ->
        {
            try {
                tDao.updatePriorita(task.getId(), newVal.getnPriorita());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        priorita.setConverter(new StringConverter<Priorita>() {

            @Override
            public String toString(Priorita object) {
                if (object != null)
                    return object.getP();
                return null;
            }

            @Override
            public Priorita fromString(String string) {
                return null;
            }
        });
        priorita.getSelectionModel().select(task.getPriorita() + 1);
        return priorita;
    }
}
