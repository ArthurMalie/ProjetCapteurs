package Connexion;

import java.sql.*;

public class Connexion {

    private final String address;
    private final String username;
    private final String password;
    private boolean connected;
    private Connection connection;
    private Statement statement;

    public Connexion ( String address, String username, String password ) {
        this.connected = false;
        this.address = address;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    /*
    ____________________________
        CONNECTION & ERREURS
    ____________________________
    */

    public boolean connect () {

        System.out.println("-------- MySQL JDBC Connection Testing ------");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            this.connected = false;
            System.err.println("Could not find the MySQL JDBC Driver");
            e.printStackTrace();
            return false;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println("MySQL JDBC Driver Registered!");

        try {
            Connection conn =
                    DriverManager.getConnection("jdbc:mysql://localhost:3306/capteur" +
                            "user=root&password="); }
        catch (SQLException e) {

            this.connected = false;
            System.err.println("Connection Failed!");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
            return false;

        }

        if (connection != null) {

            this.connected = true;
            System.out.println("Connection successfully established!");
            return true;

        } else {

            this.connected = false;
            System.err.println("Connection Failed!");
            return false;

        }
    }

    public ResultSet executeQuery ( String query ) {

        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Could not execute query : " + query);
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate ( String query ) {

        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Could not execute update : " + query);
            e.printStackTrace();
            return 0;
        }
    }

    /*
    ____________________________
        GETTERS & SETTERS
    ____________________________
    */

    public boolean isConnected () {
        return connected;
    }

    public void setConnected ( boolean connected ) {
        this.connected = connected;
    }

    public String getAddress () {
        return address;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

    /*
    ____________________________
        REQUESTS SQL
    ____________________________
    */


}
