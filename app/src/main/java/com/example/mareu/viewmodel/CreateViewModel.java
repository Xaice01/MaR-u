package com.example.mareu.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.R;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;
import com.example.mareu.model.service.DummyReunionApiService;
import com.example.mareu.model.service.DummySalleApiService;
import com.example.mareu.model.service.ReunionApiService;
import com.example.mareu.model.service.SalleApiService;
import com.example.mareu.model.usecase.CreateReunionUseCase;
import com.example.mareu.model.usecase.GetIdForNewReunionUseCase;
import com.example.mareu.model.usecase.GetSalleAvailableUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * ViewModel Use to Create Reunion
 */
public class CreateViewModel extends ViewModel {

    //----------------------------------------------------
    //Repository
    //----------------------------------------------------

    //injection de dépendance Data:DummyReunionApiService()
    private final ReunionApiService reunionApiService = new DummyReunionApiService();
    private final ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);


    //injection de dépendance Data:DummySalleApiService()
    private final SalleApiService salleApiService = new DummySalleApiService();
    private final SalleRepository salleRepository = SalleRepository.getInstance(salleApiService);

    private final CreateReunionUseCase createReunionUseCase = new CreateReunionUseCase(reunionRepository);

    private final GetIdForNewReunionUseCase getIdForNewReunionUseCase = new GetIdForNewReunionUseCase(reunionRepository);
    private final GetSalleAvailableUseCase getSalleAvailableUseCase = new GetSalleAvailableUseCase(reunionRepository, salleRepository);

    //----------------------------------------------------
    //Create View
    //----------------------------------------------------

    //----------------------------------------------------
    //Data
    //----------------------------------------------------

    public MutableLiveData<Calendar> datePick = new MutableLiveData<>();

    public MutableLiveData<Calendar> hourStart = new MutableLiveData<>();
    public MutableLiveData<Calendar> hourEnd = new MutableLiveData<>();

    public MutableLiveData<ArrayAdapter<String>> listOfSalleAvailaibleAdapterItems = new MutableLiveData<>();
    public MutableLiveData<ArrayList<String>> listOfParticipant = new MutableLiveData<>();

    private final MutableLiveData<List<Salle>> salles = new MutableLiveData<>();

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------

    /**
     * ChoiceTime use for TimePicker for select hour of the start of Reunion or hour of the end of reunion
     */
    public enum ChoiceTime {start, end}

    int duration;
    int selectYearToCreate;
    int selectMonthToCreate;
    int selectDayToCreate;
    int selectHourToCreate;
    int selectMinuteToCreate;

    /**
     * Inisialization
     * <p>
     * setValue salleRepository to MutableLiveData salles
     */
    public void inisialisation() {
        salles.setValue(salleRepository.getSalles());
        listOfParticipant.setValue(new ArrayList<>());
    }

    //----------------------------------------------------
    //fonction
    //----------------------------------------------------

    /**
     * Create a Reunion
     *
     * @param reunion reunion to create
     */
    public void createReunion(Reunion reunion) {
        createReunionUseCase.createReunion(reunion);
    }

    /**
     * Get Salle with the string name of this
     *
     * @param nameOfSalle string of the name of Salle
     * @return salleToReturn objet Salle
     */
    public Salle getSalleWithString(String nameOfSalle) {
        Salle salleToReturn = null;
        for (Salle salle : Objects.requireNonNull(salles.getValue())) {
            if (Objects.equals(salle.getLieu(), nameOfSalle)) {
                salleToReturn = salle;
            }
        }
        return salleToReturn;
    }

    //user select date and show this date
    private void datePicker(Context context) {
        Calendar calendar = new GregorianCalendar();

        final Calendar c = Calendar.getInstance();
        int lastSelectedYear = c.get(Calendar.YEAR);
        int lastSelectedMonth = c.get(Calendar.MONTH);
        int lastSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // Date Set Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {

            calendar.set(year, month, dayOfMonth);
            //get date for createView and show date
            datePick.setValue(calendar);
            selectYearToCreate = year;
            selectMonthToCreate = month;
            selectDayToCreate = dayOfMonth;

        };
        // Create DatePickerDialog:
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);

        // Show
        datePickerDialog.show();
    }

    //user select time and show time
    private void timepicker(Context context, CreateViewModel.ChoiceTime choice) {
        Calendar calendar = new GregorianCalendar();

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int curentHour = c.get(Calendar.HOUR_OF_DAY);
        int curentMinute = c.get(Calendar.MINUTE);

        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            calendar.set(0, 0, 0, hourOfDay, minute);
            switch (choice) {
                case start:
                    hourStart.setValue(calendar);
                    selectHourToCreate = hourOfDay;
                    selectMinuteToCreate = minute;

                case end:
                    hourEnd.setValue(calendar);
                    duration = (hourOfDay - selectHourToCreate) * 60 + (minute - selectMinuteToCreate);
                    initializeSalleReunion(context);
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                timeSetListener, curentHour, curentMinute, true);

        // Show
        timePickerDialog.show();
    }

    //initialize the list of Venue available
    private void initializeSalleReunion(Context context) {
        Calendar calendar = (Calendar) new GregorianCalendar(selectYearToCreate, selectMonthToCreate, selectDayToCreate, selectHourToCreate, selectMinuteToCreate, 0);
        listOfSalleAvailaibleAdapterItems.setValue(new ArrayAdapter<>(context, R.layout.item_list_salle, getSalleAvailableUseCase.getSalleAvailable(calendar, duration)));
    }

    /**
     * select Date for new Reunion
     *
     * @param context context of CreateViewActivity
     */
    public void datePickerCreateView(Context context) {
        datePicker(context);
    }

    /**
     * select Hour and minute to start Reunion
     *
     * @param context context of CreateViewActivity
     */
    public void timePickerStart(Context context) {
        timepicker(context, CreateViewModel.ChoiceTime.start);
    }

    /**
     * select Hour and minute to end Reunion
     *
     * @param context context of CreateViewActivity
     */
    public void timePickerEnd(Context context) {
        timepicker(context, CreateViewModel.ChoiceTime.end);
    }

    /**
     * for add a email to a list of Email in createView
     *
     * @param email email to add of the list
     */
    public void addToListOfParticipant(String email) {
        ArrayList<String> listOfEmail = listOfParticipant.getValue();
        Objects.requireNonNull(listOfEmail).add(email);
        listOfParticipant.setValue(listOfEmail);
    }

    /**
     * for show the emails in textView
     *
     * @param strings (ArrayList<String>) list of email
     */
    public String listOfEmailToShow(ArrayList<String> strings) {
        String listOfEmail = null;
        if (!strings.isEmpty()) {
            for (String email : strings) {
                if (listOfEmail == null) {
                    listOfEmail = email;
                } else {
                    listOfEmail = listOfEmail + " \n" + email;
                }
            }
        }
        return listOfEmail;
    }

    /**
     * create Reunion with only the name of Reunion and name of Salle
     *
     * @param nameOfReunion name of the reunion to create
     * @param nameOfSalle   name of the salle to reunion create
     */
    public void createReunion(String nameOfReunion, String nameOfSalle) {
        Calendar calendar = new GregorianCalendar(selectYearToCreate, selectMonthToCreate, selectDayToCreate, selectHourToCreate, selectMinuteToCreate, 0);
        Reunion reunion = new Reunion(getIdForNewReunionUseCase.getIdForNewReunion(), nameOfReunion, calendar, duration, getSalleWithString(nameOfSalle), listOfParticipant.getValue());
        createReunion(reunion);
    }
}