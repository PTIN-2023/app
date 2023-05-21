package com.example.appptin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    private LinearLayout linearLayoutMedicaments;

    public MedicamentsFragment() {
        // Required empty public constructor
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_medicaments, container, false);
        linearLayoutMedicaments = view.findViewById(R.id.medicaments_layout);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/list_available_medicines"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONObject jsonBody = new JSONObject();

        // Ejemplo de medicamentos de prueba
        List<Medicament> medicaments = new ArrayList();
        medicaments.add(new Medicament("Medicamento 1", "123456789", "Uso 1", "Administración 1", true, 10.0, "Forma 1", "Excipiente 1"));
        medicaments.add(new Medicament("Medicamento 2", "987654321", "Uso 2", "Administración 2", false, 15.0, "Forma 2", "Excipiente 2"));
        medicaments.add(new Medicament("Medicamento 3", "567890123", "Uso 3", "Administración 3", true, 20.0, "Forma 3", "Excipiente 3"));
        medicaments.add(new Medicament("Medicamento 4", "321098765", "Uso 4", "Administración 4", false, 25.0, "Forma 4", "Excipiente 4"));

        // Llamar a la función para agregar las vistas de los medicamentos
        agregarVistasMedicamentos(medicaments);

        try {
            jsonBody.put("session_token", login.getSession_token());
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String result = response.getString("result");
                    if (result.equals("ok")) {
                        JSONArray medicineList = response.getJSONArray("medicine_list");
                        List<Medicament> medicaments = new ArrayList();
                        for (int i = 0; i < medicineList.length(); i++) {

                            JSONObject medicamentObj = medicineList.getJSONObject(i);
                           // String identifier = medicamentObj.getString("medicine_identifier");
                           // String imageUrl = medicamentObj.getString("medicine_image_url");

                            String name = medicamentObj.getString("med_name");
                            String nationalCode = medicamentObj.getString("nationalcode");
                            String excipient = medicamentObj.getString("excipient");
                            double pvp = medicamentObj.getDouble("preu");
                            boolean prescriptionNeeded = medicamentObj.getBoolean("req_recepta");
                            String form = medicamentObj.getString("presentacio");
                            String typeOfAdministration = medicamentObj.getString("administracio");
                            String useType = medicamentObj.getString("tipus_us");

                            medicaments.add(new Medicament(name, nationalCode, useType, typeOfAdministration,
                                    prescriptionNeeded, pvp, form, excipient));
                        }



                        // Aquí tens la llista de medicaments, pots fer amb ella el que necessitis
                    } else {
                        // En cas que el resultat sigui diferent de "ok"
                        // Maneig de l'error o notificació a l'usuari
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En cas d'error en la crida a l'API
                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
        Button filtres = view.findViewById(R.id.bt_Filtres);

        filtres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new FiltreFragment());
            }
        });


        return view;
    }

    // Función para agregar las vistas de los medicamentos
    private void agregarVistasMedicamentos(List<Medicament> medicaments) {
        //LinearLayout scroll_medicaments = getView().findViewById(R.id.scroll_medicaments);

        for (Medicament medicament : medicaments) {
            // Crear el LinearLayout para cada medicamento
            LinearLayout medicamentoLayout = new LinearLayout(getActivity());
            medicamentoLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            medicamentoLayout.setOrientation(LinearLayout.VERTICAL);

            // Crear el ImageView y cargar la imagen desde la URL
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    225, // Ancho deseado en píxeles
                    225  // Alto deseado en píxeles
            ));
            // Establecer la imagen de muestra desde el recurso drawable
            imageView.setImageResource(R.drawable.avatar_gestor);
            //cargarImagenDesdeURL(medicament.getImageUrl(), imageView);

            // Crear el TextView y configurar el nombre del medicamento
            TextView nombreTextView = new TextView(getActivity());
            nombreTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            nombreTextView.setText("prova");

            // Agregar el ImageView y el TextView al LinearLayout del medicamento
            medicamentoLayout.addView(imageView);
            medicamentoLayout.addView(nombreTextView);

            // Agregar el LinearLayout del medicamento al contenedor principal
            linearLayoutMedicaments.addView(medicamentoLayout);
        }
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
}