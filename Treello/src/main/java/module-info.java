module FranV.Treello {
    requires javafx.controls;
    requires javafx.fxml;

    opens FranV.Treello to javafx.fxml;
    exports FranV.Treello;
}