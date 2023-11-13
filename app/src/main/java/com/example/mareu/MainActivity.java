package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.viewmodel.ReunionViewModel;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ReunionViewModel reunionViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);

        reunionViewModel.init();
        reunionViewModel.getReunions().observe(this, newReunion -> {
            binding.text.setText(newReunion.get(0).getName());
        });


    }
}