/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;
import library.assistant.ui.listbook.BookListController.Book;

/**
 *
 * @author Amit
 */
public class BookAddController implements Initializable {

    private Label label;
    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Boolean isInEditMode = Boolean.FALSE ;

    DatabaseHandler databaseHandler;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            databaseHandler = DatabaseHandler.getInstance();
            checkData();

        } catch (SQLException ex) {
        }

    }

    @FXML
    private void addBook(ActionEvent event) throws SQLException {

        String bookID = id.getText();
        String bookAuthor = author.getText();
        String bookName = title.getText();
        String bookPublisher = publisher.getText();
        System.out.println(bookID + " " + bookAuthor + " " + bookName + " " + bookPublisher);

        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookName.isEmpty() || bookPublisher.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter all fields . ");

        } else {

//                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
//                        + "     id varchar(200) primary Key, \n"
//                        + "     title varchar(200), \n   "
//                        + "     author varchar(200), \n "
//                        + "     publisher varchar(100), \n "
//                        + "     isAvail boolean default true "
//                        + " )");
            if (isInEditMode) {
                handleUpdateBookOperation();
                return;
            }

            String query = "INSERT INTO book VALUES ( '" + bookID + "','" + bookName + "','" + bookAuthor + "','" + bookPublisher + "',true)";
            if (databaseHandler.execAction(query)) {
                // System.out.println("i failed here");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Success ");
                alert.showAndWait();
                System.out.println("inserted");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Failed ");
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

    public void infalateUI(BookListController.Book book) {
        System.out.println(book.getPublisher() + " " + book.getId() + " " + book.getAuthor() + " " + book.getTitle());
        System.out.println(author + " " + publisher + " " + title + " " + id);

        id.setText(book.getId());
        id.setEditable(false);
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        title.setText(book.getTitle());
        isInEditMode = Boolean.TRUE;
    }

    private void checkData() throws SQLException {
        String qu = "Select title from BOOK";
        ResultSet rs = databaseHandler.execQuery(qu);
        while (rs.next()) {
            System.out.println(rs.getString("title"));
        }
    }

    private void handleUpdateBookOperation() throws SQLException {
        BookListController.Book book = new BookListController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
        if(DatabaseHandler.getInstance().bookUpdate(book)) {
            AlertMaker.showSimpleAlert("success","Book Successfully updated" );
//            ((Stage)title.getScene().getWindow()).close();
            
        }else {
            AlertMaker.showErrorAlert("Failed", "Failed to update Book");
        }
    }
}
