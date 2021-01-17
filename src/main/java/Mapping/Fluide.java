package Mapping;

public class Fluide {

    private String unite;
    private String type_fluide;
    private float seuilMinDefaut;
    private float seuilMaxDefaut;

    public Fluide ( String unite, String type_fluide, float seuilMinDefaut, float seuilMaxDefaut) {
        this.unite = unite;
        this.type_fluide = type_fluide;
        this.seuilMinDefaut = seuilMinDefaut;
        this.seuilMaxDefaut = seuilMaxDefaut;
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

    public float getSeuilMinDefaut() {
        return seuilMinDefaut;
    }

    public void setSeuilMinDefaut(float seuilMinDefaut) {
        this.seuilMinDefaut = seuilMinDefaut;
    }

    public float getSeuilMaxDefaut() {
        return seuilMaxDefaut;
    }

    public void setSeuilMaxDefaut(float seuilMaxDefaut) {
        this.seuilMaxDefaut = seuilMaxDefaut;
    }
}
