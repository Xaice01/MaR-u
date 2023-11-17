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
    //TODO viewModel Factory a faire
    //----------------------------------------------------
    //Repository
    //----------------------------------------------------

    //injection de dépendance Data:DummyReunionApiService()
    private ReunionApiService reunionApiService = new DummyReunionApiService();
    private ReunionRepository reunionRepository = new ReunionRepository(reunionApiService);


    //injection de dépendance Data:DummySalleApiService()
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

    private MutableLiveData<Integer> deletePosition = new MutableLiveData<>();
    private MutableLiveData<String> filter;



    //----------------------------------------------------
    //inisialization
    //----------------------------------------------------

    /**
     * Inisialization
     * <p>
     * setValue reunionRepository to MutableLiveData reunions
     * setValue salleRepository to MutableLiveData salles
     */
    public void init() {
        reunions.setValue(reunionRepository.getReunions());
        salles.setValue(salleRepository.getSalles());
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
        reunionRepository.createReunion(reunion);
    }

    /**
     * delete a Reunion
     *
     * @param reunion  reunion to delete
     * @param position position of item delete
     */
    public void deleteReunion(Reunion reunion, int position) {
        //if the reunion is delete
        if (reunionRepository.deleteReunion(reunion)) {
            deletePosition.setValue(position);
        } else {
            throw new IllegalArgumentException("reunion not found");
        }
    }

    /**
     * get list of Reunion
     */
    public LiveData<List<Reunion>> getReunions() {
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


}
