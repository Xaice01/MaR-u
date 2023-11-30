package com.example.mareu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.mareu.databinding.ActivityMainBinding;
import com.example.mareu.model.Reunion;
import com.example.mareu.model.Salle;
import com.example.mareu.view.CreateViewActivity;
import com.example.mareu.view.ReunionListAdapter;
import com.example.mareu.viewmodel.ReunionViewModel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


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
        reunionViewModel.getReunions().observe(this, list -> listAdapter.submitList(list));
        //for the delete of item
        reunionViewModel.getDeletePosition().observe(this, position -> listAdapter.notifyItemRemoved(position));


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
        if (item.getItemId() == R.id.menu_filtre_date) {
            Toast.makeText(this, "Menu filtre date selected", Toast.LENGTH_SHORT).show();
            Calendar calendar = datePicker();
            Toast.makeText(this, calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.menu_filtre_lieu) {
            createSubMenu(item);
            return true;
        }
        if (item.getItemId() == R.id.menu_filtre_resert) {
            reunionViewModel.getReunions();
            Toast.makeText(this, "Menu filtre Reset selected", Toast.LENGTH_SHORT).show();

            return true;
        }

        List<Salle> listSalle = reunionViewModel.getSalles().getValue();
        for (int i = 0; listSalle.size() > i; i++) {
            if (item.getItemId() == (100 + i)) {
                reunionViewModel.getReunionsByLieu(listSalle.get(i));
                Toast.makeText(this, "test test test test " + (100 + i), Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * for the delete Reunion
     *
     * @param reunion  reunion to delete
     * @param position position of this reunion
     */
    @Override
    public void onClickDelete(Reunion reunion, int position) {
        reunionViewModel.deleteReunion(reunion, position);
    }

    private Calendar datePicker() {
        Calendar calendar = new GregorianCalendar();

        final Calendar c = Calendar.getInstance();
        int lastSelectedYear = c.get(Calendar.YEAR);
        int lastSelectedMonth = c.get(Calendar.MONTH);
        int lastSelectedDay = c.get(Calendar.DAY_OF_MONTH);

        // Date Set Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {

            calendar.set(year, month, dayOfMonth);
            //filtre with a calendar with date select by user
            reunionViewModel.getReunionsByDate(calendar);
            Toast.makeText(this, "test test in calendar ", Toast.LENGTH_SHORT).show();
        };

        // Create DatePickerDialog:
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDay);

        // Show
        datePickerDialog.show();
        return calendar;
    }

    private void createSubMenu(MenuItem parentItem) {
        // Créer un sous-menu
        SubMenu subMenu = parentItem.getSubMenu();

        if (subMenu.size() < 1) {
            List<Salle> listSalle = reunionViewModel.getSalles().getValue();
            for (int i = 0; listSalle.size() > i; i++) {
                subMenu.add(0, 100 + i, i, listSalle.get(i).getLieu());
            }
        }
        // Afficher le sous-menu
        parentItem.expandActionView();
    }
}



