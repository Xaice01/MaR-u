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
import com.example.mareu.model.usecase.DeleteReunionUseCase;
import com.example.mareu.model.usecase.FilterReunionByDateUseCase;
import com.example.mareu.model.usecase.FilterReunionByVenueUseCase;
import com.example.mareu.model.usecase.GetReunionsUseCase;
import com.example.mareu.model.usecase.GetSallesUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ReunionViewModel extends ViewModel {
    // region repository

    //injection de dépendance Data:DummyReunionApiService()
    private final ReunionApiService reunionApiService = new DummyReunionApiService();
    private final ReunionRepository reunionRepository = ReunionRepository.getInstance(reunionApiService);

    private final GetReunionsUseCase getReunionsUseCase = new GetReunionsUseCase(reunionRepository);


    //injection de dépendance Data:DummySalleApiService()
    private final SalleApiService salleApiService = new DummySalleApiService();
    private final SalleRepository salleRepository = SalleRepository.getInstance(salleApiService);

    private final GetSallesUseCase getSallesUseCase = new GetSallesUseCase(salleRepository);

    private final FilterReunionByVenueUseCase filterReunionByVenueUseCase = new FilterReunionByVenueUseCase(reunionRepository);
    private final FilterReunionByDateUseCase filterReunionByDateUseCase = new FilterReunionByDateUseCase(reunionRepository);
    private final DeleteReunionUseCase deleteReunionUseCase = new DeleteReunionUseCase(reunionRepository);
    // endregion


    //----------------------------------------------------
    //Data
    //----------------------------------------------------

    private final MutableLiveData<List<Reunion>> reunions = new MutableLiveData<>();
    private final MutableLiveData<List<Salle>> salles = new MutableLiveData<>();

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------


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
        setReunionWithFilter();
        salles.setValue(getSallesUseCase.getSalles());
    }

    /**
     * set the list of reunions to show
     */
    public void setReunionWithFilter() {
        List<Reunion> listToActualise = new ArrayList<>();
        switch (filterMain) {
            case reset:
                listToActualise = getReunionsUseCase.getReunions();
                //reunions.setValue(reunionRepository.getReunions());
                break;
            case date:
                listToActualise = filterReunionByDateUseCase.filterReunionByDate(calendarFilter);
                //reunions.setValue(reunionRepository.getReunionFilterByDate(calendarFilter,listToFilter));
                break;
            case lieu:
                listToActualise = filterReunionByVenueUseCase.filterReunionBySalle(salleFilter);
                //reunions.setValue(reunionRepository.getReunionFilterByVenue(salleFilter,listToFilter));
                break;

            default:
                //reunions.setValue(reunionRepository.getReunions());
        }
        reunions.setValue(listToActualise);
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
        if (deleteReunionUseCase.deleteReunion(reunion)) {
            setReunionWithFilter();
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
        setReunionWithFilter();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByDate(Calendar calendar) {
        calendarFilter = calendar;
        filterMain = filter.date;
        setReunionWithFilter();
        return reunions;
    }

    public LiveData<List<Reunion>> getReunionsByLieu(Salle salle) {
        salleFilter = salle;
        filterMain = filter.lieu;
        setReunionWithFilter();
        return reunions;
    }

    /**
     * get list of Salle
     */
    public LiveData<List<Salle>> getSalles() {
        return salles;
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
        List<Salle> listSalle = getSallesUseCase.getSalles();
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
            List<Salle> listSalle = getSallesUseCase.getSalles();
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
