package fr.greta.tpflottejavafx.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stockage et chargement de données JSON
 */
public class StockageJson {
    private static final JsonMapper MAPPER = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    private static final TypeReference<Map<String, String>> TYPE_MAP = new TypeReference<>() {};

    private final Path chemin;

    public StockageJson(Path chemin) {
        this.chemin = chemin;
    }

    public Path getChemin() {
        return chemin;
    }

    /** Lit un fichier du système de fichiers ; renvoie une map vide s'il n'existe pas encore. */
    public Map<String, String> charger() {
        File f = chemin.toFile();
        if (!f.exists()) {
            return new HashMap<>();
        }
        return MAPPER.readValue(f, TYPE_MAP);
    }

    /** Lit un fichier JSON placé dans src/main/resources (classpath). */
    public static Map<String, String> chargerRessource(String nomRessource) throws IOException {
        try (InputStream in = StockageJson.class.getClassLoader().getResourceAsStream(nomRessource)) {
            if (in == null) {
                throw new FileNotFoundException("Ressource introuvable : " + nomRessource);
            }
            return MAPPER.readValue(in, TYPE_MAP);
        } catch (JacksonException e) {
            throw new IOException("JSON invalide dans " + nomRessource, e);
        }
    }
}
