package com.example.mareu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mareu.MainActivity;
import com.example.mareu.R;
import com.example.mareu.databinding.ActivityCreateViewBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.viewmodel.ReunionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

public class CreateViewActivity extends AppCompatActivity {

    private ActivityCreateViewBinding binding;
    private ReunionViewModel reunionViewModel;

    //salle de reunion
    ArrayAdapter<String> adapterItems;
    private int duration = 20;
    List<String> listSalle = new ArrayList<>();


    //Email
    private final ArrayList<String> email = new ArrayList<>();


    //timepicker
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;
    //datepicker
    private int lastSelectedYear = -1;
    private int lastSelectedMonth = -1;
    private int lastSelectedDay = -1;

    //toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Gérer les clics sur les éléments de la barre d'action/toolbar
        if (item.getItemId() == android.R.id.home) {
            //for back to main activity
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //configure reunionViewModel
        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);
        reunionViewModel.init();


        binding.textInputDate.setOnClickListener(v -> {
            //get Date
            datepicker();
        });

        binding.textInputDureeStart.setOnClickListener(v -> {
            //get hour start
            timepicker(false);
        });

        binding.textInputDureeEnd.setOnClickListener(v -> {
            //get hour finish
            timepicker(true);
            //todo regarder la durée
            //initializeSalleReunion();
        });

        binding.autoCompleteSalleReunion.setOnClickListener(v -> initializeSalleReunion());
        binding.autoCompleteSalleReunion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.textInputEmail.setOnClickListener(v -> {
            if (!String.valueOf(binding.textInputEmail.getText()).isEmpty()) {
                email.add(String.valueOf(binding.textInputEmail.getText()));
                writerEmail(email);
                binding.textInputEmail.setText("");
            }
        });


        binding.buttonCreateReunion.setOnClickListener(v -> {
            if (!checkIfTextIsEmpty()) {

                Calendar calendar = (Calendar) new GregorianCalendar(lastSelectedYear, lastSelectedMonth, lastSelectedDay, lastSelectedHour, lastSelectedMinute, 0);

                Reunion reunion = new Reunion(reunionViewModel.getIdForNewReunion(), String.valueOf(binding.textInputNomReunion.getText()), calendar, (long) duration, reunionViewModel.getSalleWithString(String.valueOf(binding.autoCompleteSalleReunion.getText())), email);

                reunionViewModel.createReunion(reunion);

                Toast.makeText(getApplicationContext(), "created Reunion", Toast.LENGTH_SHORT).show();

                /*
                 * back to the mainActivity
                 */
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initializeSalleReunion() {
        Calendar calendar = (Calendar) new GregorianCalendar(lastSelectedYear, lastSelectedMonth, lastSelectedDay, lastSelectedHour, lastSelectedMinute, 0);
        listSalle.addAll(getSalle(calendar, duration));
        adapterItems = new ArrayAdapter<>(this, R.layout.item_list_salle, listSalle);
        binding.autoCompleteSalleReunion.setAdapter(adapterItems);
    }

    private void writerEmail(List<String> listEmail) {
        binding.displayTextViewEmailList.setText("");
        for (String email : listEmail) {
            binding.displayTextViewEmailList.setText(binding.displayTextViewEmailList.getText() + " \n" + email);
        }
    }

    private List<String> getSalle(Calendar calendar, int duration) {
        return reunionViewModel.getSalleAvailable(calendar, duration);
    }

    //Check if one of textfield is empty
    private boolean checkIfTextIsEmpty() {
        boolean check = false;
        String reunion = binding.textInputNomReunion.getText().toString();
        String date = binding.textInputDate.getText().toString();
        String dureeStart = binding.textInputDureeStart.getText().toString();
        String dureeEnd = binding.textInputDureeEnd.getText().toString();
        String salleDeReunion = binding.autoCompleteSalleReunion.getText().toString();
        String participant = binding.displayTextViewEmailList.getText().toString();

        if (reunion.isEmpty()) {
            binding.outlinedTextFieldNomReunion.setError("entrez un nom de reunion");
            check = true;
        } else binding.outlinedTextFieldNomReunion.setError("");
        if (date.isEmpty()) {
            binding.outlinedTextFieldDate.setError("entrez une date");
            check = true;
        } else binding.outlinedTextFieldDate.setError("");
        if (dureeStart.isEmpty()) {
            binding.outlinedTextFieldDureeStart.setError("entrez une heure");
            check = true;
        } else binding.outlinedTextFieldDureeStart.setError("");
        if (dureeEnd.isEmpty()) {
            binding.outlinedTextFieldDureeEnd.setError("entrez une heure");
            check = true;
        } else binding.outlinedTextFieldDureeEnd.setError("");
        if (salleDeReunion.isEmpty()) {
            binding.outlinedTextFieldSalleReunion.setError("selectionnez une salle");
            check = true;
        } else binding.outlinedTextFieldSalleReunion.setError("");
        if (participant.isEmpty()) {
            binding.outlinedTextFieldEmail.setError("entrez un Participant");
            check = true;
        } else binding.outlinedTextFieldEmail.setError("");

        return check;
    }


    private void datepicker() {
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // Date Set Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            binding.textInputDate.setText(dayOfMonth + "/" + month + "/" + year);
            lastSelectedYear = year;
            lastSelectedMonth = month;
            lastSelectedDay = dayOfMonth;
        };

        // Create DatePickerDialog:
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);

        // Show
        datePickerDialog.show();

    }

    private void timepicker(Boolean dureeEnd) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int curentHour = c.get(Calendar.HOUR_OF_DAY);
        int curentMinute = c.get(Calendar.MINUTE);


        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            if (dureeEnd) {
                binding.textInputDureeEnd.setText(hourOfDay + "H" + minute);
                duration = (hourOfDay - lastSelectedHour) * 60 + (minute - lastSelectedMinute);
            } else {
                binding.textInputDureeStart.setText(hourOfDay + "H" + minute);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, curentHour, curentMinute, true);

        // Show
        timePickerDialog.show();
    }
}