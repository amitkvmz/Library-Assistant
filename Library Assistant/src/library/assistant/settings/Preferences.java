/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author Amit
 */
public final class Preferences {

    int nDaysWithoutFine;
    int finePerDay;
    String username;
    String password;
    public static final String CONFIG_FILE = "config.json";

    Preferences() {
        nDaysWithoutFine = 14;
        finePerDay = 2;
        username = "admin";
        setPassword("admin");
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public int getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(int finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 16) {

            this.password = DigestUtils.sha1Hex(password);

        } else {
            this.password = password;
        }
    }

    public static void initConfig() throws IOException {
        Preferences preferences = new Preferences();
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(CONFIG_FILE)) {
            gson.toJson(preferences, writer);
        }
    }

    public static Preferences getPreferences() throws FileNotFoundException, IOException {
        Gson gson = new Gson();
        Preferences preferences = new Preferences();
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        } catch (FileNotFoundException filenotfound) {
            initConfig();
        }
        return preferences;

    }

    public static void writePreferencesToFile(Preferences preferences) throws IOException {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);

        } catch (JsonIOException | IOException ex) {

        } finally {
            try {
                writer.close();
            } catch (IOException ex) {

            }

        }
    }

    void writePreferencesToFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
