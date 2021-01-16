package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Lieu {

    private int id;
    private String nom;
    private String batiment;
    private int etage;

    public Lieu ( int id, String nom, String batiment, int etage ) {
        this.id = id;
        this.nom = nom;
        this.batiment = batiment;
        this.etage = etage;
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

    public String getNom () {
        return nom;
    }

    public void setNom ( String nom ) {
        this.nom = nom;
    }

    public String getBatiment () {
        return batiment;
    }

    public void setBatiment ( String batiment ) {
        this.batiment = batiment;
    }

    public int getEtage () {
        return etage;
    }

    public void setEtage ( int etage ) {
        this.etage = etage;
    }
}
