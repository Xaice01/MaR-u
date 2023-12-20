package com.example.mareu.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;


import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummyReunionGenerator;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.usecase.FilterReunionByDateUseCase;
import com.example.mareu.model.usecase.FilterReunionByVenueUseCase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * local unit test, which will execute on the development machine (host).
 * test reunion service and repository
 */
public class ReunionServiceUnitTest {
    private final ReunionApiService service = new DummyReunionApiService();
    private final ReunionRepository reunionRepository = ReunionRepository.getInstance(service);

    @Test
    public void getReunionsWithSuccess() {
        //setup
        List<Reunion> reunions = service.getReunions();
        List<Reunion> expectedReunions = DummyReunionGenerator.DUMMY_REUNIONS;
        //test
        assertThat(reunions, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedReunions.toArray()));
    }

    @Test
    public void createReunionWithSucess() {
        //setup
        Reunion reunionToAdd = new Reunion((long) 1, "Réunion A", (Calendar) new GregorianCalendar(2017, 0, 20, 19, 0, 0), 45,
                new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"));
        service.createReunion(reunionToAdd);
        //test
        assertTrue(service.getReunions().contains(reunionToAdd));
    }

    @Test
    public void deleteReunionWithSuccess() {
        //setup
        Reunion reunionToDelete = service.getReunions().get(0);
        service.deleteReunion(reunionToDelete);
        //test
        assertFalse(service.getReunions().contains(reunionToDelete));
    }

    @Test
    public void getReunionFilterByDateWithSuccess() {
        //setup
        Calendar calendar = (Calendar) new GregorianCalendar(2023, 11, 20, 19, 0, 0);
        Reunion reunionToAdd = new Reunion((long) 4, "Réunion A", calendar, 45,
                new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"));
        //test
        assertFalse(new FilterReunionByDateUseCase(reunionRepository).filterReunionByDate(calendar).contains(reunionToAdd));
        reunionRepository.createReunion(reunionToAdd);
        assertTrue(new FilterReunionByDateUseCase(reunionRepository).filterReunionByDate(calendar).contains(reunionToAdd));
    }

    @Test
    public void getReunionFilterByVenueWithSuccess() {
        //setup
        Salle venue = new Salle("Wario", 100);
        Reunion reunionToAdd = new Reunion((long) 6, "Réunion A", (Calendar) new GregorianCalendar(2023, 11, 20, 19, 0, 0), 45,
                venue, Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"));
        //test
        assertFalse(new FilterReunionByVenueUseCase(reunionRepository).filterReunionBySalle(venue).contains(reunionToAdd));
        reunionRepository.createReunion(reunionToAdd);
        assertTrue(new FilterReunionByVenueUseCase(reunionRepository).filterReunionBySalle(venue).contains(reunionToAdd));
    }

}