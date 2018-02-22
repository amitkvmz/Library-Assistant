package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import library.assistant.alert.AlertMaker;
import library.assistant.ui.listbook.BookListController.Book;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;
    private static final String DB_URL = "jdbc:derby:librarydatabase;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() throws SQLException {
        createConnection();

        setupIssueTable();
        setupBookTable();
        setupMemberTable();

    }

    public static DatabaseHandler getInstance() throws SQLException {
        try {
            if (handler == null) {
                handler = new DatabaseHandler();

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
        return handler;

    }

    void createConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Can't Load Database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

    }

    public boolean deleteBook(Book book) throws SQLException {
        try {
            String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteStatement);
            pstmt.setString(1, book.getId());
            System.out.println(book.getId());
            System.out.print(deleteStatement);
//          pstmt.executeUpdate();
            if (pstmt.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {

            AlertMaker.showErrorAlert("Failed ", "Failed to delete Book.");
            return false;
        }
        return false;

    }

    void setupBookTable() {
        String TABLE_NAME = "BOOK";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table   : " + TABLE_NAME + " already exists. we are ready");

//  stmt.execute("drop table "+TABLE_NAME);
                //       System.out.println("dropping table");
            } else {

                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary Key, \n"
                        + "     title varchar(200), \n   "
                        + "     author varchar(200), \n "
                        + "     publisher varchar(100), \n "
                        + "     isAvail boolean default true "
                        + " )");

            }

        } catch (SQLException e) {
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:handler " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    public Boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error : " + ex.getMessage(), "error occured", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    private void setupMemberTable() {
        String TABLE_NAME = "MEMBER";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table   : " + TABLE_NAME + " already exists. we are ready");
                //  stmt.execute("drop table "+TABLE_NAME);
                //       System.out.println("dropping table");
            } else {

                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary Key, \n"
                        + "     name varchar(200), \n   "
                        + "     mobile varchar(200), \n "
                        + "     email varchar(100) \n "
                        + " )");

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupIssueTable() {
        String TABLE_NAME = "ISSUE";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table : " + TABLE_NAME + " already exists. we are ready");
                //  stmt.execute("drop table "+TABLE_NAME);
                //       System.out.println("dropping table");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     bookId varchar(200) primary Key, \n"
                        + "     memberId varchar(200), \n   "
                        + "     issueTime timestamp default CURRENT_TIMESTAMP  ,\n "
                        + "     renew_count integer default 0, \n "
                        + "     FOREIGN KEY (bookId) REFERENCES BOOK(id), \n"
                        + "     FOREIGN KEY (memberId) REFERENCES MEMBER(id) \n"
                        + " )");

                System.out.println("issue table created for the first time");
            }

        } catch (SQLException e) {
        }

    }
}
