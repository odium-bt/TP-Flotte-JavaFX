module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.sql;


    opens fr.greta.tpflottejavafx to javafx.fxml;
    exports fr.greta.tpflottejavafx;
    exports controllers;
    opens controllers to javafx.fxml;
}