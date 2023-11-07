package com.example.mareu.service;

import com.example.mareu.model.Salle;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public abstract class DummySalleGenerator {

    /**
     * Generator
     * List of Venue
     */
    public static List<Salle> DUMMY_SALLES = Arrays.asList(
            new Salle("Mario", 10),
            new Salle("Luigi", 20),
            new Salle("Peach", 15),
            new Salle("Toad", 10),
            new Salle("Daisy", 5),
            new Salle("Harmonie", 20),
            new Salle("Wario", 100),
            new Salle("Waluigi", 10),
            new Salle("Yoshi", 5),
            new Salle("Bowser", 10)
    );

    static List<Salle> generateSalles() {
        return new ArrayList<>(DUMMY_SALLES);
    }
}
