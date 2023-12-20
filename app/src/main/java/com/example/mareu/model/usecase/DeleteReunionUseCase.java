package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.repository.ReunionRepository;
/**
 * Use Case for delete Reunion
 */
public class DeleteReunionUseCase {
    private final ReunionRepository repository;

    public DeleteReunionUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public Boolean deleteReunion(Reunion reunion) {
        return repository.deleteReunion(reunion);
    }
}
