package Mapping;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Fluide {

    private String unite;
    private String type_fluide;

    public Fluide ( String unite, String type_fluide ) {
        this.unite = unite;
        this.type_fluide = type_fluide;
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

    public String getType_fluide () {
        return type_fluide;
    }

    public void setType_fluide ( String type_fluide ) {
        this.type_fluide = type_fluide;
    }
}
