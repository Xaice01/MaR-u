package com.example.mareu.main;

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

import com.example.mareu.R;
import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.view.CreateViewActivity;
import com.example.mareu.view.ReunionListAdapter;
import com.example.mareu.viewmodel.ReunionViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataTransferInterface, ReunionListAdapter.Listener {

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

        listAdapter = new ReunionListAdapter(this, this);

        //configure reunionViewModel
        reunionViewModel = new ViewModelProvider(this).get(ReunionViewModel.class);
        reunionViewModel.getReunions().observe(this, list -> listAdapter.submitList(list));
        //for the delete of item
        //reunionViewModel.getDeletePosition().observe(this, position -> listAdapter.notifyItemRemoved(position));


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
    public boolean onOptionsItemSelected(MenuItem item) {
        //createMenu and Submenu
        reunionViewModel.selectedMenu(item, this);
        return super.onOptionsItemSelected(item);
    }

    /**
     * for the delete Reunion
     *
     * @param reunion  reunion to delete
     * @param position position of this reunion
     */
    @Override
    public void onClickDelete(Reunion reunion, int position) throws InterruptedException {
        reunionViewModel.deleteReunion(reunion, position);
    }

    @Override
    public void onSetValues(ArrayList<?> al) {

    }
}



