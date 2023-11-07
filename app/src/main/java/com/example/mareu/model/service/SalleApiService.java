package com.example.mareu.model.service;

import com.example.mareu.model.Salle;

import java.util.List;

/**
 * Salle API client (Venue API)
 */
public interface SalleApiService {
    /**
     * Get all my venue
     *
     * @return {@link List}
     */
    List<Salle> getSalles();

    /**
     * Create venue
     *
     * @param salle
     */
    void createSalle(Salle salle);

    /**
     * delete venue
     *
     * @param salle
     */
    void deleteSalle(Salle salle);
}
