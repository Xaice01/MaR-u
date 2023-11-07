package com.example.mareu.model.service;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class DummyReunionGenerator {
    /**
     * Generator
     * List of Reunion
     */
    public static List<Reunion> DUMMY_REUNIONS = Arrays.asList(
            new Reunion((long) 1, "Réunion A", (Calendar) new GregorianCalendar(2017, 0, 25, 19, 0, 0), 45,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 2, "Réunion B", (Calendar) new GregorianCalendar(2017, 0, 24, 19, 0, 0), 45,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com")),
            new Reunion((long) 3, "Réunion C", (Calendar) new GregorianCalendar(2017, 0, 25, 19, 0, 0), 45,
                    new Salle("Luigi", 20), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 4, "Réunion D", (Calendar) new GregorianCalendar(2017, 0, 25, 18, 0, 0), 45,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com")),
            new Reunion((long) 5, "Réunion E", (Calendar) new GregorianCalendar(2017, 0, 25, 17, 0, 0), 60,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 6, "Réunion F", (Calendar) new GregorianCalendar(2017, 0, 24, 19, 0, 0), 60,
                    new Salle("Luigi", 20), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 7, "Réunion G", (Calendar) new GregorianCalendar(2017, 0, 25, 15, 0, 0), 45,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 8, "Réunion H", (Calendar) new GregorianCalendar(2017, 0, 23, 15, 0, 0), 45,
                    new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 9, "Réunion I", (Calendar) new GregorianCalendar(2017, 0, 25, 15, 0, 0), 45,
                    new Salle("Luigi", 20), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com")),
            new Reunion((long) 10, "Réunion J", (Calendar) new GregorianCalendar(2017, 0, 25, 15, 0, 0), 45,
                    new Salle("Harmonie", 20), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"))

    );

    static List<Reunion> generateReunions() {
        return new ArrayList<>(DUMMY_REUNIONS);
    }


}
