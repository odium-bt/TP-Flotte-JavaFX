package fr.greta.tpflottejavafx.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Mailer {
    private final Logger log = LogManager.getLogger(String.valueOf(Mailer.class));

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final String senderMail;
    private final String senderName;
    private final String key;

    private String destinataire;
    private String sujet;
    private String corps;

    public Mailer(String propertiesPath, String mailPath) throws IOException {
        // Identifiants de l'envoyeur :
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input == null) {
                throw new FileNotFoundException("Fichier introuvable : " + propertiesPath);
            }

            properties.load(input);
        }

        senderMail = properties.getProperty("sender");
        senderName = properties.getProperty("name");
        key = properties.getProperty("key");

        // Propriétés du mail :
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(mailPath)) {
            if (input == null) {
                throw new FileNotFoundException("Fichier introuvable : " + mailPath);
            }

            JsonNode json = mapper.readTree(new File(mailPath));

            destinataire = json.get("destinataire").asString();
            sujet = json.get("sujet").asString();
            corps = json.get("corps").asString();

        }
    }

    public void send(File attachment) throws UnsupportedEncodingException, MessagingException {
        // Configuration des propriétés SMTP pour Gmail
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Authentification
        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderMail, key);
            }
        });

        // Création du message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderMail, senderName));
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(destinataire)
        );
        message.setSubject(sujet);

        // Création du contenu
        message.setText(corps);

        // Envoi du message
        Transport.send(message);
    }
}