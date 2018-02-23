/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.listmember;

import java.io.IOException;
import library.assistant.ui.listbook.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import library.assistant.ui.addmember.MemberAddController;
import library.assistant.util.LibraryAssistantUtil;

/**
 * FXML Controller class
 *
 * @author Amit
 */
public class MemberListController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    ObservableList<Member> list = FXCollections.observableArrayList();

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
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory("email"));

    }

    private void loadData() {
        try {
            list.clear();
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String query = "SELECT * FROM MEMBER";
            ResultSet rs = handler.execQuery(query);
            while (rs.next()) {
                String gname = rs.getString("name");
                String gid = rs.getString("id");
                String gmobile = rs.getString("mobile");
                String gemail = rs.getString("email");

                list.add(new Member(gname, gid, gmobile, gemail));
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
        //tableView.setItems(list);
    }

    @FXML
    private void context_menu_refresh(ActionEvent event) {
        loadData();
    }

    @FXML
    private void context_menu_edit(ActionEvent event) throws IOException {
        MemberListController.Member selectedforedit = tableView.getSelectionModel().getSelectedItem();
        if (selectedforedit == null) {
            AlertMaker.showErrorAlert("Error", "No Member selected.");
            return;
        }
        System.out.println("goint to load edit ");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addmember/add_member.fxml"));
        Parent parent = loader.load();

        MemberAddController controller = (MemberAddController) loader.getController();
        controller.infalateUI(selectedforedit);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Edit Member");
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
    private void context_menu_delete(ActionEvent event) throws SQLException {
        Member selectedMember = tableView.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            AlertMaker.showErrorAlert("No Member Selected ", "Please Select Member first ");
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Do you want to delete the Member : " + selectedMember);
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            Boolean result = DatabaseHandler.getInstance().deleteMember(selectedMember);
            if (result) {
                AlertMaker.showSimpleAlert("Success", "Member Successfully Deleted ");
                list.remove(selectedMember);
                loadData();
            } else {
                AlertMaker.showErrorAlert("Failed", "Failed to delete member");
            }
        } else {
            AlertMaker.showErrorAlert("Cancelled", "Member deletion cancelled");
            ((Stage) rootPane.getScene().getWindow()).close();

        }

    }

    public static class Member {

        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Member(String name, String id, String mobile, String email) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);

        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

    }

}
