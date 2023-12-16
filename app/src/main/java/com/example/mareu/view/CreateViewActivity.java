package com.example.mareu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mareu.MainActivity;
import com.example.mareu.databinding.ActivityCreateViewBinding;
import com.example.mareu.viewmodel.CreateViewModel;

import java.util.Calendar;
import java.util.Objects;

public class CreateViewActivity extends AppCompatActivity {

    private ActivityCreateViewBinding binding;
    private CreateViewModel createViewModel;


    //Create toolbar
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
        createViewModel = new ViewModelProvider(this).get(CreateViewModel.class);
        createViewModel.inisialisation();


        //for change the text of the Date
        createViewModel.datePick.observe(this, calendar -> binding.textInputDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR)));

        //for change the text of time to start
        createViewModel.hourStart.observe(this, calendar -> binding.textInputDureeStart.setText(calendar.get(Calendar.HOUR_OF_DAY) + "H" + calendar.get(Calendar.MINUTE)));

        //for change the text of time to end
        createViewModel.hourEnd.observe(this, calendar -> binding.textInputDureeEnd.setText(calendar.get(Calendar.HOUR_OF_DAY) + "H" + calendar.get(Calendar.MINUTE)));

        //for change and create the list of salle available
        createViewModel.listOfSalleAvailaibleAdapterItems.observe(this, stringArrayAdapter -> binding.autoCompleteSalleReunion.setAdapter(stringArrayAdapter));

        //for change the list of email
        createViewModel.listOfParticipant.observe(this, strings -> binding.displayTextViewEmailList.setText(createViewModel.listOfEmailToShow(strings)));

        binding.textInputDate.setOnClickListener(v -> {
            //get Date
            createViewModel.datePickerCreateView(this);
        });

        binding.textInputDureeStart.setOnClickListener(v -> {
            //get hour start
            createViewModel.timePickerStart(this);
        });

        binding.textInputDureeEnd.setOnClickListener(v -> {
            //get hour finish
            createViewModel.timePickerEnd(this);
        });
        //get email of participant
        binding.textInputEmail.setOnClickListener(v -> {
            if (!String.valueOf(binding.textInputEmail.getText()).isEmpty()) {
                createViewModel.addToListOfParticipant(String.valueOf(binding.textInputEmail.getText()));
                binding.textInputEmail.setText("");
            }
        });


        binding.buttonCreateReunion.setOnClickListener(v -> {
            if (!checkIfTextIsEmpty()) {
                //create reunion
                createViewModel.createReunion(String.valueOf(binding.textInputNomReunion.getText()), String.valueOf(binding.autoCompleteSalleReunion.getText()));

                Toast.makeText(getApplicationContext(), "created Reunion", Toast.LENGTH_SHORT).show();

                //back to the mainActivity
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    //Check if one of textfield is empty and write a error message
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


}