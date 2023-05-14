package com.example.appptin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

    private PopupWindow popupWindow;

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
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.fragment_cistella, container, false);
        linearLayoutCistella = view.findViewById(R.id.linearLayout_cistella);

        Button finalitzar_compra = view.findViewById(R.id.btn_finalitzar_compra);

        // Afegir un producte exemple per defecte
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);
        afegirProducte(view,"Producte de prova", 1, 1);

        finalitzar_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obrir pop-up
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        initPopup();

        actualitzarPreu(view);
        actualitzarCistella(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //actualitzarCistella();
    }

    private void afegirProducte(View viewPreu, String nomProducte, int quantitat, float preu) {
        HashMap<String, Object> producte = new HashMap<>();
        producte.put("nom", nomProducte);
        producte.put("quantitat", quantitat);
        producte.put("preu", preu);
        cistella.add(producte);

        // Actualitzar la vista de la cistella
        actualitzarPreu(viewPreu);
        actualitzarCistella(viewPreu);
    }

    private void actualitzarCistella(View viewPreu) {
        linearLayoutCistella.removeAllViews();

        for (HashMap<String, Object> producte : cistella) {
            View producteView = getLayoutInflater().inflate(R.layout.productes_cistella, null);
            TextView nomProducte = producteView.findViewById(R.id.nom_producte);
            TextView preuProducte = producteView.findViewById(R.id.preu_producte);
            TextView quantitatProducte = producteView.findViewById(R.id.quantity_textview);
            ImageButton btnEliminar = producteView.findViewById(R.id.btn_eliminar);
            ImageButton btnMenys = producteView.findViewById(R.id.decrement_button);
            ImageButton btnMes = producteView.findViewById(R.id.increment_button);

            nomProducte.setText((String) producte.get("nom"));
            preuProducte.setText(Float.toString((float) producte.get("preu")) + " €");
            quantitatProducte.setText(Integer.toString((int) producte.get("quantitat")));

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a eliminar el producte de la cistella
                    cistella.remove(producte);
                    actualitzarCistella(viewPreu);
                }
            });

            btnMenys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a disminuir la quantitat del producte a la cistella
                    int novaQuantitat = (int) producte.get("quantitat") - 1;
                    if (novaQuantitat >= 1) {
                        producte.put("quantitat", novaQuantitat);
                        actualitzarCistella(viewPreu);
                    }
                }
            });

            btnMes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a incrementar la quantitat del producte a la cistella
                    int novaQuantitat = (int) producte.get("quantitat") + 1;
                    producte.put("quantitat", novaQuantitat);
                    actualitzarCistella(viewPreu);
                }
            });

            linearLayoutCistella.addView(producteView);
        }
        actualitzarPreu(viewPreu);
    }

    private void actualitzarPreu(View ViewPreu) {
        float preuTotal = 0;

        for (HashMap<String, Object> producte : cistella) {
            float preuProducte = (float) producte.get("preu");
            int quantitatProducte = (int) producte.get("quantitat");
            preuTotal += preuProducte * quantitatProducte;
        }
        //View preuView = getLayoutInflater().inflate(R.layout.fragment_cistella, null);
        TextView preuTotalView = ViewPreu.findViewById(R.id.preu_total);
        preuTotalView.setText("Preu total: " + preuTotal + "€");
    }

    private void initPopup() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.fragment_popup_finalitzar_compra, null);

        // Crear pop-up window
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Configurar elements del pop-up
        Button btn_finNo = popupView.findViewById(R.id.btn_finalitzar_compraNo);
        Button btn_finSi = popupView.findViewById(R.id.btn_finalitzar_compraSi);


        btn_finNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el text en la pantalla home
                popupWindow.dismiss();
            }
        });

        //Canviar a la activity de pagament al clickar el botó de "Si"
        //al pop-up de finalitzar compra
        btn_finSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el text en la pantalla home
                Intent intent = new Intent(getActivity(), PagamentActivity.class);
                startActivity(intent);
            }
        });
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