package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Capteur {

    private int id;
    private float valeur;
    private float seuilMin;
    private float seuilMax;
    private boolean connecte;
    private Lieu lieu;
    private Fluide fluide;

    public Capteur ( int id, float valeur, float seuilMin, float seuilMax, boolean connecte, Lieu lieu, Fluide fluide ) {
        this.id = id;
        this.valeur = valeur;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.connecte = connecte;
        this.lieu = lieu;
        this.fluide = fluide;
    }

   /*
    ____________________________
           REQUETES SQL
    ____________________________
    */


    public static Capteur create ( ResultSet resultSet ) {
        try {

            int id = resultSet.getInt("id");
            float valeur = resultSet.getFloat("valeur");
            float seuilMin = resultSet.getFloat("seuilMin");
            float seuilMax = resultSet.getFloat("seuilMax");
            boolean connecte = resultSet.getBoolean("connecte");

            int idS = resultSet.getInt("id");
            String nom = resultSet.getString("nom");
            String batiment = resultSet.getString("batiment");
            int etage = resultSet.getInt("etage");

            Lieu lieu;
            lieu = new Lieu(idS, nom, batiment, etage);


            String unite = resultSet.getString("unite");
            float seuilDefautMin = resultSet.getFloat("seuilDefautMin");
            float seuilDefautMax = resultSet.getFloat("seuilDefautMax");
            TYPEFLUIDE type_fluide = resultSet.getObject("etage", TYPEFLUIDE.class);

            Fluide fluide;
            fluide = new Fluide(unite, seuilDefautMin, seuilDefautMax, type_fluide);


            Capteur capteur;
            capteur = new Capteur(id, valeur, seuilMin, seuilMax, connecte, lieu, fluide);
            return capteur;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    /*
    ____________________________
        GETTERS & SETTERS
    ____________________________
    */

    public int getId () {
        return id;
    }

    public void setId ( int id ) {
        this.id = id;
    }

    public float getValeur () {
        return valeur;
    }

    public void setValeur ( float valeur ) {
        this.valeur = valeur;
    }

    public float getSeuilMin () {
        return seuilMin;
    }

    public void setSeuilMin ( float seuilMin ) {
        this.seuilMin = seuilMin;
    }

    public float getSeuilMax () {
        return seuilMax;
    }

    public void setSeuilMax ( float seuilMax ) {
        this.seuilMax = seuilMax;
    }

    public boolean isConnecte () {
        return connecte;
    }

    public void setConnecte ( boolean connecte ) {
        this.connecte = connecte;
    }

    public Lieu getLieu () {
        return lieu;
    }

    public void setLieu ( Lieu lieu ) {
        this.lieu = lieu;
    }

    public Fluide getFluide () {
        return fluide;
    }

    public void setFluide ( Fluide fluide ) {
        this.fluide = fluide;
    }
}
