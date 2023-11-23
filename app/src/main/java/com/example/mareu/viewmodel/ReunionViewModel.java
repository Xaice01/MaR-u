package com.example.mareu.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.service.SalleApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ReunionViewModel extends ViewModel {
    //TODO viewModel Factory a faire
    //----------------------------------------------------
    //Repository
    //----------------------------------------------------

    //injection de dépendance Data:DummyReunionApiService()
    private ReunionApiService reunionApiService = new DummyReunionApiService();
    private ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);


    //injection de dépendance Data:DummySalleApiService()
    private SalleApiService salleApiService = new DummySalleApiService();
    private SalleRepository salleRepository = new SalleRepository(salleApiService);

    //----------------------------------------------------
    //Data
    //----------------------------------------------------

    private MutableLiveData<List<Reunion>> reunions = new MutableLiveData<>();
    private MutableLiveData<List<Salle>> salles = new MutableLiveData<>();

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------

    private MutableLiveData<Integer> deletePosition = new MutableLiveData<>();
    private MutableLiveData<String> filter;



    //----------------------------------------------------
    //inisialization
    //----------------------------------------------------

    /**
     * Inisialization
     * <p>
     * setValue reunionRepository to MutableLiveData reunions
     * setValue salleRepository to MutableLiveData salles
     */
    public void init() {
        reunions.setValue(reunionRepository.getReunions());
        salles.setValue(salleRepository.getSalles());
    }

    //----------------------------------------------------
    //fonction
    //----------------------------------------------------

    /**
     * Get id for new reunion
     *
     * @return id next Id available
     */
    public long getIdForNewReunion() {
        int sizeOfReunions;
        sizeOfReunions = reunionRepository.getReunions().size();
        return reunionRepository.getReunions().get(sizeOfReunions - 1).getId() + 1;
    }

    /**
     * Create a Reunion
     *
     * @param reunion reunion to create
     */
    public void createReunion(Reunion reunion) {
        reunionRepository.createReunion(reunion);
    }

    /**
     * delete a Reunion
     *
     * @param reunion  reunion to delete
     * @param position position of item delete
     */
    public void deleteReunion(Reunion reunion, int position) {
        //if the reunion is delete
        if (reunionRepository.deleteReunion(reunion)) {
            deletePosition.setValue(position);
        } else {
            throw new IllegalArgumentException("reunion not found");
        }
    }

    /**
     * get list of Salle available
     *
     * @param calendar      the date and hour of Reunion
     * @param dureeToMinute duration of Reunion
     */
    public List<String> getSalleAvailable(Calendar calendar, int dureeToMinute) {
        init();
        List<String> listSalle = new ArrayList<>();
        for (Salle salle : salles.getValue()) {
            listSalle.add(salle.getLieu());
        }

        List<Reunion> reunion_To_Compare = reunions.getValue();
        for (Reunion reunion : reunion_To_Compare) {
            Calendar calendarEnd = reunion.getDate();
            calendarEnd.add(Calendar.MINUTE, dureeToMinute);
            Calendar endOfReunion = reunion.getDate();
            endOfReunion.add(Calendar.MINUTE, (int) reunion.getDuration());

            //calcul if a reunion is created on a other Reunion
            if ((reunion.getDate().before(calendar) & endOfReunion.before(calendar)) || reunion.getDate().equals(calendar) || (reunion.getDate().before(calendarEnd) & endOfReunion.before(calendarEnd))) {
                if (listSalle.contains(reunion.getVenue().getLieu()))
                    listSalle.remove(reunion.getVenue().getLieu());
            }

            if (listSalle.isEmpty()) {
                listSalle.add("No Salle Available");
            }

        }
        return listSalle;

    }

    /**
     * Get Salle with the string name of this
     *
     * @param nameOfSalle string of the name of Salle
     * @return salleToReturn objet Salle
     */
    public Salle getSalleWithString(String nameOfSalle) {
        Salle salleToReturn = null;
        for (Salle salle : salles.getValue()) {
            if (Objects.equals(salle.getLieu(), nameOfSalle)) {
                salleToReturn = salle;
            }

        }
        return salleToReturn;
    }


    /**
     * get list of Reunion
     */
    public LiveData<List<Reunion>> getReunions() {
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByDate(Calendar calendar) {
        //TODO methode a faire
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByLieu(Salle salle) {
        //TODO methode a faire
        return reunions;
    }

    /**
     * get list of Salle
     */
    public LiveData<List<Salle>> getSalles() {
        return salles;
    }

    /**
     * get position of item to delete
     */
    public LiveData<Integer> getDeletePosition() {
        return deletePosition;
    }


}
