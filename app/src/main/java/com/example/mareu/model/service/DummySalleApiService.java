package com.example.mareu.model.service;

import com.example.mareu.model.Salle;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummySalleApiService implements SalleApiService {

    private final List<Salle> salles = DummySalleGenerator.generateSalles();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Salle> getSalles() {
        return salles;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createSalle(Salle salle) {
        salles.add(salle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSalle(Salle salle) {
        salles.remove(salle);

    }
}
