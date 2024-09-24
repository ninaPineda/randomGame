package com.ninaPineda.app;
import java.io.*;
import java.util.*;

public class HighscoreManager {

    private static final String HIGHSCORE_FILE = "src/resources/highscores.txt";
    private static HighscoreManager instance;  // Singleton-Instanz
    private Map<String, Integer> highscores;

    // Privater Konstruktor, um direkte Instanziierung zu verhindern
    private HighscoreManager() {
        highscores = new HashMap<>();
        loadHighscores();
    }

    // Methode zum Abrufen der Singleton-Instanz
    public static synchronized HighscoreManager getInstance() {
        if (instance == null) {
            instance = new HighscoreManager();
        }
        return instance;
    }

    public Map<String, Integer> getHighscores() {
        return sortHighscoresByValue(highscores);
    }

    private Map<String, Integer> sortHighscoresByValue(Map<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(unsortedMap.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Absteigende Sortierung

        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public String getHighscoresAsHtml() {
        StringBuilder highscoreHtml = new StringBuilder();
        for (Map.Entry<String, Integer> entry : getHighscores().entrySet()) {
            highscoreHtml.append("<br>")
                         .append(entry.getKey() + ": ")
                         .append(entry.getValue())
                         .append("<br>");
        }
        return highscoreHtml.toString();
    }

    // Highscore hinzufügen oder aktualisieren
    public void addHighscore(String name, int score) {
        highscores.put(name, score);
        saveHighscores();
    }

    // Highscores laden
    private void loadHighscores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    highscores.put(name, score);
                }
            }
        } catch (IOException e) {
            System.out.println("Aktuelles Arbeitsverzeichnis: " + System.getProperty("user.dir"));
            System.out.println("Keine vorhandene Highscore-Datei gefunden. Neue Datei wird erstellt.");
        }
    }

    public List<Map.Entry<String, Integer>> getSortedHighscores() {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(highscores.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        return sortedList;
    }

    // Highscores speichern
    private void saveHighscores() {
        // Holen der sortierten Highscores
        List<Map.Entry<String, Integer>> sortedHighscores = getSortedHighscores();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            for (Map.Entry<String, Integer> entry : sortedHighscores) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Highscores anzeigen
    public void displayHighscores() {
        for (Map.Entry<String, Integer> entry : highscores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
