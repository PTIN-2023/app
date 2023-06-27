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
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private float preu;

    private LinearLayout linearLayoutCistella;
    private List<HashMap<String, Object>> cistella = new ArrayList<>();

    private List<HashMap<String, Object>> cistellaOriginal = new ArrayList<>();

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


        SearchView searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<HashMap<String, Object>> filteredCistella = filter(cistellaOriginal, newText);
                actualitzarCistellaConFiltro(filteredCistella);
                return false;
            }
        });

        // Afegir un productes
        try {
            afegirProducte(view);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

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

    private void afegirProducte(View viewPreu) throws JSONException {

        //Leer JSONArray creado en la ventana Medicamentos que contiene la lista de medicamentos añadidos

        JSONArray lista_cesta = MainActivity.getListaMedicamentos();

        for (int i = 0; i < lista_cesta.length(); i++) {
            JSONObject jsonObject = lista_cesta.getJSONObject(i);
            System.out.println("Medicament: "+ jsonObject);
            // Obtener los valores de los campos del objeto JSON
            String nationalCode = jsonObject.getString("nationalCode");
            String medName = jsonObject.getString("medName");
            float pvp = (float) jsonObject.getDouble("pvp");
            int cantidad = jsonObject.getInt("quantitat");
            int limit_quantity = jsonObject.getInt("quantity_available");



            // Crear HashMap con los valores del OBJECT
            HashMap<String, Object> producte = new HashMap<>();
            producte.put("nationalCode", nationalCode);
            producte.put("nom", medName);
            producte.put("quantitat", cantidad);
            producte.put("preu", pvp);
            producte.put("limit_disponible", limit_quantity);


            // Añadirlos a la lista
            cistella.add(producte);
            cistellaOriginal = new ArrayList<>(cistella);

        }


        // Actualitzar la vista de la cistella
        actualitzarPreu(viewPreu);
        actualitzarCistella(viewPreu);
    }

    private void actualitzarCistella(View viewPreu) {
        linearLayoutCistella.removeAllViews();

        // Listar todos los productos a mostrar
        int i = 0;
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

            // Eliminar producto
            int finalI = i;
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a eliminar el producte de la cistella
                    cistella.remove(producte);
                    actualitzarCistella(viewPreu);
                    MainActivity.deleteToCart(finalI);
                }
            });

            // Restar cantidad producto
            btnMenys.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a disminuir la quantitat del producte a la cistella
                    int novaQuantitat = (int) producte.get("quantitat") - 1;
                    if (novaQuantitat >= 1) {
                        producte.put("quantitat", novaQuantitat);
                        actualitzarCistella(viewPreu);
                        try {
                            MainActivity.getCantidadMedicamento(finalI,-1);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            // Sumar Cantidad producto
            btnMes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acció per a incrementar la quantitat del producte a la cistella

                    int novaQuantitat = (int) producte.get("quantitat") + 1;
                    if (novaQuantitat <= (int) producte.get("limit_disponible")) {
                        producte.put("quantitat", novaQuantitat);
                        actualitzarCistella(viewPreu);
                    }
                    try {
                        MainActivity.getCantidadMedicamento(finalI,1);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            i+=1;
            linearLayoutCistella.addView(producteView);
        }
        actualitzarPreu(viewPreu);


    }

    private List<HashMap<String, Object>> filter(List<HashMap<String, Object>> cistellaOriginal, String query) {
        List<HashMap<String, Object>> filteredCistella = new ArrayList<>();
        for (HashMap<String, Object> producte : cistellaOriginal) {
            String nomProducte = (String) producte.get("nom");
            if (nomProducte.toLowerCase().contains(query.toLowerCase())) {
                filteredCistella.add(producte);
            }
        }
        return filteredCistella;
    }

    private void actualitzarCistellaConFiltro(List<HashMap<String, Object>> filteredCistella) {
        linearLayoutCistella.removeAllViews();
        for (HashMap<String, Object> producte : filteredCistella) {
            View producteView = getLayoutInflater().inflate(R.layout.productes_cistella, null);
            TextView nomProducte = producteView.findViewById(R.id.nom_producte);
            TextView preuProducte = producteView.findViewById(R.id.preu_producte);
            TextView quantitatProducte = producteView.findViewById(R.id.quantity_textview);

            nomProducte.setText((String) producte.get("nom"));
            preuProducte.setText(Float.toString((float) producte.get("preu")) + " €");
            quantitatProducte.setText(Integer.toString((int) producte.get("quantitat")));

            linearLayoutCistella.addView(producteView);
        }
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

        preu = preuTotal;
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
                intent.putExtra("preuTotal", preu);
                startActivity(intent);
                popupWindow.dismiss();
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