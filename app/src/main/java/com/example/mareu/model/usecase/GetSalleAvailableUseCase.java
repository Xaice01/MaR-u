package com.example.mareu.model.usecase;

import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.model.repository.ReunionRepository;
import com.example.mareu.model.repository.SalleRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Use Case get get list of Salle available in create view
 */
public class GetSalleAvailableUseCase {
    private final ReunionRepository reunionRepository;
    private final SalleRepository salleRepository;

    public GetSalleAvailableUseCase(ReunionRepository reunionRepository, SalleRepository salleRepository) {
        this.reunionRepository = reunionRepository;
        this.salleRepository = salleRepository;
    }

    /**
     * get list of Salle available
     *
     * @param calendar      the date and hour of Reunion
     * @param dureeToMinute duration of Reunion
     */
    public List<String> getSalleAvailable(Calendar calendar, int dureeToMinute) {
        List<String> listSalle = new ArrayList<>();
        for (Salle salle : salleRepository.getSalles()) {
            listSalle.add(salle.getLieu());
        }

        Calendar calendarEnd = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

        calendarEnd.add(Calendar.MINUTE, dureeToMinute);
        List<Reunion> reunion_To_Compare = new ArrayList<>(reunionRepository.getReunions());
        for (Reunion reunion : reunion_To_Compare) {
            Calendar endOfReunion = new GregorianCalendar(reunion.getDate().get(Calendar.YEAR), reunion.getDate().get(Calendar.MONTH), reunion.getDate().get(Calendar.DAY_OF_MONTH), reunion.getDate().get(Calendar.HOUR_OF_DAY), reunion.getDate().get(Calendar.MINUTE), reunion.getDate().get(Calendar.SECOND));

            endOfReunion.add(Calendar.MINUTE, (int) reunion.getDuration());

            //calcul if a reunion is created on a other Reunion
            if ((reunion.getDate().after(calendar) & reunion.getDate().before(calendarEnd)) || reunion.getDate().equals(calendar) || (reunion.getDate().before(calendar) & endOfReunion.after(calendar))) {
                listSalle.remove(reunion.getVenue().getLieu());
            }

            if (listSalle.isEmpty()) {
                listSalle.add("No Salle Available");
            }
        }
        return listSalle;
    }
}
