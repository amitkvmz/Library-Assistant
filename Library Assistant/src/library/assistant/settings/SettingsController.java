/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField daysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton save_btn;
    @FXML
    private JFXButton cancel_btn;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initDefaultValues();
        } catch (IOException ex) {
            Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void saveBtnClick(ActionEvent event) throws IOException {
        Preferences preferences = Preferences.getPreferences();
        preferences.setnDaysWithoutFine(Integer.parseInt(daysWithoutFine.getText()));
        preferences.setFinePerDay(Integer.parseInt(finePerDay.getText()));
        preferences.setUsername(username.getText());
        preferences.setPassword(password.getText());

        Preferences.writePreferencesToFile(preferences);
        AlertMaker.showSimpleAlert("", "New Settings have been saved.");
        ((Stage)save_btn.getScene().getWindow()).close();
        
    }

    @FXML
    private void cancelBtnClick(ActionEvent event) {
        Stage st = (Stage)anchorPane.getScene().getWindow();
        st.close();
    }

    private void initDefaultValues() throws IOException {
        Preferences preferences = Preferences.getPreferences();
        daysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        username.setText(preferences.getUsername());
        password.setText(preferences.getPassword());
    }

}
