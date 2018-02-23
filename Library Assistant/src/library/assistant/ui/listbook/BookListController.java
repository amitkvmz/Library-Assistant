/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.listbook;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.main.Main;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class BookListController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    ObservableList<Book> list = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory("availability"));

    }

    private void loadData() {
        try {
            list.clear();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String query = "SELECT * FROM BOOK";
            ResultSet rs = handler.execQuery(query);
            while (rs.next()) {
                String gtitle = rs.getString("title");
                String gid = rs.getString("id");
                String gauthor = rs.getString("author");
                String gpublisher = rs.getString("publisher");
                Boolean gavailability = rs.getBoolean("isAvail");
                list.add(new Book(gtitle, gid, gauthor, gpublisher, gavailability));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.setItems(list);   //   tableView.getItems().setAll(list);
    }

    @FXML
    private void context_menu_delete(ActionEvent event) throws SQLException {
        Book bookselected = tableView.getSelectionModel().getSelectedItem();
        if (bookselected == null) {
            AlertMaker.showErrorAlert("Error", "No book Selected.");
            return;
        }

        if (!DatabaseHandler.getInstance().isBookAlreadyIssued(bookselected)) {
            System.out.println(DatabaseHandler.getInstance().isBookAlreadyIssued(bookselected));
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setContentText("Are you sure want to delete the " + bookselected.getTitle() + " ?");
            Optional<ButtonType> answer = alert.showAndWait();
            if (answer.get() == ButtonType.OK) {
                Boolean result = DatabaseHandler.getInstance().deleteBook(bookselected);
                if (result) {
                    AlertMaker.showSimpleAlert("Success", "Book has been deleted successfully.");
                    list.remove(bookselected);
                } else {
                    AlertMaker.showErrorAlert("Failed", "Failed to delete Book");
                }
            } else {
                AlertMaker.showSimpleAlert("Cancelled", "You cancelled the book deletion");
            }

//        } else {
//            Alert alert = new Alert(AlertType.ERROR);
//            alert.setHeaderText(null);
//            alert.setContentText("Book already issued, Can't be deleted.");
//            alert.showAndWait();
//        }
//       
            //  System.out.println("already issued");
        } else {
            AlertMaker.showErrorAlert("Can't issue ", "Can't Book is already issued to someone");

        }
    }

    @FXML
    private void context_menu_edit(ActionEvent event) throws IOException {
        Book selectedforedit = tableView.getSelectionModel().getSelectedItem();
        if (selectedforedit == null) {
            AlertMaker.showErrorAlert("Error", "No book selected.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"));
        Parent parent = loader.load();

        BookAddController controller = (BookAddController) loader.getController();
        controller.infalateUI(selectedforedit);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Edit Book");
        LibraryAssistantUtil.setStageIcon(stage);
        stage.setScene(new Scene(parent));
        stage.show();
        stage.setOnCloseRequest(e -> {
            System.out.println("refreshinng data");
            context_menu_refresh(new ActionEvent());
        });

//        
//        MainController controller = new MainController();
//        BookAddController bac = new BookAddController();
//        bac.infalateUI(selectedforedit);
//        
//        controller.loadWindow("/library/assistant/ui/addbook/add_book.fxml", "Edit Book");
//        System.out.println("loaded eddit winodqw");
//       
    }

    @FXML
    private void context_menu_refresh(ActionEvent actionEvent) {
        loadData();
    }

    public static class Book {

        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availability;

        public Book(String title, String id, String author, String publisher, Boolean availability) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(availability);
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Boolean getAvailability() {
            return availability.get();
        }

    }

}
