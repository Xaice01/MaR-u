package com.example.mareu.viewmodel;

import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.service.SalleApiService;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ReunionViewModelTest extends TestCase {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Observer<List<Reunion>> reunionObserver;

    @Mock
    private Observer<List<Salle>> salleObserver;

    private final ReunionApiService reunionApiService = new DummyReunionApiService();
    private ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);

    private final SalleApiService salleApiService = new DummySalleApiService();

    private ReunionViewModel reunionViewModel = new ReunionViewModel();


    @Test
    public void testGetReunions() {
        // Given
        List<Reunion> reunions = reunionRepository.getReunions();

        // When
        reunionViewModel.getReunions().observeForever(reunionObserver);

        // Then
        verify(reunionObserver).onChanged(reunions);
        reunionViewModel.getReunions().removeObserver(reunionObserver);
    }

    @Test
    public void testGetReunionsByDate() {
        // Given
        List<Reunion> listToFilter = reunionViewModel.getReunions().getValue();
        Calendar calendar = new GregorianCalendar(2017, 0, 25, 0, 0, 0);
        List<Reunion> reunions = reunionApiService.getReunionFilterByDate(calendar, listToFilter);

        // When
        reunionViewModel.getReunionsByDate(calendar).observeForever(reunionObserver);

        // Then
        verify(reunionObserver).onChanged(reunions);
        reunionViewModel.getReunions().removeObserver(reunionObserver);
    }

    @Test
    public void testGetReunionsByLieu() {
        // Given
        List<Reunion> listToFilter = reunionViewModel.getReunions().getValue();
        Salle salle = new Salle("Mario", 10);
        List<Reunion> reunions = reunionRepository.getReunionFilterByVenue(salle, listToFilter);

        // When
        reunionViewModel.getReunionsByLieu(salle).observeForever(reunionObserver);

        // Then
        verify(reunionObserver).onChanged(reunions);
        reunionViewModel.getReunions().removeObserver(reunionObserver);
    }

    @Test
    public void testDeleteReunion() {
        // Given
        int position = 1;
        Reunion reunionToDelete = reunionViewModel.getReunions().getValue().get(position);

        // When
        assertTrue(reunionViewModel.getReunions().getValue().contains(reunionToDelete));
        reunionViewModel.deleteReunion(reunionToDelete, position);

        // Then
        assertFalse(reunionViewModel.getReunions().getValue().contains(reunionToDelete));
    }

    @Test
    public void testGetSalles() {
        // Given
        List<Salle> salles = salleApiService.getSalles();

        // When
        reunionViewModel.getSalles().observeForever(salleObserver);
        reunionViewModel.init();

        // Then
        verify(salleObserver).onChanged(salles);
        reunionViewModel.getSalles().removeObserver(salleObserver);
    }

    @Test
    public void testGetDeletePosition() {
        // Given
        int position = 1;
        Reunion reunionToDelete = reunionViewModel.getReunions().getValue().get(position);

        // When
        reunionViewModel.deleteReunion(reunionToDelete, position);
        int positionToCompare = reunionViewModel.getDeletePosition().getValue();

        // Then
        assertEquals(position, positionToCompare);
    }

}