package com.example.mareu.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;


import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummyReunionGenerator;
import com.example.mareu.model.service.ReunionApiService;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ReunionServiceUnitTest {
    private ReunionApiService service;

    @Before
    public void setup() {
        service = new DummyReunionApiService();
    }

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
        List<Reunion> listToFilter = service.getReunions();
        Calendar calendar = (Calendar) new GregorianCalendar(2023, 11, 20, 19, 0, 0);
        Reunion reunionToAdd = new Reunion((long) 4, "Réunion A", calendar, 45,
                new Salle("Mario", 10), Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"));
        //test
        assertFalse(service.getReunionFilterByDate(calendar, listToFilter).contains(reunionToAdd));
        service.createReunion(reunionToAdd);
        assertTrue(service.getReunionFilterByDate(calendar, listToFilter).contains(reunionToAdd));
    }

    @Test
    public void getReunionFilterByVenueWithSuccess() {
        //setup
        List<Reunion> listToFilter = service.getReunions();
        Salle venue = new Salle("Wario", 100);
        Reunion reunionToAdd = new Reunion((long) 6, "Réunion A", (Calendar) new GregorianCalendar(2023, 11, 20, 19, 0, 0), 45,
                venue, Arrays.asList("Xavier.carpentier@gmail.com", "Nour.Elislam.Saidi@gmail.com", "Personne.réunion@gmail.com"));
        //test
        assertFalse(service.getReunionFilterByVenue(venue, listToFilter).contains(reunionToAdd));
        service.createReunion(reunionToAdd);
        assertTrue(service.getReunionFilterByVenue(venue, listToFilter).contains(reunionToAdd));
    }

}