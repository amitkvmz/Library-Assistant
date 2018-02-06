package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public final class DatabaseHandler {

    private static DatabaseHandler handler;
    private static final String DB_Url = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    public DatabaseHandler() throws SQLException {
        createConnection();
        setupBookTable();
    }

    void createConnection() throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_Url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    void setupBookTable() {
        String TABLE_NAME = "BOOK";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
              System.out.println("Table" + TABLE_NAME + " already exists. we are ready");
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

        } catch (Exception e) {
            e.printStackTrace();
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
            JOptionPane.showMessageDialog(null, "Erro : " + ex.getMessage(), "error occured", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}
