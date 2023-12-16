package com.example.mareu.viewmodel;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;

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
        reunions.setValue(listToActualise);

        salles.setValue(salleRepository.getSalles());
    }

    //----------------------------------------------------
    //fonction
    //----------------------------------------------------

    /**
     * delete a Reunion
     *
     * @param reunion reunion to delete
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
            datePicker(context);
            return;
        }
        //Created submenu with all of Salle
        if (item.getItemId() == R.id.menu_filtre_lieu) {
            createSubMenu(item);
            return;
        }
        //Resert filter
        if (item.getItemId() == R.id.menu_filtre_reset) {
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
    private void datePicker(Context context) {
        Calendar calendar = new GregorianCalendar();

        final Calendar c = Calendar.getInstance();
        int lastSelectedYear = c.get(Calendar.YEAR);
        int lastSelectedMonth = c.get(Calendar.MONTH);
        int lastSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // Date Set Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {

            calendar.set(year, month, dayOfMonth);

            //filter with a calendar with date select by user
            getReunionsByDate(calendar);

        };
        // Create DatePickerDialog:
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);

        // Show
        datePickerDialog.show();
    }
}
