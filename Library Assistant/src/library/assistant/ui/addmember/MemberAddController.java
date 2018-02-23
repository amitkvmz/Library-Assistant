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
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;
import library.assistant.ui.listmember.MemberListController;

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

    private Boolean isInEditMode = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void addMember(ActionEvent event) throws SQLException {
        String gname = name.getText();
        String gid = id.getText();
        String gmobile = mobile.getText();
        String gemail = email.getText();

        if (gname.isEmpty() || gid.isEmpty() || gmobile.isEmpty() || gemail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fill all data");
            alert.showAndWait();
            return;
        } else {
            if (isInEditMode) {
                handleMemberUpdate();
              
                
                return;
            }
            try {
                DatabaseHandler handler = DatabaseHandler.getInstance();

                String query = "INSERT INTO MEMBER VALUES ( '" + gid + "','" + gname + "','" + gmobile + "','" + gemail + "')";
                if (handler.execAction(query)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Successfully inserted");
                    alert.showAndWait();
                } else {
                    System.out.println("error in query : " + query);
                }

            } catch (SQLException ex) {
                Logger.getLogger(MemberAddController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage st = (Stage) rootPane.getScene().getWindow();
        st.close();
    }

    public void infalateUI(MemberListController.Member member) {
        System.out.println(member.getEmail() + " " + member.getId() + " " + member.getMobile() + " " + member.getName());
        System.out.println(email + " " + name + " " + mobile + " " + id);

        id.setText(member.getId());
        id.setEditable(false);
        email.setText(member.getEmail());
        mobile.setText(member.getMobile());
        name.setText(member.getName());
        isInEditMode = Boolean.TRUE;

    }

    private void handleMemberUpdate() throws SQLException {
        MemberListController.Member member = new MemberListController.Member(name.getText(), id.getText(), mobile.getText(), email.getText());
        if (DatabaseHandler.getInstance().memberUpdate(member)) {
            AlertMaker.showSimpleAlert("success", "Member Successfully updated");
            
//            ((Stage)title.getScene().getWindow()).close();

        } else {
            AlertMaker.showErrorAlert("Failed", "Failed to update Member");
        }
    }
}

