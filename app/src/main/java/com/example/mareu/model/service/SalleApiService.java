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
     * @param salle Salle to Create
     */
    void createSalle(Salle salle);

    /**
     * delete venue
     *
     * @param salle Salle to Delete
     */
    void deleteSalle(Salle salle);
}
