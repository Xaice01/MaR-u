package com.example.mareu.model.service;

import com.example.mareu.model.Reunion;

import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyReunionApiService implements ReunionApiService {

    private final List<Reunion> reunions = DummyReunionGenerator.generateReunions();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reunion> getReunions() {
        return reunions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReunion(Reunion reunion) {
        reunions.add(reunion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteReunion(Reunion reunion) {
        reunions.remove(reunion);
    }
}
