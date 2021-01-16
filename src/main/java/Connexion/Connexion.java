package Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connexion {

    private final String address;
    private final String username;
    private final String password;
    private boolean connected;
    private Connection connection;
    private Statement statement;

    public Connexion(String address, String username, String password) {
        this.connected = false;
        this.address = address;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    /*
    ____________________________
        CONNECTION & ERRORS
    ____________________________
    */

    public boolean connect() {

        System.out.println("-------- MySQL JDBC Connection Testing ------");

        /*try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();

        } catch (Exception e) {

            this.connected = false;
            System.err.println("Could not find the MySQL JDBC Driver");
            e.printStackTrace();
            return false;

        }
*/
        System.out.println("MySQL JDBC Driver Registered!");

        try {

            connection = DriverManager.getConnection(address + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC", username, password);
            statement = connection.createStatement();

        } catch (SQLException e) {

            this.connected = false;
            System.err.println("Connection Failed!");
            e.printStackTrace();
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

    public ResultSet executeQuery(String query) {

        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("Could not execute query : " + query);
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String query) {

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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /*
    ____________________________
        SQL REQUESTS
    ____________________________
    */

    public String[] getAllBatiments () {
        List<String> list = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT DISTINCT NOMB FROM LIEU ORDER BY NOMB");
        try {
            while (resultSet.next())
                list.add(resultSet.getString("NOMB"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));
    }

    public String[] getAllCapteursId () {
        List<String> list = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT DISTINCT NOMB FROM LIEU ORDER BY NOMB");
        try {
            while (resultSet.next())
                list.add("Capteur " + resultSet.getInt("IDC"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));
    }

    public String[] getAllCapteursFiltres(boolean airComprime, boolean eau, boolean electricite, boolean temperature){
        List<String> list = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            while (resultSet.next())
                if (airComprime) {
                    resultSet = executeQuery("SELECT DISTINCT IDC FROM CAPTEUR WHERE TYPEF=AIR_COMPRIME");
                    list.add("Capteur " + resultSet.getInt("IDC"));
                }
                if (eau){
                    resultSet = executeQuery("SELECT DISTINCT IDC FROM CAPTEUR WHERE TYPEF=EAU");
                    list.add("Capteur " + resultSet.getInt("IDC"));
                }
                if (electricite){
                    resultSet = executeQuery("SELECT DISTINCT IDC FROM CAPTEUR WHERE TYPEF=ELECTRICITE");
                    list.add("Capteur " + resultSet.getInt("IDC"));
                }
                if (temperature){
                    resultSet = executeQuery("SELECT DISTINCT IDC FROM CAPTEUR WHERE TYPEF=TEMPERATURE");
                    list.add("Capteur " + resultSet.getInt("IDC"));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));
    }

    public String[] getAllCapteursFiltres(String filtres){
        List<String> list = new ArrayList<>();


        return list.toArray((new String[list.size()]));
    }


}
