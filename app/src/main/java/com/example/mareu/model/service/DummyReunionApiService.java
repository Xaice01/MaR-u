package com.example.mareu.model.service;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyReunionApiService implements ReunionApiService {

    private final List<Reunion> reunions = DummyReunionGenerator.generateReunions();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reunion> getReunions() {
        return reunions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReunion(Reunion reunion) {
        reunions.add(reunion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteReunion(Reunion reunion) {
        return reunions.remove(reunion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reunion> getReunionFilterByDate(Calendar calendar, List<Reunion> listToFilter) {

        List<Reunion> listFilterByDate = new ArrayList<>();
        for (Reunion reunionToCompare : listToFilter) {
            if (reunionToCompare.getDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) & reunionToCompare.getDate().get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                listFilterByDate.add(reunionToCompare);
            }
        }
        return listFilterByDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reunion> getReunionFilterByVenue(Salle salle, List<Reunion> listToFilter) {
        List<Reunion> listFilterByVenue = new ArrayList<>();
        for (Reunion reunionToCompare : listToFilter) {
            if (reunionToCompare.getVenue().equals(salle)) {
                listFilterByVenue.add(reunionToCompare);
            }
        }
        return listFilterByVenue;
    }
}
