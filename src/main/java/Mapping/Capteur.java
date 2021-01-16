package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Capteur {

    private int id;
    private float seuilMin;
    private float seuilMax;
    private Lieu lieu;
    private Fluide fluide;

    public Capteur ( int id, float seuilMin, float seuilMax, Lieu lieu, Fluide fluide ) {
        this.id = id;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
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

            int idC = resultSet.getInt("idC");
            float seuilMin = resultSet.getFloat("seuilMin");
            float seuilMax = resultSet.getFloat("seuilMax");

            int idL = resultSet.getInt("Capteur.idL");
            String nom = resultSet.getString("nomL");
            String batiment = resultSet.getString("nomB");
            int etage = resultSet.getInt("etage");

            Lieu lieu = new Lieu(idL, nom, batiment, etage);

            String unite = resultSet.getString("unite");
            String type_fluide = resultSet.getString("Capteur.typeF");

            Fluide fluide;
            fluide = new Fluide(unite, type_fluide);

            return new Capteur(idC, seuilMin, seuilMax, lieu, fluide);

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
