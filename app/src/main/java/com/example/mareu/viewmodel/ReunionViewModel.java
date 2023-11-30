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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class ReunionViewModel extends ViewModel {
    //TODO viewModel Factory a faire
    //----------------------------------------------------
    //Repository
    //----------------------------------------------------

    //injection de dépendance Data:DummyReunionApiService()
    private final ReunionApiService reunionApiService = new DummyReunionApiService();
    private ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);


    //injection de dépendance Data:DummySalleApiService()
    private final SalleApiService salleApiService = new DummySalleApiService();
    private SalleRepository salleRepository = SalleRepository.getInstance(salleApiService);

    //----------------------------------------------------
    //Data
    //----------------------------------------------------

    private MutableLiveData<List<Reunion>> reunions = new MutableLiveData<>();
    private MutableLiveData<List<Salle>> salles = new MutableLiveData<>();

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------

    private MutableLiveData<Integer> deletePosition = new MutableLiveData<>();
    private String filter = "default";

    private Salle salleFilter;
    private Calendar calendarFilter;


    //----------------------------------------------------
    //inisialization
    //----------------------------------------------------

    /**
     * Inisialization
     * <p>
     * setValue reunionRepository to MutableLiveData reunions or filter with date or filter with Salle
     * <p>
     * setValue salleRepository to MutableLiveData salles
     */
    public void init() {
        switch (filter) {
            case "default":
                reunions.setValue(reunionRepository.getReunions());
                break;
            case "date":
                reunions.setValue(reunionRepository.getReunionFilterByDate(calendarFilter));
                break;
            case "lieu":
                reunions.setValue(reunionRepository.getReunionFilterByVenue(salleFilter));
                break;
            default:
                reunions.setValue(reunionRepository.getReunions());
        }

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

        Calendar calendarEnd = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));


        calendarEnd.add(Calendar.MINUTE, dureeToMinute);
        List<Reunion> reunion_To_Compare = new ArrayList<>(reunions.getValue());
        for (Reunion reunion : reunion_To_Compare) {
            Calendar endOfReunion = new GregorianCalendar(reunion.getDate().get(Calendar.YEAR), reunion.getDate().get(Calendar.MONTH), reunion.getDate().get(Calendar.DAY_OF_MONTH), reunion.getDate().get(Calendar.HOUR_OF_DAY), reunion.getDate().get(Calendar.MINUTE), reunion.getDate().get(Calendar.SECOND));


            endOfReunion.add(Calendar.MINUTE, (int) reunion.getDuration());

            //calcul if a reunion is created on a other Reunion
            if ((reunion.getDate().after(calendar) & reunion.getDate().before(calendarEnd)) || reunion.getDate().equals(calendar) || (reunion.getDate().before(calendar) & endOfReunion.after(calendar))) {
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
        filter = "default";
        init();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByDate(Calendar calendar) {
        //TODO methode a faire
        calendarFilter = calendar;
        filter = "date";
        init();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByLieu(Salle salle) {
        salleFilter = salle;
        filter = "lieu";
        init();
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

    /**
     * get list of Email in one string
     *
     * @param reunion reunion to get a list of email
     * @return list of email in one string
     */
    public String listOfEmailInString(Reunion reunion) {
        String listofEmail = null;
        for (String Email : reunion.getEmail_Person()) {
            if (listofEmail == null) {
                listofEmail = Email;
            } else {
                listofEmail = listofEmail + ", " + Email;
            }
        }
        return listofEmail;
    }
}
