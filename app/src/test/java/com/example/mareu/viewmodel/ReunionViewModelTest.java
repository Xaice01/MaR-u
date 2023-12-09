package com.example.mareu.viewmodel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummyReunionGenerator;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.service.SalleApiService;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testGetIdForNewReunion() {
        // Given
        int sizeOfReunions = reunionViewModel.getReunions().getValue().size();
        long lastId = reunionViewModel.getReunions().getValue().get(sizeOfReunions - 1).getId();


        // When
        int idExpected = (int) (lastId + 1);

        // Then
        assertEquals(reunionViewModel.getIdForNewReunion(), idExpected);
    }

    @Test
    public void testCreateReunion() {
        // Given
        Reunion reunionToAdd = new Reunion(reunionViewModel.getIdForNewReunion(), "Réunion Test", (Calendar) new GregorianCalendar(2023, 2, 25, 19, 0, 0), 45,
                new Salle("Mario", 10), Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));

        // When
        assertFalse(reunionViewModel.getReunions().getValue().contains(reunionToAdd));
        reunionViewModel.createReunion(reunionToAdd);

        // Then
        assertTrue(reunionViewModel.getReunions().getValue().contains(reunionToAdd));
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
    public void testGetSalleAvailable() {
        //Given
        int duration = 45;
        //add salle with same time but Salle different
        Salle salleUse1 = new Salle("Mario", 10);
        Salle salleUse2 = new Salle("Luigi", 20);
        Salle salleUse3 = new Salle("Daisy", 5);
        Calendar calendarTimeOfReunion = new GregorianCalendar(2023, 2, 25, 19, 0, 0);
        Reunion reunionToAdd = new Reunion((long) 12, "Réunion Test", calendarTimeOfReunion, duration,
                salleUse1, Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));
        reunionViewModel.createReunion(reunionToAdd);
        reunionToAdd = new Reunion((long) 13, "Réunion Test", calendarTimeOfReunion, duration,
                salleUse2, Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));
        reunionViewModel.createReunion(reunionToAdd);
        reunionToAdd = new Reunion((long) 14, "Réunion Test", calendarTimeOfReunion, duration,
                salleUse3, Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));
        reunionViewModel.createReunion(reunionToAdd);


        //When
        //get all of Salles
        ArrayList<Salle> listOfSalleAvailabe = (ArrayList<Salle>) salleApiService.getSalles();
        //delete Salle Use
        listOfSalleAvailabe.remove(salleUse1);
        listOfSalleAvailabe.remove(salleUse2);
        listOfSalleAvailabe.remove(salleUse3);
        //transform the list in list of String
        List<String> listOfSalleAvailabeInString = new ArrayList<>();
        for (Salle salle : listOfSalleAvailabe) {
            listOfSalleAvailabeInString.add(salle.getLieu());
        }

        List<String> listToCompare = reunionViewModel.getSalleAvailable(calendarTimeOfReunion, duration);

        //Then
        assertEquals(listOfSalleAvailabeInString, listToCompare);
    }

    @Test
    public void testGetSalleWithString() {
        //Given
        Salle salle = reunionViewModel.getReunions().getValue().get(3).getVenue();
        String NameOfSalle = salle.getLieu();

        //When
        Salle salleToCompare = reunionViewModel.getSalleWithString(NameOfSalle);

        //Then
        assertEquals(salle, salleToCompare);
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

    public void testDatePickerCreateView() {
    }

    public void testTimePickerStart() {
    }

    public void testTimePickerEnd() {
    }

    @Test
    public void testAddToListOfParticipant() {
        // Given
        reunionViewModel.init();
        String email = "Test@gmail.com";

        // When
        reunionViewModel.addToListOfParticipant(email);
        String emailToCompar = reunionViewModel.listOfParticipant.getValue().get(0);

        // Then
        assertEquals(email, emailToCompar);
    }

    @Test
    public void testListOfEmailToShow() {
        // Given
        String email1 = "Test1@gmail.com";
        String email2 = "Test2@gmail.com";
        String email3 = "Test3@gmail.com";
        ArrayList<String> arrayListToSend = new ArrayList<>();
        arrayListToSend.add(email1);
        arrayListToSend.add(email2);
        arrayListToSend.add(email3);

        String listExpected = email1 + " \n" + email2 + " \n" + email3;

        // When
        String listOfEmailToShow = reunionViewModel.listOfEmailToShow(arrayListToSend);

        // Then
        assertEquals(listOfEmailToShow, listExpected);
    }

    @Test
    public void testCreateReunionWithNameOfReunionAndNameOfSalle() {
        //Given
        reunionViewModel.init();
        reunionViewModel.duration = 45;
        reunionViewModel.addToListOfParticipant("emailTest@gmail");
        reunionViewModel.selectYearToCreate = 2023;
        reunionViewModel.selectMonthToCreate = 1;
        reunionViewModel.selectDayToCreate = 10;
        reunionViewModel.selectHourToCreate = 0;
        reunionViewModel.selectMinuteToCreate = 0;
        String nameOfReuion = "TestNameOfReunion";
        String NameOfSalle = "Mario";
        int reunionSizeBefore = reunionViewModel.getReunions().getValue().size();

        //When
        reunionViewModel.createReunion(nameOfReuion, nameOfReuion);
        int reunionSize = reunionViewModel.getReunions().getValue().size();
        String nameExpect = reunionViewModel.getReunions().getValue().get(reunionSize - 1).getName();

        //Then
        assertEquals(reunionSize, (reunionSizeBefore + 1));
        assertEquals(nameExpect, nameOfReuion);
    }

}