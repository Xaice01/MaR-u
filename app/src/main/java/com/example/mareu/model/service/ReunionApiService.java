package com.example.mareu.model.service;

import com.example.mareu.model.Reunion;

import java.util.List;

/**
 * Reunion API client
 */
public interface ReunionApiService {

    /**
     * Get all of Reunions
     *
     * @return {@link List}
     */
    List<Reunion> getReunions();

    /**
     * Create a Reunion
     *
     * @param reunion to create
     */
    void createReunion(Reunion reunion);

    /**
     * Delete a Reunion
     *
     * @param reunion to delete
     */
    boolean deleteReunion(Reunion reunion);

}
