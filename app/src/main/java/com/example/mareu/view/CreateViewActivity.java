package com.example.mareu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
    private int duration = -1;
    List<String> listSalle = new ArrayList<>();
    private String selectionSalle = "Mario";


    //Email
    private ArrayList<String> email = new ArrayList<>();


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
        switch (item.getItemId()) {
            case android.R.id.home:
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

        //TODO a supprimer
        //test
        Calendar calendar = (Calendar) new GregorianCalendar(2018, 6, 25, 15, 40, 0);
        //listSalle = getSalle(calendar, 15);
        listSalle.add("pas de salle");
        listSalle.add("autre chose");
        duration = 15;
        adapterItems = new ArrayAdapter<>(this, R.layout.item_list_salle, listSalle);
        binding.autoCompleteSalleReunion.setAdapter(adapterItems);


        binding.textInputDateEtHeure.setOnClickListener(v -> {
            //recupère la date et heure
            timepicker(false);
            datepicker();
        });

        binding.textInputDuree.setOnClickListener(v -> {
            timepicker(true);
            //Calendar calendar = (Calendar) new GregorianCalendar(lastSelectedYear, lastSelectedMonth, lastSelectedDay, lastSelectedHour, lastSelectedMinute, 0);
            //listSalle.set(getSalle(calendar, duration));
            //adapterItems = new ArrayAdapter<String>(this,R.layout.item_list_salle,listSalle);
            //binding.autoCompleteSalleReunion.setAdapter(adapterItems);
        });

        binding.autoCompleteSalleReunion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //selectionSalle = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.textInputEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(binding.textInputEmail.getText()).isEmpty()) {
                    email.add(String.valueOf(binding.textInputEmail.getText()));
                    writerEmail(email);
                    binding.textInputEmail.setText("");
                }
            }
        });


        binding.buttonCreateReunion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO enlever le commentaire et mettre la fonction dans un if
                //checkIfTextIsEmpty();


                Calendar calendar = (Calendar) new GregorianCalendar(lastSelectedYear, lastSelectedMonth, lastSelectedDay, lastSelectedHour, lastSelectedMinute, 0);

                Reunion reunion = new Reunion(reunionViewModel.getIdForNewReunion(), String.valueOf(binding.textInputNomReunion.getText()), calendar, (long) duration, reunionViewModel.getSalleWithString(selectionSalle), email);

                reunionViewModel.createReunion(reunion);


                Toast.makeText(getApplicationContext(), "created Reunion", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
        boolean check = true;
        String reunion = binding.textInputNomReunion.getText().toString();
        String dateAndHeure = binding.textInputDateEtHeure.getText().toString();
        String duree = binding.textInputDuree.getText().toString();
        String salleDeReunion = binding.autoCompleteSalleReunion.getText().toString();
        String participant = binding.displayTextViewEmailList.getText().toString();

        if (reunion.isEmpty()) {
            binding.outlinedTextFieldNomReunion.setError("entrez un nom de reunion");
            check = false;
        } else binding.outlinedTextFieldNomReunion.setError("");
        if (dateAndHeure.isEmpty()) {
            binding.outlinedTextFieldDateEtHeure.setError("entrez une date");
            check = false;
        } else binding.outlinedTextFieldDateEtHeure.setError("");
        if (duree.isEmpty()) {
            binding.outlinedTextFieldDuree.setError("entrez une duree");
            check = false;
        } else binding.outlinedTextFieldDuree.setError("");
        if (salleDeReunion.isEmpty()) {
            binding.outlinedTextFieldSalleReunion.setError("selectionnez une salle");
            check = false;
        } else binding.outlinedTextFieldSalleReunion.setError("");
        if (participant.isEmpty()) {
            binding.outlinedTextFieldEmail.setError("entrez un Participant");
            check = false;
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
            binding.textInputDateEtHeure.setText(dayOfMonth + "/" + month + "/" + year);
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

    private void timepicker(Boolean duree) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        this.lastSelectedHour = c.get(Calendar.HOUR_OF_DAY);
        this.lastSelectedMinute = c.get(Calendar.MINUTE);


        // Time Set Listener.
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            if (duree) {
                binding.textInputDuree.setText(hourOfDay + "H" + minute);
                duration = (hourOfDay * 60) + minute;
            } else {
                Editable temporarytext = binding.textInputDateEtHeure.getText();
                binding.textInputDateEtHeure.setText(temporarytext + "  " + hourOfDay + "H" + minute);
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };

        // Create TimePickerDialog:
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                timeSetListener, lastSelectedHour, lastSelectedMinute, true);

        // Show
        timePickerDialog.show();
    }
}