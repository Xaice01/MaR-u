package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;

import java.util.ArrayList;
import java.util.List;

public class DeleteReunionUseCase {
    private final ReunionRepository repository;

    public DeleteReunionUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public Boolean deleteReunion(Reunion reunion) {
        return repository.deleteReunion(reunion);
    }
}
