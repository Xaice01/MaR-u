package com.example.mareu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.view.CreateViewActivity;
import com.example.mareu.view.ReunionListAdapter;
import com.example.mareu.viewmodel.ReunionViewModel;

/**
 * MainActivity show the list of reunion
 */
public class MainActivity extends AppCompatActivity implements ReunionListAdapter.Listener {

    private ReunionViewModel reunionViewModel;
    private ReunionListAdapter listAdapter;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.mareu.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        listAdapter = new ReunionListAdapter(this);


        //configure reunionViewModel
        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);
        reunionViewModel.init();
        reunionViewModel.getReunions().observe(this, list -> listAdapter.submitList(list));

        //configure RecyclerView
        recyclerView = binding.listReunionRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(listAdapter);


        //navigate to CreateViewActivity on Click to floatingActionButtonAddReunion
        binding.floatingActionButtonAddReunion.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CreateViewActivity.class);
            startActivity(intent);
        });

    }

    /**
     * for the create of filter menu
     *
     * @param menu The options menu in which you place your items.
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * for the item selected
     *
     * @param item The menu item that was selected.
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //createMenu and Submenu
        reunionViewModel.selectedMenu(item, this);
        return super.onOptionsItemSelected(item);
    }

    /**
     * for the delete Reunion
     *
     * @param reunion reunion to delete
     */
    @Override
    public void onClickDelete(Reunion reunion) {
        reunionViewModel.deleteReunion(reunion);
    }

}