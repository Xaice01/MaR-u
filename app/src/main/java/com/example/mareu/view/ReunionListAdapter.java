package com.example.mareu.view;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.mareu.R;
import com.example.mareu.model.Reunion;


public class ReunionListAdapter extends ListAdapter<Reunion, ReunionViewHolder> {

    private final Listener callback;

    public interface Listener {
        void onClickDelete(Reunion reunion);
    }

    public ReunionListAdapter(Listener callback) {
        super(DIFF_CALLBACK);
        //TODO a supprimer
        Log.d("Xavier", "entrer dans ReunionListAdapter");
        this.callback = callback;
    }

    @Override
    public ReunionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //TODO a retirer
        Log.d("Xavier", "entrer dans onCreateViewHolder");

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_reunion, parent, false);
        return new ReunionViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReunionViewHolder holder, int position) {
        Log.d("Xavier", "aprés le holder.bind");
        holder.bind(getItem(position), callback);
        Log.d("Xavier", "aprés le holder.bind");
    }

    public static final DiffUtil.ItemCallback<Reunion> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Reunion>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull Reunion oldReunion, @NonNull Reunion newReunion) {
                    // User properties may have changed if reloaded from the DB, but ID is fixed
                    return oldReunion.getId() == newReunion.getId();
                }

                @Override
                public boolean areContentsTheSame(
                        @NonNull Reunion oldReunion, @NonNull Reunion newReunion) {
                    // NOTE: if you use equals, your object must properly override Object#equals()
                    // Incorrectly returning false here will result in too many animations.
                    return oldReunion.equals(newReunion);
                }
            };

}
