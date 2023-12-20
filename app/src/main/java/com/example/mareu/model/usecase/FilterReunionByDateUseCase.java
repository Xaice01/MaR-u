package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.repository.ReunionRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Use Case for Filter Reunion By Date
 */
public class FilterReunionByDateUseCase {

    private final ReunionRepository repository;

    public FilterReunionByDateUseCase(ReunionRepository repository) {
        this.repository = repository;
    }

    public List<Reunion> filterReunionByDate(Calendar calendar) {
        List<Reunion> listFilterByDate = new ArrayList<>();
        for (Reunion reunionToCompare : repository.getReunions()) {
            if (reunionToCompare.getDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) & reunionToCompare.getDate().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                listFilterByDate.add(reunionToCompare);
            }
        }
        return listFilterByDate;
    }
}
