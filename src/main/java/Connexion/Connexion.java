package Connexion;

import Mapping.Capteur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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

    public String[] getAllBatiments() {
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

    public String[] getAllCapteursId() {
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

    public String[] getAllCapteursFiltres(boolean airComprime, boolean eau, boolean electricite, boolean temperature) {
        List<String> list = new ArrayList<>();
        String fluides = "";
        if (airComprime)
            fluides += "'AIR_COMPRIME',";
        if (eau)
            fluides += "'EAU',";
        if (electricite)
            fluides += "'ELECTRICITE',";
        if (temperature)
            fluides += "'TEMPERATURE',";
        if (fluides.length() > 0)
            fluides = fluides.substring(0, fluides.length() - 1);

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR WHERE TYPEF IN (" + fluides + ");");
        try {
            while (resultSet.next()) {
                list.add("Capteur " + resultSet.getInt("IDC"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));
    }

    public Capteur[] getAllCapteursFiltresOnglet1(boolean airComprime, boolean eau, boolean electricite, boolean temperature, String[] batiments) {

        List<Capteur> list = new ArrayList<>();
        String fluides = "";
        if (airComprime)
            fluides += "'AIR_COMPRIME',";
        if (eau)
            fluides += "'EAU',";
        if (electricite)
            fluides += "'ELECTRICITE',";
        if (temperature)
            fluides += "'TEMPERATURE',";
        if (fluides.length() > 0)
            fluides = fluides.substring(0, fluides.length() - 1);

        String batimentsIn = "";
        for (String bat : batiments)
            batimentsIn += "'" + bat + "',";
        if (batimentsIn.length() > 0)
            batimentsIn = batimentsIn.substring(0, batimentsIn.length() - 1);

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR,LIEU,FLUIDE WHERE CAPTEUR.IDL = LIEU.IDL AND CAPTEUR.TYPEF = FLUIDE.TYPEF AND CAPTEUR.TYPEF IN (" + fluides + ") AND CAPTEUR.IDL IN (SELECT IDL FROM LIEU WHERE NOMB IN (" + batimentsIn + "));");
        try {
            while (resultSet.next()) {

                list.add(Capteur.create(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new Capteur[list.size()]));

    }

    public String[] getBatimentsFluides(boolean airComprime, boolean eau, boolean electricite, boolean temperature) {

        List<String> list = new ArrayList<>();
        String fluides = "";
        if (airComprime)
            fluides += "'AIR_COMPRIME',";
        if (eau)
            fluides += "'EAU',";
        if (electricite)
            fluides += "'ELECTRICITE',";
        if (temperature)
            fluides += "'TEMPERATURE',";
        if (fluides.length() > 0)
            fluides = fluides.substring(0, fluides.length() - 1);


        ResultSet resultSet = executeQuery("SELECT nomB from lieu where idl in (select idL from capteur where typeF in (" + fluides + ")) ;");
        try {
            while (resultSet.next()) {

                list.add(resultSet.getString("nomb"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));

    }

    public Capteur[] getAllCapteurs() {

        List<Capteur> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("SELECT * from capteur,lieu,fluide where capteur.idl = lieu.idl and capteur.typef = fluide.typef;");
        try {
            while (resultSet.next()) {
                list.add(Capteur.create(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new Capteur[list.size()]));

    }

    public Integer[] getEtagesBatiment(String batiment) {
        List<Integer> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("SELECT etage FROM lieu WHERE nomB = '" + batiment + "'");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getInt("etage"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new Integer[0]);
    }

    public Capteur[] getCapteurEtageBatiment(int etage, String batiment) {
        List<Capteur> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("SELECT * from capteur,lieu,fluide where capteur.idl = lieu.idl and capteur.typef = fluide.typef and lieu.nomb = '" + batiment + "' and lieu.etage = " + etage + ";");
        try {
            while (resultSet.next()) {
                list.add(Capteur.create(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new Capteur[list.size()]));

    }

    public Capteur getCapteurById(String id) {
        Capteur capteur = null;

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR,LIEU,FLUIDE WHERE CAPTEUR.IDL = LIEU.IDL AND CAPTEUR.TYPEF = FLUIDE.TYPEF AND IDC = " + id);

        try {
            if(resultSet.next()) {
                capteur = Capteur.create(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capteur;
    }

    public int updateSeuils(String id, String seuilMin, String seuilMax) {
        return executeUpdate("UPDATE CAPTEUR SET SEUILMIN = " + seuilMin + ", SEUILMAX = " + seuilMax + " WHERE IDC = " + id);
    }

}
