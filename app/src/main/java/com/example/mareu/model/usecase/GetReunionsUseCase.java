package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.repository.ReunionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Get all of Reunions
 */
public class GetReunionsUseCase {
    private final ReunionRepository repository;

    public GetReunionsUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public List<Reunion> getReunions() {
        return new ArrayList<>(repository.getReunions());

    }
}
