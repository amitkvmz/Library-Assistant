/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.listmember;

import library.assistant.ui.listbook.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController.Book;

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
            DatabaseHandler handler = DatabaseHandler.getInstance();
            String query = "SELECT * FROM MEMBER";
            ResultSet rs = handler.execQuery(query);
            while (rs.next()) {
                String gname = rs.getString("name");
                String gid = rs.getString("id");
                String gmobile = rs.getString("mobile");
                String gemail = rs.getString("email");
                
                list.add(new Member(gname,gid,gmobile,gemail)); 
            }

        } catch (SQLException ex) {
            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableView.getItems().setAll(list);
    }

    public static class Member {

        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;
        
        Member(String name, String id, String mobile, String email) {
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
