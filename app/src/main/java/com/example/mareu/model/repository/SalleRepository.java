package com.example.mareu.model.repository;

import com.example.mareu.model.Salle;
import com.example.mareu.model.service.SalleApiService;

import java.util.List;

/**
 * Create a SalleRepository
 * <p>
 * in Singleton
 */
public class SalleRepository {
    private static SalleApiService service;


    private SalleRepository(SalleApiService service) {
        SalleRepository.service = service;
    }

    public List<Salle> getSalles() {
        return service.getSalles();
    }

    public void createSalle(Salle salle) {
        service.createSalle(salle);
    }

    public void deleteSalle(Salle salle) {
        service.deleteSalle(salle);
    }

    private static volatile SalleRepository instance;

    /**
     * Singleton  (pour avoir une seule instance SalleRepository)
     *
     * @param service (SalleApiService)
     * @return instance_of_SalleRepository
     */
    public static SalleRepository getInstance(SalleApiService service) {
        if (instance == null) {
            synchronized (SalleRepository.class) {
                instance = new SalleRepository(service);
            }
        }
        return instance;
    }
}
