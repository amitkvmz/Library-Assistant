/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
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
    @FXML
    private JFXButton issue_btn;
    @FXML
    private JFXTextField renew_book_id_get;
    @FXML
    private JFXButton renew_btn;
    @FXML
    private JFXButton submit_btn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            JFXDepthManager.setDepth(book_info, 2);
            JFXDepthManager.setDepth(member_info, 2);
             JFXDepthManager.setDepth(book_id_input, 2);
             JFXDepthManager.setDepth(member_id_input, 2);
            
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

        // loadWindow("/library/assistant/ui/addbook/add_book.fxml","Add Book");
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

    @FXML
    private void bookIssue(ActionEvent event) {
        String bid = book_id_input.getText();
        String mid = member_id_input.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("confirm");
        alert.setContentText("Are you sure you want to issue the book : " + bid + " to " + mid);

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String update_issue = "INSERT INTO ISSUE (bookId,memberId) values('" + bid + "','" + mid + "')";
            String update_book = "UPDATE BOOK SET isAvail=false where id = '" + bid + "'";
            if (handler.execAction(update_issue)) {
                System.out.println("update_issue table success ");

                //handler.execQuery(update_book);
                if (handler.execAction(update_book)) {

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Success");
                    alert.setContentText("Successfully issued  Book : " + bid + " to " + mid);
                    alert.showAndWait();

                }
            } else {
                System.out.println(update_issue + "  " + update_book);
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error Message");
                alert.setContentText("Book issue Failed  " + bid + " to " + mid);
                System.out.println("error in query  : " + update_issue + " " + update_book);
                alert.showAndWait();

            }
        } else {
            System.out.println("you cancelled to  issue " + bid + " " + mid);
        }
    }

    @FXML
    private void renew_book_info(ActionEvent event) {
        String bid = renew_book_id_get.getText();
        ResultSet rs = handler.execQuery("select * from BOOK");
        while(rs.next())
    }

    @FXML
    private void book_submit(ActionEvent event) {
    }

    @FXML
    private void renew_book(ActionEvent event) {
    }

}
