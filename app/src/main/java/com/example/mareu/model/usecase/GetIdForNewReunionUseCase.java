package com.example.mareu.model.usecase;

import com.example.mareu.model.repository.ReunionRepository;

/**
 * Use Case for get the new id to a reunion
 */
public class GetIdForNewReunionUseCase {
    private final ReunionRepository repository;

    public GetIdForNewReunionUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    /**
     * Get id for new reunion
     *
     * @return id next Id available
     */
    public long getIdForNewReunion() {
        int sizeOfReunions;
        if (repository.getReunions().isEmpty()) {
            return 1;
        }
        sizeOfReunions = repository.getReunions().size();
        return repository.getReunions().get(sizeOfReunions - 1).getId() + 1;
    }
}
