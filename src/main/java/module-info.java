module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.greta.tpflottejavafx to javafx.fxml;
    exports fr.greta.tpflottejavafx;
}