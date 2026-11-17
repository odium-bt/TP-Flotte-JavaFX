package fr.greta.tpflottejavafx.util;

import fr.greta.tpflottejavafx.dao.DAOFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Création de mail avec pièce jointe
 */
public class Email {
    private final Logger log = LogManager.getLogger(String.valueOf(Email.class));

    private String nomExpediteur;
    private String expediteur;
    private String nomDestinataire;
    private String destinataire;
    private String sujet;
    private String corps;


    // Les erreurs sont propagées à l'appelant : c'est lui qui sait si l'envoi a réussi ou échoué
    public void sendEmail(Path pieceJointe, String mailPath) throws IOException {
        //Clé API
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        final String CONSTANTES_PROPERTIES = "mail2.properties";
        Properties props = new Properties();
        try (InputStream in = DAOFactory.class.getClassLoader().getResourceAsStream(CONSTANTES_PROPERTIES)) {
            // Fichier non présent, on lève une exception
            if (in == null) {
                throw new IOException();
            }
            // Chargement des propriétés
            props.load(in);
        } catch (IOException e) {
            log.error("Erreur : ", e);
        }
        // Récuperation du paramètre
        String apikey = props.getProperty("API_KEY");

        // Configuration de la clé API
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(apikey);

        //Récupération des données d'envoi du mail
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(mailPath)) {
            if (input == null) {
                throw new FileNotFoundException("Fichier introuvable : " + mailPath);
            }

            Map<String, String> dataEnvoiMail = StockageJson.chargerRessource("mail.json");

            nomExpediteur = dataEnvoiMail.get("expediteur-nom");
            expediteur = dataEnvoiMail.get("expediteur");
            nomDestinataire = dataEnvoiMail.get("destinataire-nom");
            destinataire = dataEnvoiMail.get("destinataire");
            sujet = dataEnvoiMail.get("sujet");
            corps = dataEnvoiMail.get("corps");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            // Expéditeur
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(expediteur);
            sender.setName(nomExpediteur);

            // Destinataire principal
            List<SendSmtpEmailTo> toList = new ArrayList<>();
            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(destinataire);
            to.setName(nomDestinataire);
            toList.add(to);

            // Construction de l'e-mail
            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setSubject(sujet);
            sendSmtpEmail.setHtmlContent(corps);
            sendSmtpEmail.addAttachmentItem(creerPieceJointe(pieceJointe));

            // Envoi de l'e-mail
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
            log.info("E-mail envoyé avec succès, identifiant : {}", response.getMessageId());

        } catch (ApiException e) {
            throw new IOException("Erreur API Brevo " + e.getCode() + " : " + e.getResponseBody(), e);
        }
    }

    //Le SDK encode le contenu en base64 lors de l'envoi : on lui donne simplement les octets du fichier
    SendSmtpEmailAttachment creerPieceJointe(Path fichier) throws IOException {
        SendSmtpEmailAttachment pieceJointe = new SendSmtpEmailAttachment();
        pieceJointe.setName(fichier.getFileName().toString());
        pieceJointe.setContent(Files.readAllBytes(fichier));
        return pieceJointe;
    }
}