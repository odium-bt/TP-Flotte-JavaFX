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

    exports fr.greta.tpflottejavafx.application;
    opens fr.greta.tpflottejavafx.application to javafx.fxml;
    exports fr.greta.tpflottejavafx.controllers;
    opens fr.greta.tpflottejavafx.controllers to javafx.fxml;
    exports fr.greta.tpflottejavafx.service;
    opens fr.greta.tpflottejavafx.service to javafx.fxml;
    opens fr.greta.tpflottejavafx.modeles to javafx.base;
}