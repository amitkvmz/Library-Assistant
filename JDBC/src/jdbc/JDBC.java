package jdbc;

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBC {

    static String name = "";
    static String contact = "";
    static String email = "";

    public static void main(String[] args) throws SQLException {
        askUserInfo();

        // getWorldData();
        insertData();
        System.out.println("Going to retrive data : ");

        System.out.println("Do you want to retrieve data ? ");
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        if (ans.equals("yes")) {

            retrieveData();
        } else {
            System.exit(0);
        }

    }

    private static void askUserInfo() {
        System.out.println("Enter your Name ?");
        Scanner sc = new Scanner(System.in);
        name = sc.nextLine();
        System.out.println("Enter your contact ?");
        contact = sc.nextLine();
        System.out.println("Enter your email ?");
        email = sc.nextLine();

    }

    private static void insertData() throws SQLException {
        try {

            DBConnect db = new DBConnect();
            Connection con = (Connection) db.getInstance();
            Statement st = con.createStatement();
            String query = "insert into emp_info (emp_name,emp_contact,emp_email) values('" + name + "','" + contact + "','" + email + "')";

            if (st.executeUpdate(query) > 0) {
                System.out.println("successfully inserted ");
            } else {
                System.out.println("failed to insert here's your query : " + query);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getWorldData() {
        try {
            DBConnect db = new DBConnect();
            Connection conn = (Connection) db.getInstance();
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM WORLD.COUNTRY where code ='IND'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String cname = rs.getString("Name");
                String ccontinent = rs.getString("Continent");
                String cregion = rs.getString("Region");

                System.out.println("country : " + cname + " Continent : " + ccontinent + " Region : " + cregion);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("failed to retrieve data .");
        }
    }

    private static void retrieveData() {
        try {
            DBConnect db = new DBConnect(); // creating object of DBConnect so that we can use its methods and datas..

            Connection conn = (Connection) db.getInstance(); // calling getInstance method using db object, which retruns Connection as object.
            Statement stmt = conn.createStatement(); // Creating statement using conneciton.

            ResultSet rs = stmt.executeQuery(" SELECT * from emp_info"); // getting all the required data by executing query which returns RESULT SeT as an object.
            while (rs.next()) {         // looping through all the data which we have retrieved.
                String e_id = rs.getString("emp_id");
                String e_name = rs.getString("emp_name");
                String e_conntact = rs.getString("emp_contact");
                String e_email = rs.getString("emp_email");
                System.out.println(" id  = "+e_id+" name = "+e_name+" contact = "+e_conntact+" email = "+e_email);
                        

            }

        } catch (Exception e) {

        }

    }
}
