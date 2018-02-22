/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;

/**
 *
 * @author Amit
 */
public class SettingsLoader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/settings/settings.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.show();
        
        new Thread(() -> {
            try {
                DatabaseHandler.getInstance();
            } catch (SQLException ex) {
                Logger.getLogger(SettingsLoader.class.getName()).log(Level.SEVERE, null, ex);
                AlertMaker.showErrorMessage(ex, "Failed", "couldn't save the settings. ");
            }
        }).start();
       // DatabaseHandler.getInstance();
      // Preferences.initConfig();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
