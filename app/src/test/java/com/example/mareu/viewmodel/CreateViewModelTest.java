package com.example.mareu.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.service.SalleApiService;
import com.example.mareu.model.usecase.GetIdForNewReunionUseCase;
import com.example.mareu.model.usecase.GetSalleAvailableUseCase;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * local unit test, which will execute on the development machine (host).
 * test CreateViewModel
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateViewModelTest extends TestCase {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();


    private final ReunionApiService reunionApiService = new DummyReunionApiService();
    private final ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);

    private final SalleApiService salleApiService = new DummySalleApiService();
    private final SalleRepository salleRepository = SalleRepository.getInstance(salleApiService);

    private final CreateViewModel createViewModel = new CreateViewModel();

    @Test
    public void testGetIdForNewReunion() {
        // Given
        int sizeOfReunions = reunionRepository.getReunions().size();
        long lastId = reunionRepository.getReunions().get(sizeOfReunions - 1).getId();


        // When
        long idExpected = lastId + 1;

        // Then
        assertEquals(new GetIdForNewReunionUseCase(reunionRepository).getIdForNewReunion(), idExpected);
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
        createViewModel.createReunion(reunionToAdd);
        reunionToAdd = new Reunion((long) 13, "Réunion Test", calendarTimeOfReunion, duration,
                salleUse2, Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));
        createViewModel.createReunion(reunionToAdd);
        reunionToAdd = new Reunion((long) 14, "Réunion Test", calendarTimeOfReunion, duration,
                salleUse3, Arrays.asList("Test@gmail.com", "Test2@gmail.com", "Test3@gmail.com"));
        createViewModel.createReunion(reunionToAdd);


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

        List<String> listToCompare = new GetSalleAvailableUseCase(reunionRepository, salleRepository).getSalleAvailable(calendarTimeOfReunion, duration);

        //Then
        assertEquals(listOfSalleAvailabeInString, listToCompare);
    }


    @Test
    public void testGetSalleWithString() {
        //Given
        createViewModel.inisialisation();
        Salle salle = reunionRepository.getReunions().get(3).getVenue();
        String NameOfSalle = salle.getLieu();

        //When
        Salle salleToCompare = createViewModel.getSalleWithString(NameOfSalle);

        //Then
        assertEquals(salle, salleToCompare);
    }

    @Test
    public void testAddToListOfParticipant() {
        // Given
        createViewModel.inisialisation();
        String email = "Test@gmail.com";

        // When
        createViewModel.addToListOfParticipant(email);
        String emailToCompar = Objects.requireNonNull(createViewModel.listOfParticipant.getValue()).get(0);

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
        String listOfEmailToShow = createViewModel.listOfEmailToShow(arrayListToSend);

        // Then
        assertEquals(listOfEmailToShow, listExpected);
    }

    @Test
    public void testCreateReunionWithNameOfReunionAndNameOfSalle() {
        //Given
        createViewModel.inisialisation();
        createViewModel.duration = 45;
        createViewModel.addToListOfParticipant("emailTest@gmail");
        createViewModel.selectYearToCreate = 2023;
        createViewModel.selectMonthToCreate = 1;
        createViewModel.selectDayToCreate = 10;
        createViewModel.selectHourToCreate = 0;
        createViewModel.selectMinuteToCreate = 0;
        String nameOfReuion = "TestNameOfReunion";
        int reunionSizeBefore = reunionRepository.getReunions().size();

        //When
        createViewModel.createReunion(nameOfReuion, nameOfReuion);
        int reunionSize = reunionRepository.getReunions().size();
        String nameExpect = reunionRepository.getReunions().get(reunionSize - 1).getName();

        //Then
        assertEquals(reunionSize, (reunionSizeBefore + 1));
        assertEquals(nameExpect, nameOfReuion);
    }
}
