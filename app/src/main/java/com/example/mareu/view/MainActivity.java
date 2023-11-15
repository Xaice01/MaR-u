package com.example.mareu.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
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


        //configure reunionViewModel
        //ReunionViewHolder holder= new ReunionViewHolder(view);

        listAdapter = new ReunionListAdapter(this);

        //listAdapter.onCreateViewHolder((ViewGroup) view,view.getId());
        //listAdapter.onBindViewHolder(holder, listAdapter.getItemCount());

        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);
        reunionViewModel.init();
        reunionViewModel.getReunions().observe(this, list -> listAdapter.submitList(list));

        //configure RecyclerView
        recyclerView = binding.listReunionRecyclerview;
        recyclerView.setAdapter(listAdapter);


        //reunionViewModel.getReunions().observe(this, newReunion -> {
        //    binding.text.setText(newReunion.get(0).getName());
        //});
//

    }
    //private void configureRecyclerView() {
    //    recyclerView = binding.listReunionRecyclerview;
    //    listAdapter = new ReunionListAdapter(this);
    //    recyclerView.setAdapter(listAdapter);
    //}


    @Override
    public void onClickDelete(Reunion reunion) {
        reunionViewModel.deleteReunion(reunion);
    }
}