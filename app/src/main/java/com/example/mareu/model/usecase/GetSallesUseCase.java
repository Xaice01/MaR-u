package com.example.mareu.model.usecase;

import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.SalleRepository;

import java.util.ArrayList;
import java.util.List;

public class GetSallesUseCase {
    private final SalleRepository repository;

    public GetSallesUseCase(SalleRepository repository) {
        this.repository = repository;
    }

    public List<Salle> getSalles() {
        return new ArrayList<>(repository.getSalles());

    }
}
