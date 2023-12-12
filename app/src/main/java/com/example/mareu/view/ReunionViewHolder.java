package com.example.mareu.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.model.Reunion;
import com.example.mareu.viewmodel.ReunionViewModel;

import java.util.Calendar;

public class ReunionViewHolder extends RecyclerView.ViewHolder {


    private TextView textViewNameHeureSalle;
    private TextView textViewEmail;
    private ImageButton deleteButton;



    public ReunionViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewNameHeureSalle = itemView.findViewById(R.id.item_list_name);
        textViewEmail = itemView.findViewById(R.id.item_list_email);
        deleteButton = itemView.findViewById(R.id.item_list_delete_button);
    }

    public void bind(Reunion reunion, ReunionListAdapter.Listener callback) {

        textViewNameHeureSalle.setText(reunion.getName() + " - " + reunion.getDate().get(Calendar.HOUR_OF_DAY) + "H" + reunion.getDate().get(Calendar.MINUTE) + " - " + reunion.getVenue().getLieu());

        //recupération de la liste des Emails dans un seul string
        ReunionViewModel reunionViewModel = new ReunionViewModel();
        String textEmail = reunionViewModel.listOfEmailInString(reunion);

        textViewEmail.setText(textEmail);
        deleteButton.setOnClickListener(view -> callback.onClickDelete(reunion, getAdapterPosition()));
    }

}


