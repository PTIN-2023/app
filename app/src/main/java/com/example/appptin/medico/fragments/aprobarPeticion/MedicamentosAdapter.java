package com.example.appptin.medico.fragments.aprobarPeticion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.R;

import java.util.ArrayList;

public class MedicamentosAdapter extends RecyclerView.Adapter<MedicamentosAdapter.MyHolderMedicamentos> implements View.OnClickListener {

    Context context;
    ArrayList<String> medicamentosLista;
    LayoutInflater layoutInflater;

    public MedicamentosAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.medicamentosLista = arrayList;
        this.layoutInflater = layoutInflater.from(context);
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public MyHolderMedicamentos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.contenedor_medicamento,parent,false);

        return new MyHolderMedicamentos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderMedicamentos holder, int position) {
        holder.text_medicamento.setText(medicamentosLista.get(position));
    }

    @Override
    public int getItemCount() {
        return medicamentosLista.size();
    }

    public class MyHolderMedicamentos extends RecyclerView.ViewHolder {
        TextView text_medicamento;
        public MyHolderMedicamentos(@NonNull View itemView) {
            super(itemView);
            text_medicamento =itemView.findViewById(R.id.txt_medicamento);
        }
    }
}
