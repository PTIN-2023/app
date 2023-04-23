package com.example.appptin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CistellaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CistellaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout linearLayoutCistella;
    private List<HashMap<String, Object>> cistella = new ArrayList<>();

    public CistellaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CistellaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CistellaFragment newInstance(String param1, String param2) {
        CistellaFragment fragment = new CistellaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        View view = inflater.inflate(R.layout.fragment_cistella, container, false);
        linearLayoutCistella = view.findViewById(R.id.linearLayout_cistella);
        // Afegir un producte exemple per defecte
        afegirProducte("Producte de prova", 1);

        actualitzarCistella();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actualitzarCistella();
    }

    private void afegirProducte(String nomProducte, int quantitat) {
        HashMap<String, Object> producte = new HashMap<>();
        producte.put("nom", nomProducte);
        producte.put("quantitat", quantitat);
        cistella.add(producte);

        // Actualitzar la vista de la cistella
        actualitzarCistella();
    }

    private void actualitzarCistella() {
        linearLayoutCistella.removeAllViews();

        for (HashMap<String, Object> producte : cistella) {
            View producteView = getLayoutInflater().inflate(R.layout.productes_cistella, null);
            TextView nomProducte = producteView.findViewById(R.id.nom_producte);
            TextView quantitatProducte = producteView.findViewById(R.id.quantity_textview);
            ImageButton btnEliminar = producteView.findViewById(R.id.btn_eliminar);
            ImageButton btnMenys = producteView.findViewById(R.id.decrement_button);
            ImageButton btnMes = producteView.findViewById(R.id.increment_button);

            nomProducte.setText((String) producte.get("nom"));
            quantitatProducte.setText(Integer.toString((int) producte.get("quantitat")));

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a eliminar el producte de la cistella
                    cistella.remove(producte);
                    actualitzarCistella();
                }
            });

            btnMenys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a disminuir la quantitat del producte a la cistella
                    int novaQuantitat = (int) producte.get("quantitat") - 1;
                    if (novaQuantitat >= 1) {
                        producte.put("quantitat", novaQuantitat);
                        actualitzarCistella();
                    }
                }
            });

            btnMes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a incrementar la quantitat del producte a la cistella
                    int novaQuantitat = (int) producte.get("quantitat") + 1;
                    producte.put("quantitat", novaQuantitat);
                    actualitzarCistella();
                }
            });

            linearLayoutCistella.addView(producteView);
        }
    }

    //Dos funcions per a ocultar la barra de cerca en aquest fragment
    /*@Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }*/

}