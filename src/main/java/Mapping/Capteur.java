package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Capteur {

    private String nomC;
    private float seuilMin;
    private float seuilMax;
    private String lieu;
    private String batiment;
    private int etage;
    private Fluide fluide;

    private float valeur;

    public Capteur(String nomC, float seuilMin, float seuilMax, String lieu, String batiment, int etage, Fluide fluide) {
        this.nomC = nomC;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.lieu = lieu;
        this.batiment = batiment;
        this.etage = etage;
        this.fluide = fluide;
        valeur = -1F;
    }

   /*
    ____________________________
           REQUETES SQL
    ____________________________
    */


    public static Capteur create(ResultSet resultSet) {
        try {

            String unite = resultSet.getString("unite");
            String type_fluide = resultSet.getString("Capteur.typeF");
            float seuilMinDefaut = resultSet.getFloat("seuilMinDefaut");
            float seuilMaxDefaut = resultSet.getFloat("seuilMaxDefaut");

            Fluide fluide;
            fluide = new Fluide(unite, type_fluide, seuilMinDefaut, seuilMaxDefaut);

            String nomC = resultSet.getString("nomC");
            float seuilMin = resultSet.getFloat("seuilMin");
            float seuilMax = resultSet.getFloat("seuilMax");
            String lieu = resultSet.getString("lieu");
            String batiment = resultSet.getString("batiment");
            int etage = resultSet.getInt("etage");

            return new Capteur(nomC, seuilMin, seuilMax, lieu, batiment, etage, fluide);

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

    public String getNomC() {
        return nomC;
    }

    public void setnomC(String nomC) {
        this.nomC = nomC;
    }

    public float getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(float seuilMin) {
        this.seuilMin = seuilMin;
    }

    public float getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(float seuilMax) {
        this.seuilMax = seuilMax;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getBatiment() {
        return batiment;
    }

    public void setBatiment(String batiment) {
        this.batiment = batiment;
    }

    public int getEtage() {
        return etage;
    }

    public void setEtage(int etage) {
        this.etage = etage;
    }

    public Fluide getFluide() {
        return fluide;
    }

    public void setFluide(Fluide fluide) {
        this.fluide = fluide;
    }

    public float getValeur() {
        return valeur;
    }

    public void setValeur(float valeur) {
        this.valeur = valeur;
    }


}
