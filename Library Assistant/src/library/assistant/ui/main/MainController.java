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
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

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

    public int renew_value;

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
    @FXML
    private ListView<String> issued_book_info_list;

    Boolean isBookReadyForSubmission = false;

    //  int renew_count = 0;
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
    private void settingsLoad(ActionEvent event) throws IOException {

        loadWindow("/library/assistant/settings/settings.fxml", "settings");
    }

    public void loadWindow(String loc, String title) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(loc));
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(title);
        LibraryAssistantUtil.setStageIcon(stage);
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
            author_output.setText("");
            status_output.setText("");

        }
    }

    void clearBookCache() {

        book_name_output.setText("Book Name");
        author_output.setText("Author Name");
        status_output.setText("Status");
        issued_book_info_list.getItems().clear();

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
            System.out.println("you cancelled to  issue " + bid + " to  " + mid);
        }
    }

    @FXML
    private void renew_book_info(ActionEvent event) throws SQLException {
        isBookReadyForSubmission = false;
        String bid = renew_book_id_get.getText();
        ResultSet rs = handler.execQuery("select * from ISSUE where bookId='" + bid + "'");
        ObservableList list = FXCollections.observableArrayList();

        while (rs.next()) {
            String mid = rs.getString("memberId");
            Timestamp ts = rs.getTimestamp("issueTime");
            renew_value = rs.getInt("renew_count");
            list.add("ISSUE DATA AND TIME : " + ts);
            list.add("Renew Count         : " + renew_value);

            list.add("Book Information");

            rs = handler.execQuery("SELECT * FROM BOOK WHERE ID = '" + bid + "'");
            while (rs.next()) {
                list.add("           Book Name           :  " + rs.getString("title"));
                list.add("           Book ID             :  " + bid);
                list.add("           Author              :  " + rs.getString("author"));
                list.add("           Book Publisher      :  " + rs.getString("publisher"));
            }
            System.out.println("got book info : success book id = " + bid + " member : " + mid);
            rs = handler.execQuery("SELECT * FROM MEMBER WHERE ID = '" + mid + "'");

            while (rs.next()) {

                list.add("Member's Informations ");
                list.add("            Member Name     :  " + rs.getString("name"));
                list.add("            Member ID       :  " + mid);
                list.add("            Member Contact  :  " + rs.getString("mobile"));

            }

            isBookReadyForSubmission = true;
        }
        issued_book_info_list.getItems().addAll(list);
    }

    @FXML
    private void book_submit(ActionEvent event) {
        System.out.println(isBookReadyForSubmission);
        if (!isBookReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select the book first");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to return the book " + book_name_output.getText());
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String bid = book_id_input.getText();
                if (handler.execAction("DELETE FROM ISSUE WHERE bookId = '" + bid + "'")
                        && handler.execAction("update book set isAvail = TRUE where id = '" + bid + "'")) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("book submitted successfully");
                    alert.showAndWait();
                    clearBookCache();
                    isBookReadyForSubmission = false;

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Book submission failed ");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Book submission cancelled ");
                alert.setHeaderText(null);
                alert.showAndWait();

            }
        }
    }

    @FXML
    private void renew_book(ActionEvent event) {
        System.out.println(isBookReadyForSubmission.toString());

        if (!isBookReadyForSubmission) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select the book first");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure want to Renew the book " + book_name_output.getText() + " to " + member_name_output.getText());

            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                String renewalquery = "update ISSUE set issueTime=CURRENT_TIMESTAMP, renew_count =" + (renew_value + 1) + " where bookId='" + book_id_input.getText() + "'";
                if (handler.execAction(renewalquery)) {
                    System.out.println(" query running : " + renewalquery);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("Book Renewal Successfull :  " + book_name_output.getText() + " to " + member_name_output.getText());
                    alert.showAndWait();
                    clearBookCache();
                    isBookReadyForSubmission = false;
                } else {
                    System.out.println("renewal failed because of update query " + renewalquery);

                }

            }
        }
    }

    @FXML
    private void menu_close_click(ActionEvent event) {
        ((Stage)add_book_btn.getScene().getWindow()).close();
    }

    @FXML
    private void menu_add_book_click(ActionEvent event) throws IOException {
         loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Add Book");

    }

    @FXML
    private void menu_add_members_click(ActionEvent event) throws IOException {
        loadWindow("/library/assistant/ui/addmember/add_member.fxml", "Add Member");

    }

    @FXML
    private void menu_view_books_click(ActionEvent event) throws IOException {
            loadWindow("/library/assistant/ui/listbook/list_book.fxml", "List Book");

    }

    @FXML
    private void menu_view_members_click(ActionEvent event) throws IOException {
            loadWindow("/library/assistant/ui/listmember/list_member.fxml", "List Members");

    }

    @FXML
    private void menu_view_fullscreen_click(ActionEvent event) {
    Stage  stage = (Stage)view_book_btn.getScene().getWindow();
    stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void menu_about_click(ActionEvent event) {
    
    }

    @FXML
    private void menu_file_settings_click(ActionEvent event) throws IOException {
           loadWindow("/library/assistant/settings/settings.fxml", "Settings");

    }

}
