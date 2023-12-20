package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * Use Case for Filter Reunion By Salle
 */
public class FilterReunionByVenueUseCase {

    private final ReunionRepository repository;

    public FilterReunionByVenueUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public List<Reunion> filterReunionBySalle(Salle salle) {
        List<Reunion> listFilterByVenue = new ArrayList<>();
        for (Reunion reunionToCompare : repository.getReunions()) {
            if (reunionToCompare.getVenue().equals(salle)) {
                listFilterByVenue.add(reunionToCompare);
            }
        }
        return listFilterByVenue;
    }
}
