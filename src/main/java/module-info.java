module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;


    opens fr.greta.tpflottejavafx to javafx.fxml;
    exports fr.greta.tpflottejavafx;
}