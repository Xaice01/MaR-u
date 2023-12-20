package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.repository.ReunionRepository;

/**
 * Use Case for create Reunion
 */
public class CreateReunionUseCase {

    private final ReunionRepository repository;

    public CreateReunionUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public void createReunion(Reunion reunion) {
        repository.createReunion(reunion);
    }


}
