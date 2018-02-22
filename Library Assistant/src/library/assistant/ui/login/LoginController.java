/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField uname;
    @FXML
    private JFXPasswordField pword;
    @FXML
    private JFXButton login_btn;
    @FXML
    private JFXButton cancel_btn;
    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        label.setStyle("-fx-background-color:#6a89cc");
       // label.setStyle("-fx-border-radius:3");
        JFXDepthManager.setDepth(label, 3);
       // Stage stage = (Stage)uname.getScene().getWindow();
       // LibraryAssistantUtil.setStageIcon(stage);
    }

    @FXML
    private void login_btn_click(ActionEvent event) throws IOException {
        Preferences preferences = Preferences.getPreferences();
        String username = uname.getText();
        String password = pword.getText();
        String encrpwd = DigestUtils.sha1Hex(password);
        if (username.equals(preferences.getUsername()) && encrpwd.equals(preferences.getPassword())) {
            MainController mctrl = new MainController();
            ((Stage) cancel_btn.getScene().getWindow()).close();
            mctrl.loadWindow("/library/assistant/ui/main/main.fxml", "Library Assistant");

        } else {
            label.setStyle("-fx-background-color:#e55039");
            label.setText("Invalid Login, Please try again. ");
        }
    }

    @FXML
    private void cancel_btn_click(ActionEvent event) {
        ((Stage) cancel_btn.getScene().getWindow()).close();

    }

}
