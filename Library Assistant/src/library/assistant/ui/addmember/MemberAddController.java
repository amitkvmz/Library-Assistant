/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class MemberAddController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        
    }    

    @FXML
    private void addMember(ActionEvent event) {
        String gname = name.getText();
        String gid = id.getText();
        String gmobile = mobile.getText();
        String gemail = email.getText();
        
        if(gname.isEmpty()||gid.isEmpty()||gmobile.isEmpty()||gemail.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill all data");
            alert.showAndWait();
            return;
        }else {
            try {
                DatabaseHandler handler = DatabaseHandler.getInstance();
                
                String query = "INSERT INTO MEMBER VALUES ( '"+gid+"','"+gname+"','"+gmobile+"','"+gemail+"')";
                if(handler.execAction(query)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Successfully inserted");
                    alert.showAndWait();
                }else {
                    System.out.println("error in query : "+query);
                }        
                       
                
                
            } catch (SQLException ex) {
                Logger.getLogger(MemberAddController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        
        
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage st = (Stage)rootPane.getScene().getWindow();
        st.close();
    }
    
}
