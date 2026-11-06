module fr.greta.tpflottejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires bcrypt;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires jakarta.mail;
    requires tools.jackson.databind;
    requires kernel;
    requires layout;

    exports application;
    opens application to javafx.fxml;
    exports controllers;
    opens controllers to javafx.fxml;
}