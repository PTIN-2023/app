package com.example.appptin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.medico.conexion.InformacionBase;
import com.example.appptin.medico.fragments.historialPeticion.PeticionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicamentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicamentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerMedicaments;
    private AlertDialog.Builder builder;

    public MedicamentsFragment() {
        // Instancia de la lista de medicamentos para la cesta

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicamentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicamentsFragment newInstance(String param1, String param2) {
        MedicamentsFragment fragment = new MedicamentsFragment();
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
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_medicaments, container, false);
        recyclerMedicaments = view.findViewById(R.id.medicaments_recycler);

        ArrayList<Medicament> list_medicament = new ArrayList<>();


        //Medicament de prova
        //Medicament medicamentDeProva1 = new Medicament("Medicament de prova", "123456789", "Ús de prova", "Administració de prova", false, 9.99, "Forma de prova", new ArrayList<>());
        //Medicament medicamentDeProva2 = new Medicament("Medicament de prova", "123456789", "Ús de prova", "Administració de prova", false, 9.99, "Forma de prova", new ArrayList<>());
        //list_medicament.add(medicamentDeProva1);
        //list_medicament.add(medicamentDeProva2);

        // Agafar filtres
        Bundle args = getArguments();
        if (args != null) {
            String medName = args.getString("medName");
            String pvpMin = args.getString("minPrice");
            String pvpMax = args.getString("maxPrice");
            boolean prescriptionNeeded = args.getBoolean("prescriptionNeeded");
            ArrayList type_of_administration = args.getStringArrayList("via");
            ArrayList form = args.getStringArrayList("format");

            // mostrem resultats
            System.out.println("Nom Medicament: " + medName + "\nMin Price: " + pvpMin + "\nMax Price: " + pvpMax + "\nPrescription Needed: " + prescriptionNeeded);
            System.out.println("Via: ");
            for(int i = 0; i < type_of_administration.size(); i++){
                System.out.println(type_of_administration.get(i));
            }
            System.out.println("Format: ");
            for(int i = 0; i < form.size(); i++){
                System.out.println(form.get(i));
            }
        }


        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_available_medicines"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONArray  jsonBody = new JSONArray();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");
            System.out.println(session_token);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("session_token", session_token);
            jsonBody.put(jsonObject);

/*            jsonBody.put("filter", False);
            jsonBody.put("meds_per_page", 1);
            jsonBody.put("page", 1);
            if (medName != null && !medName.isEmpty()) {
                jsonBody.put("med_name", medName);
            }
            if (pvpMin != null) {
                jsonBody.put("pvp_min", pvpMin);
            }
            if (pvpMax != null) {
                jsonBody.put("pvp_max", pvpMax);
            }
            if (prescriptionNeeded != null) {
                jsonBody.put("prescription_needed", prescriptionNeeded);
            }
            if (form != null && !form.isEmpty()) {
                jsonBody.put("form", form);
            }
            if (typeOfAdministration != null && !typeOfAdministration.isEmpty()) {
                jsonBody.put("type_of_administration", typeOfAdministration);
           }*/

            //    jsonBody.put("filter", False);
            //    jsonBody.put("meds_per_page", 1);
            //    jsonBody.put("page", 1);
            //    if (medName != null && !medName.isEmpty()) {
            //        jsonBody.put("med_name", medName);
            //    }
            //    if (pvpMin != null) {
            //        jsonBody.put("pvp_min", pvpMin);
            //    }
            //    if (pvpMax != null) {
            //        jsonBody.put("pvp_max", pvpMax);
            //    }
            //    if (prescriptionNeeded != null) {
            //        jsonBody.put("prescription_needed", prescriptionNeeded);
            //    }
            //    if (form != null && !form.isEmpty()) {
            //        jsonBody.put("form", form);
            //    }
            //    if (typeOfAdministration != null && !typeOfAdministration.isEmpty()) {
            //        jsonBody.put("type_of_administration", typeOfAdministration);
            //    }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        // Acceder a los campos del objeto JSON
                        String typeOfAdministration = jsonObject.getString("administracio");
                        String nationalCode = jsonObject.getString("codi_nacional");
                        String form = jsonObject.getString("form");
                        String medName = jsonObject.getString("medicine_identifier");
                        String useType = jsonObject.getString("presentacio");
                        double pvp = jsonObject.getDouble("preu");

                        JSONArray jsonarray_prospecto = jsonObject.getJSONArray("prospecto");
                        ArrayList<String> excipients = new ArrayList<String>();
                        for (int j = 0; j < jsonarray_prospecto.length(); j++) {
                            excipients.add(jsonarray_prospecto.getString(j));
                        }

                        boolean prescriptionNeeded = jsonObject.getBoolean("req_recepta");
                        String tipusUs = jsonObject.getString("tipus_us");

                        list_medicament.add(new Medicament(medName,nationalCode,useType,typeOfAdministration,prescriptionNeeded,pvp,form,excipients));

                    }
                    //Agregar los elementos del RecyclerView
                    Creacion_elementos_RecyclerView(list_medicament);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Manejo de errores de la solicitud
                error.printStackTrace();
            }
        });

        queue.add(jsonArrayRequest);

        Button filtres = view.findViewById(R.id.bt_Filtres);
        filtres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new FiltreFragment());
            }
        });

        return view;
    }

    // Función para cargar una imagen desde una URL y establecerla en un ImageView
    private void cargarImagenDesdeURL(String imageUrl, ImageView imageView) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(imageUrl);
                    InputStream inputStream = url.openConnection().getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void replaceFragments(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    private void abrirMedicamentoSeleccionado(Medicament medicament) {
        //Crear el diseño del diálogo que contiene la inf del medicameno
        //CrearDialogoMedicamento(medicament);

        // Crear y mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void Creacion_elementos_RecyclerView(ArrayList<Medicament> lista_elementos ){
        //Creación de LayoutManager que se encarga de la disposición de los elementos del RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerMedicaments.setLayoutManager(layoutManager);

        // Creación del adapter con la nueva lista de elementos búscados
        MedicamentAdapter adapter = new MedicamentAdapter(lista_elementos,getActivity());

        recyclerMedicaments.setAdapter(adapter);

    }




}