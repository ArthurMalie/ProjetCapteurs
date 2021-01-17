package Connexion;

import Mapping.Capteur;

import java.sql.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class Database {

    private final String address;
    private final String username;
    private final String password;
    private boolean connected;
    private Connection connection;
    private Statement statement;

    public Database(String address, String username, String password) {
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

    public int newCapteur(String nomC, String typeF, String batiment, String etage, String lieu) {
        return executeUpdate("INSERT INTO CAPTEUR (NOMC, TYPEF, BATIMENT, ETAGE, LIEU, SEUILMIN, SEUILMAX) VALUES ('" +
                nomC + "','" +
                typeF + "','" +
                batiment + "','" +
                etage + "','" +
                lieu + "','" +
                getSeuilMin(typeF) + "','" +
                getSeuilMax(typeF) + "') " +
                "ON DUPLICATE KEY UPDATE NOMC = '" + nomC + "';"
        );
    }

    public int addDonnee(String nomC, float valeur){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return executeUpdate("INSERT INTO DONNEE (NOMC, DATETIME, VALEUR) VALUES ('" +
                nomC + "','" +
                dtf.format(now) + "','" +
                valeur + "')");
    }

    public String getSeuilMin(String typeF) {
        ResultSet resultSet = executeQuery("SELECT SEUILMINDEFAUT FROM FLUIDE WHERE TYPEF = '" + typeF + "'");
        float seuil = 0.0F;
        try {
            if (resultSet.next())
                seuil = resultSet.getFloat("SEUILMINDEFAUT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(seuil);
    }

    public String getSeuilMax(String typeF) {
        ResultSet resultSet = executeQuery("SELECT SEUILMAXDEFAUT FROM FLUIDE WHERE TYPEF = '" + typeF + "'");
        float seuil = 0.0F;
        try {
            if (resultSet.next())
                seuil = resultSet.getFloat("SEUILMAXDEFAUT");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.valueOf(seuil);
    }

    public String[] getAllBatiments() {
        List<String> list = new ArrayList<>();
        ResultSet resultSet = executeQuery("SELECT DISTINCT BATIMENT FROM CAPTEUR ORDER BY BATIMENT");
        try {
            while (resultSet.next())
                list.add(resultSet.getString("BATIMENT"));
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
                list.add(String.valueOf(resultSet.getString("NOMC")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));
    }

    public List<String> getCapteursFiltresOnglet1(boolean airComprime, boolean eau, boolean electricite, boolean temperature, String[] batiments) {

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

        String batimentsIn = "";
        for (String bat : batiments)
            batimentsIn += "'" + bat + "',";
        if (batimentsIn.length() > 0)
            batimentsIn = batimentsIn.substring(0, batimentsIn.length() - 1);

        ResultSet resultSet = executeQuery("SELECT NOMC FROM CAPTEUR WHERE TYPEF IN (" + fluides + ") AND BATIMENT IN (" + batimentsIn + ");");
        try {
            while (resultSet.next()) {

                list.add(resultSet.getString("NOMC"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

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


        ResultSet resultSet = executeQuery("SELECT DISTINCT BATIMENT FROM CAPTEUR WHERE TYPEF IN (" + fluides + ");");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString("BATIMENT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray((new String[list.size()]));

    }

    public Capteur[] getAllCapteurs() {

        List<Capteur> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR, FLUIDE WHERE CAPTEUR.TYPEF = FLUIDE.TYPEF;");
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

        ResultSet resultSet = executeQuery("SELECT DISTINCT ETAGE FROM CAPTEUR WHERE BATIMENT = '" + batiment + "'");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getInt("ETAGE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new Integer[0]);
    }

    public Capteur[] getCapteurEtageBatiment(int etage, String batiment) {
        List<Capteur> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR,FLUIDE WHERE CAPTEUR.TYPEF = FLUIDE.TYPEF AND BATIMENT = '" + batiment + "' AND ETAGE = " + etage + ";");
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

        ResultSet resultSet = executeQuery("SELECT * FROM CAPTEUR,FLUIDE WHERE CAPTEUR.TYPEF = FLUIDE.TYPEF AND NOMC = '" + id + "'");

        try {
            if (resultSet.next()) {
                capteur = Capteur.create(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capteur;
    }

    public int updateSeuils(String id, String seuilMin, String seuilMax) {
        return executeUpdate("UPDATE CAPTEUR SET SEUILMIN = " + seuilMin + ", SEUILMAX = " + seuilMax + " WHERE NOMC = '" + id + "'");
    }

}
