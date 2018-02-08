/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;

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
    @FXML
    private HBox book_info;
    @FXML
    private VBox book_info_label;
    @FXML
    private HBox member_info;
    @FXML
    private VBox member_info_label;
    @FXML
    private TextField book_id_input;
    @FXML
    private Text book_name_output;
    @FXML
    private Text author_output;
    @FXML
    private Text status_output;
    @FXML
    private TextField member_id_input;
    @FXML
    private Text member_name_output;
    @FXML
    private Text mobile_output;

    /**
     * Initializes the controller class.
     */
    DatabaseHandler handler = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            JFXDepthManager.setDepth(book_info, 2);
            JFXDepthManager.setDepth(member_info, 2);
            JFXDepthManager.setDepth(book_info_label, 2);
            JFXDepthManager.setDepth(member_info_label, 2);
            handler = DatabaseHandler.getInstance();
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addBookLoad(ActionEvent event) throws IOException {
        loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Add Book");

    }

    @FXML
    private void addMemberLoad(ActionEvent event) throws IOException {

        loadWindow("/library/assistant/ui/addmember/add_member.fxml", "Add Member");
    }

    @FXML
    private void viewBookLoad(ActionEvent event) throws IOException {

        loadWindow("/library/assistant/ui/listbook/list_book.fxml", "List Book");
    }

    @FXML
    private void viewMemberLoad(ActionEvent event) throws IOException {

        loadWindow("/library/assistant/ui/listmember/list_member.fxml", "List Members");
    }

    @FXML
    private void settingsLoad(ActionEvent event) {

        // loadWindow("/library/assistant/ui/addbook/add_book.fxml","Add Book
    }

    public void loadWindow(String loc, String title) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        stage.show();
    }

    @FXML
    private void getBookInfo(ActionEvent event) throws SQLException, InterruptedException {
        String gbookid = book_id_input.getText();
        String query = "Select * from book where id='" + gbookid + "'";
        ResultSet rs = handler.execQuery(query);
        Boolean flag = false;
        while (rs.next()) {
            Boolean status = rs.getBoolean("isAvail");
            //book_id_input.setText("");

            book_name_output.setText(rs.getString("title"));
            author_output.setText(rs.getString("author"));

            status_output.setText((rs.getBoolean("isAvail")) ? "Available" : "Not Available");
            flag = true;

        }
        if (!flag) {
            clearBookCache();
            book_name_output.setText("No such Book Available");
        }
    }

    void clearBookCache() {
        
            book_name_output.setText("");
            author_output.setText("");

            status_output.setText("");

    }

    void clearMemberCache() {
        member_name_output.setText("");
        mobile_output.setText("");
    }

    @FXML
    private void getMemberInfo(ActionEvent event) throws SQLException {
        String gmemberid = member_id_input.getText();
        String query = "Select * from member where id='" + gmemberid + "'";
        ResultSet rs = handler.execQuery(query);
        Boolean flag = false;
        while (rs.next()) {
            member_name_output.setText(rs.getString("name"));
            mobile_output.setText(rs.getString("mobile"));
            flag = true;

        }
        if (!flag) {
            clearMemberCache();
            member_name_output.setText("No such Member Available");

        }
    }

}
