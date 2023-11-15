package com.example.mareu.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.databinding.ItemReunionBinding;
import com.example.mareu.model.Reunion;

public class ReunionViewHolder extends RecyclerView.ViewHolder {

    private ItemReunionBinding binding;

    private TextView textViewNameHeureSalle;
    private TextView textViewEmail;
    private ImageButton deleteButton;


    //TODO test binding if not work test itemView.findViewById
    public ReunionViewHolder(@NonNull View itemView) {
        super(itemView);
        //textViewNameHeureSalle = binding.itemListName;
        //textViewEmail = binding.itemListEmail;
        //deleteButton = binding.itemListDeleteButton;
        textViewNameHeureSalle = itemView.findViewById(R.id.item_list_name);
        textViewEmail = itemView.findViewById(R.id.item_list_email);
        deleteButton = itemView.findViewById(R.id.item_list_delete_button);
    }

    public void bind(Reunion reunion, ReunionListAdapter.Listener callback) {
        textViewNameHeureSalle.setText(reunion.getName() + " - " + reunion.getDate().HOUR_OF_DAY + "H" + reunion.getDate().MINUTE + " - " + reunion.getVenue().getLieu());
        String textEmail = reunion.getEmail_Person().get(0);
        if (reunion.getEmail_Person().get(1) != null) {
            textEmail = textEmail + ", " + reunion.getEmail_Person().get(1);
            if (reunion.getEmail_Person().get(2) != null)
                textEmail = textEmail + ", " + reunion.getEmail_Person().get(2);
        }
        textViewEmail.setText(textEmail);
        deleteButton.setOnClickListener(view -> callback.onClickDelete(reunion));
    }


}
