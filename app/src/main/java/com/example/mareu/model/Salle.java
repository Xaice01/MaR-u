package com.example.mareu.model;

import java.util.Objects;

public class Salle {
    /**
     * venue
     */
    private String lieu;

    /**
     * number maximum of person
     */
    private int person_Max;

    /**
     * Constructor
     *
     * @param lieu       lieu of reunion
     * @param person_Max number max of person
     */
    public Salle(String lieu, int person_Max) {
        this.lieu = lieu;
        this.person_Max = person_Max;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }


    public int getPerson_Max() {
        return person_Max;
    }

    public void setPerson_Max(int person_Max) {
        this.person_Max = person_Max;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salle salle = (Salle) o;
        return person_Max == salle.person_Max && Objects.equals(lieu, salle.lieu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lieu, person_Max);
    }
}
