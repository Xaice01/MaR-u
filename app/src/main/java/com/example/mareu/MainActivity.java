package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.view.ReunionListAdapter;
import com.example.mareu.viewmodel.ReunionViewModel;


public class MainActivity extends AppCompatActivity implements ReunionListAdapter.Listener {

    private ActivityMainBinding binding;


    private ReunionViewModel reunionViewModel;
    private ReunionListAdapter listAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Log.d("Xavier", "aprés le setcontentView");

        listAdapter = new ReunionListAdapter(this);

        //configure reunionViewModel
        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);
        reunionViewModel.init();
        reunionViewModel.getReunions().observe(this, list -> listAdapter.submitList(list));
        //for the delete of item
        reunionViewModel.getDeletePosition().observe(this, position -> listAdapter.notifyItemRemoved(position));

        //configure RecyclerView
        recyclerView = binding.listReunionRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(listAdapter);

    }


    @Override
    public void onClickDelete(Reunion reunion, int position) {
        reunionViewModel.deleteReunion(reunion, position);
    }
}