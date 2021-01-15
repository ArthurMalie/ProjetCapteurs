package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Fluide {

    private String unite;
    private float seuilDefautMin;
    private float seuilDefautMax;
    private TYPEFLUIDE type_fluide;

    public Fluide ( String unite, float seuilDefautMin, float seuilDefautMax, TYPEFLUIDE type_fluide ) {
        this.unite = unite;
        this.seuilDefautMin = seuilDefautMin;
        this.seuilDefautMax = seuilDefautMax;
        this.type_fluide = type_fluide;
    }

    /*
    ____________________________
           REQUETES SQL
    ____________________________
    */


    public static Fluide create ( ResultSet resultSet ) {
        try {

            String unite = resultSet.getString("unite");
            float seuilDefautMin = resultSet.getFloat("seuilDefautMin");
            float seuilDefautMax = resultSet.getFloat("seuilDefautMax");
            TYPEFLUIDE type_fluide = resultSet.getObject("etage", TYPEFLUIDE.class);

            Fluide fluide;
            fluide = new Fluide(unite, seuilDefautMin, seuilDefautMax, type_fluide);
            return fluide;

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

    public String getUnite () {
        return unite;
    }

    public void setUnite ( String unite ) {
        this.unite = unite;
    }

    public float getSeuilDefautMin () {
        return seuilDefautMin;
    }

    public void setSeuilDefautMin ( int seuilDefautMin ) {
        this.seuilDefautMin = seuilDefautMin;
    }

    public float getSeuilDefautMax () {
        return seuilDefautMax;
    }

    public void setSeuilDefautMax ( int seuilDefautMax ) {
        this.seuilDefautMax = seuilDefautMax;
    }

    public TYPEFLUIDE getType_fluide () {
        return type_fluide;
    }

    public void setType_fluide ( TYPEFLUIDE type_fluide ) {
        this.type_fluide = type_fluide;
    }
}
