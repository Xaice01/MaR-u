package com.example.mareu.model.repository;


import com.example.mareu.model.Reunion;
import com.example.mareu.model.service.ReunionApiService;

import java.util.List;

/**
 * Create a ReunionRepository
 * <p>
 * in Singleton
 */
public class ReunionRepository {
    private static ReunionApiService service;


    public ReunionRepository(ReunionApiService service) {
        this.service = service;
    }

    public List<Reunion> getReunions() {
        return service.getReunions();
    }

    public void createReunion(Reunion reunion) {
        service.createReunion(reunion);
    }

    public boolean deleteReunion(Reunion reunion) {
        return service.deleteReunion(reunion);
    }

    private static volatile ReunionRepository instance;

    /**
     * Singleton (pour avoir une seule instance ReunionRepository)
     *
     * @param service (ReunionApiService)
     * @return instance_of_ReunionRepository
     */
    public static ReunionRepository getInstance(ReunionApiService service) {
        if (instance == null) {
            synchronized (ReunionRepository.class) {
                instance = new ReunionRepository(service);
            }
        }
        return instance;
    }
}