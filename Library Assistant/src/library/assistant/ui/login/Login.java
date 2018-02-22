/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.login;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

/**
 *
 * @author Amit
 */
public class Login extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/login.fxml"));
        LibraryAssistantUtil.setStageIcon(stage);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        new Thread(() -> {
            try {
                DatabaseHandler.getInstance();
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        // DatabaseHandler.getInstance();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
