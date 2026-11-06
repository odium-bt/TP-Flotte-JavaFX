module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires jakarta.mail;


    opens fr.greta.tpflottejavafx to javafx.fxml;
    exports fr.greta.tpflottejavafx;
}