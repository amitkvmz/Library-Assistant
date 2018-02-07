/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class MainController implements Initializable {

    @FXML
    private JFXButton add_book_btn;
    @FXML
    private JFXButton add_member_btn;
    @FXML
    private JFXButton view_book_btn;
    @FXML
    private JFXButton view_members_btn;
    @FXML
    private JFXButton settings_btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addBookLoad(ActionEvent event) throws IOException {
        loadWindow("/library/assistant/ui/addbook/add_book.fxml","Add Book");
        
    }

    @FXML
    private void addMemberLoad(ActionEvent event) throws IOException {
        
        loadWindow("/library/assistant/ui/addmember/add_member.fxml","Add Member");
    }

    @FXML
    private void viewBookLoad(ActionEvent event) throws IOException {
        
        loadWindow("/library/assistant/ui/listbook/list_book.fxml","List Book");
    }

    @FXML
    private void viewMemberLoad(ActionEvent event) throws IOException {
        
        loadWindow("/library/assistant/ui/listmember/list_member.fxml","List Members");
    }

    @FXML
    private void settingsLoad(ActionEvent event) {
        
       // loadWindow("/library/assistant/ui/addbook/add_book.fxml","Add Book
    }
    
    public void loadWindow(String loc , String title) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }
}
