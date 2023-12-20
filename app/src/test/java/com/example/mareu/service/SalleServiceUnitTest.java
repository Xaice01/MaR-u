package com.example.mareu.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mareu.model.Salle;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.DummySalleGenerator;
import com.example.mareu.model.service.SalleApiService;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;


/**
 * local unit test, which will execute on the development machine (host).
 * test Salle service and repository
 */

public class SalleServiceUnitTest {

    private SalleApiService service;

    @Before
    public void setup() {
        service = new DummySalleApiService();
    }

    @Test
    public void getSallesWithSuccess() {
        //setup
        List<Salle> salles = service.getSalles();
        List<Salle> expectedSalles = DummySalleGenerator.DUMMY_SALLES;
        //test
        assertThat(salles, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedSalles.toArray()));
    }

    @Test
    public void createSalleWithSucess() {
        //setup
        Salle salleToAdd = new Salle("Test", 20);
        service.createSalle(salleToAdd);
        //test
        assertTrue(service.getSalles().contains(salleToAdd));
    }

    @Test
    public void deleteSalleWithSuccess() {
        //setup
        Salle salleToDelete = service.getSalles().get(0);
        service.deleteSalle(salleToDelete);
        //test
        assertFalse(service.getSalles().contains(salleToDelete));
    }

}