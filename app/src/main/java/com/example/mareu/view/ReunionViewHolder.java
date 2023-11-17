package com.example.mareu.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mareu.R;
import com.example.mareu.model.Reunion;

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
        //TODO a mettre dans le viewmodel( la logique) (boucle for avec toute le adresse mail)
        textViewNameHeureSalle.setText(reunion.getName() + " - " + reunion.getDate().HOUR_OF_DAY + "H" + reunion.getDate().MINUTE + " - " + reunion.getVenue().getLieu());
        String textEmail = reunion.getEmail_Person().get(0);
        if (reunion.getEmail_Person().size() > 1) {
            textEmail = textEmail + ", " + reunion.getEmail_Person().get(1);
            if (reunion.getEmail_Person().size() > 2)
                textEmail = textEmail + ", " + reunion.getEmail_Person().get(2);
        }
        textViewEmail.setText(textEmail);
        deleteButton.setOnClickListener(view -> callback.onClickDelete(reunion, getAdapterPosition()));
    }


}
