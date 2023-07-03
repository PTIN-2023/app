package com.example.appptin.medico.fragments.recetas.crear;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;

import org.json.JSONArray;
import org.json.JSONException;

public class MedsRecipeAdapter extends RecyclerView.Adapter<MedsRecipeAdapter.MyViewHolder> {

    private JSONArray list_meds;

    public MedsRecipeAdapter(JSONArray list_meds) {
        this.list_meds = list_meds;
    }

    @NonNull
    @Override
    public MedsRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recepta_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedsRecipeAdapter.MyViewHolder holder, int position) {
        try {
            holder.codiTextView.setText(list_meds.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView codiTextView;
        TextView quantTextView;
        public MyViewHolder(View view) {
            super(view);
            codiTextView = view.findViewById(R.id.recipe_codi_text);
            quantTextView = view.findViewById(R.id.recipe_quant_text);
        }
    }
}
