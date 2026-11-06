module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.sql;
    requires java.compiler;


    opens fr.greta.tpflottejavafx to javafx.fxml;
    exports fr.greta.tpflottejavafx;
}