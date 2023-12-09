package com.example.mareu.viewmodel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class ReunionViewModel extends ViewModel {
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

    public enum filter {reset, date, lieu}

    private filter filterMain = filter.reset;

    private Salle salleFilter;
    private Calendar calendarFilter;

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

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------
    public enum ChoiceTime {start, end}

    public enum ListChoiceDatePicker {filter, create}

    int duration;
    int selectYearToCreate;
    int selectMonthToCreate;
    int selectDayToCreate;
    int selectHourToCreate;
    int selectMinuteToCreate;

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
        List<Reunion> listToFilter = reunions.getValue();
        List<Reunion> listToActualise = new ArrayList<>();
        switch (filterMain) {
            case reset:
                listToActualise = reunionRepository.getReunions();
                //reunions.setValue(reunionRepository.getReunions());
                break;
            case date:
                listToActualise = reunionRepository.getReunionFilterByDate(calendarFilter, listToFilter);
                //reunions.setValue(reunionRepository.getReunionFilterByDate(calendarFilter,listToFilter));
                break;
            case lieu:
                listToActualise = reunionRepository.getReunionFilterByVenue(salleFilter, listToFilter);
                //reunions.setValue(reunionRepository.getReunionFilterByVenue(salleFilter,listToFilter));
                break;

            default:
                //reunions.setValue(reunionRepository.getReunions());
        }
        actualisedLiveDataReunions(listToActualise);

        salles.setValue(salleRepository.getSalles());
        listOfParticipant.setValue(new ArrayList<>());
    }

    private void actualisedLiveDataReunions(List<Reunion> listToActualise) {
        reunions.setValue(listToActualise);
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
     */
    public void deleteReunion(Reunion reunion, int position) {
        //if the reunion is delete
        if (reunionRepository.deleteReunion(reunion)) {
            if (filterMain == filter.reset) {
                deletePosition.setValue(position);
            } else {
                deletePosition.setValue(position);
                init();
            }
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
     * Get all of email in one String
     *
     * @param reunion reunion to have email list
     * @return String with all of email
     */
    public String listOfEmailInString(Reunion reunion) {
        String listofEmail = null;
        for (String Email : reunion.getEmail_Person()) {
            if (listofEmail == null) {
                listofEmail = Email;
            } else {
                listofEmail += ", " + Email;
            }
        }
        return listofEmail;
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
        filterMain = filter.reset;
        init();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByDate(Calendar calendar) {
        calendarFilter = calendar;
        filterMain = filter.date;
        init();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByLieu(Salle salle) {
        salleFilter = salle;
        filterMain = filter.lieu;
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
     * Create a menu and Submenu for filter Reunions
     *
     * @param item    the menu
     * @param context the context for show the date picker in the good View
     */
    public void selectedMenu(MenuItem item, Context context) {

        //filter by date
        if (item.getItemId() == R.id.menu_filtre_date) {
            ListChoiceDatePicker choice = ListChoiceDatePicker.filter;
            datePicker(context, choice);
            return;
        }
        //Created submenu with all of Salle
        if (item.getItemId() == R.id.menu_filtre_lieu) {
            createSubMenu(item);
            return;
        }
        //Resert filter
        if (item.getItemId() == R.id.menu_filtre_resert) {
            getReunions();
            return;
        }
        //Submenu With all of Salle filter by Salle
        List<Salle> listSalle = getSalles().getValue();
        for (int i = 0; listSalle.size() > i; i++) {
            if (item.getItemId() == (100 + i)) {
                getReunionsByLieu(listSalle.get(i));
                return;
            }
        }

    }

    //created a Submenu with the list of Salle
    private void createSubMenu(MenuItem parentItem) {
        // Created a submenu
        SubMenu subMenu = parentItem.getSubMenu();

        //add all of Salle in Submenu
        if (subMenu.size() < 1) {
            List<Salle> listSalle = getSalles().getValue();
            for (int i = 0; listSalle.size() > i; i++) {
                subMenu.add(0, 100 + i, i, listSalle.get(i).getLieu());
            }
        }
        // display a Submenu
        parentItem.expandActionView();
    }

    //user select date and show or filter with date
    private void datePicker(Context context, ListChoiceDatePicker choice) {
        Calendar calendar = new GregorianCalendar();

        final Calendar c = Calendar.getInstance();
        int lastSelectedYear = c.get(Calendar.YEAR);
        int lastSelectedMonth = c.get(Calendar.MONTH);
        int lastSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // Date Set Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {

            calendar.set(year, month, dayOfMonth);

            switch (choice) {
                case filter:
                    //filter with a calendar with date select by user
                    getReunionsByDate(calendar);
                    break;
                case create:
                    //get date for createView and show date
                    datePick.setValue(calendar);
                    selectYearToCreate = year;
                    selectMonthToCreate = month;
                    selectDayToCreate = dayOfMonth;
            }
        };

        // Create DatePickerDialog:
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);

        // Show
        datePickerDialog.show();
    }

    //user select time and show time
    private void timepicker(Context context, ChoiceTime choice) {
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
        listOfSalleAvailaibleAdapterItems.setValue(new ArrayAdapter<>(context, R.layout.item_list_salle, getSalleAvailable(calendar, duration)));
    }

    //select Date for new Reunion
    public void datePickerCreateView(Context context, ListChoiceDatePicker choice) {
        datePicker(context, choice);
    }

    //select Hour and minute to start Reunion
    public void timePickerStart(Context context) {
        timepicker(context, ChoiceTime.start);
    }

    //select Hour and minute to end Reunion
    public void timePickerEnd(Context context) {
        timepicker(context, ChoiceTime.end);
    }


    /**
     * for add a email to a list of Email in createView
     *
     * @param email email to add of the list
     */
    public void addToListOfParticipant(String email) {
        ArrayList<String> listOfEmail = listOfParticipant.getValue();
        listOfEmail.add(email);
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
        Reunion reunion = new Reunion(getIdForNewReunion(), nameOfReunion, calendar, duration, getSalleWithString(nameOfSalle), listOfParticipant.getValue());
        createReunion(reunion);
    }
}
