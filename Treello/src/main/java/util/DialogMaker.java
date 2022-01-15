package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class DialogMaker {
     public static Optional<ButtonType> createDeleteAlert(String s, String s2) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(s);
        alert.setHeaderText(s2);
        alert.setContentText("Sei sicuro ?");
        return alert.showAndWait();
    }
    public static Optional<String> createDialog(String prompText,String title,String header, String context) {
        TextInputDialog dialog = new TextInputDialog(prompText);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(context);
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result;
    }
    public static Optional<String> textInputDialog(String s, String s2, String s3) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(s);
        dialog.setHeaderText(s2);
        dialog.setContentText(s3);
        // Traditional way to get the response value.
        return dialog.showAndWait();
    }

    public static void informationAlert(String s) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        // set content text
        a.setContentText(s);
        a.show();
        System.out.println("Salva");
    }
}
