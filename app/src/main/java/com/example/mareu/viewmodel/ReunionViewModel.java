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

import java.util.List;

public class ReunionViewModel extends ViewModel {
    //----------------------------------------------------
    //Repository
    //----------------------------------------------------

    //injection de dépendance
    private ReunionApiService reunionApiService = new DummyReunionApiService();
    private ReunionRepository reunionRepository = new ReunionRepository(reunionApiService);

    //injection de dépendance
    private SalleApiService salleApiService = new DummySalleApiService();
    private SalleRepository salleRepository = new SalleRepository(salleApiService);

    //----------------------------------------------------
    //Data
    //----------------------------------------------------

    private MutableLiveData<List<Reunion>> reunions = new MutableLiveData<>();
    private MutableLiveData<List<Salle>> salles = new MutableLiveData<>();

    //----------------------------------------------------
    //Variable
    //----------------------------------------------------

    private MutableLiveData<String> filter;


    //----------------------------------------------------
    //Inisialisation
    //----------------------------------------------------
    public void init() {
        reunions.setValue(reunionRepository.getReunions());
        salles.setValue(salleRepository.getSalles());
    }

    //----------------------------------------------------
    //fonction
    //----------------------------------------------------
    public void createReunion(Reunion reunion) {
        reunionRepository.createReunion(reunion);
    }

    public void deleteReunion(Reunion reunion) {
        reunionRepository.deleteReunion(reunion);
    }

    public LiveData<List<Reunion>> getReunions() {
        return reunions;
    }

    public LiveData<List<Salle>> getSalles() {
        return salles;
    }


}
