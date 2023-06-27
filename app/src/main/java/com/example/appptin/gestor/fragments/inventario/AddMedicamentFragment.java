package com.example.appptin.gestor.fragments.inventario;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMedicamentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedicamentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ImageView iv_regresar;


    public AddMedicamentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMedicamentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMedicamentFragment newInstance(String param1, String param2) {
        AddMedicamentFragment fragment = new AddMedicamentFragment();
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
        view = inflater.inflate(R.layout.fragment_add_medicament, container, false);
        iv_regresar = view.findViewById(R.id.iv_dato_paciente_back);
        iv_regresar.setOnClickListener(regresar);
        EditText etNomMed = view.findViewById(R.id.et_nom_med);
        EditText etCodiNacional = view.findViewById(R.id.et_codi_nacional);
        EditText etUseType = view.findViewById(R.id.et_use_type);
        EditText etTypeOfAdm = view.findViewById(R.id.et_type_of_adm);
        EditText etForm = view.findViewById(R.id.et_form);
        EditText etPresNeed = view.findViewById(R.id.et_pres_need);
        EditText etContents = view.findViewById(R.id.et_contents);
        EditText etExcipients = view.findViewById(R.id.et_excipients);
        EditText etMedUrl = view.findViewById(R.id.et_med_url);
        EditText etPvp = view.findViewById(R.id.et_pvp);
        EditText etQuant = view.findViewById(R.id.et_quant);
        Button btnAdd = view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicine(etNomMed.getText().toString(), etCodiNacional.getText().toString(),
                        etUseType.getText().toString(), etTypeOfAdm.getText().toString(),
                        etForm.getText().toString(), etPresNeed.getText().toString(),
                        etContents.getText().toString(), etExcipients.getText().toString(),
                        etMedUrl.getText().toString(), etPvp.getText().toString(), etQuant.getText().toString());
            }
        });
        return view;
    }
    private View.OnClickListener regresar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }
        }
    };

    private void addMedicine(String medName, String medicineIdentifier, String useType,
                             String typeOfAdm, String form, String presNeed, String contents,
                             String excipient, String medUrl, String pvp, String quant) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Resources r = getResources();
        String apiUrl = r.getString(R.string.api_base_url);
        String url = apiUrl + "/api/add_medicine"; // Reemplaça amb l'adreça completa de l'API per obtenir els medicaments disponibles
        JSONObject postParams = new JSONObject();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
            String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
            //String session_token = login.getSession_token();
            System.out.println(session_token);
            postParams.put("session_token", session_token);
            postParams.put("national_code", medicineIdentifier);
            postParams.put("medicine_image_url", medUrl);
            postParams.put("med_name", medName);
            postParams.put("excipients", excipient);
            postParams.put("pvp", pvp);
            postParams.put("use_type", useType);
            postParams.put("contents", contents);
            postParams.put("prescription_needed", presNeed);
            postParams.put("form", form);
            postParams.put("type_of_administration", typeOfAdm);
            postParams.put("quantity_available", Integer.parseInt(quant));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Imprime en la consola el mensaje que se envía a la API
        Log.d("AddMedicine", "Request: " + postParams.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, postParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        try {
                            if (response.getString("result").equals("ok")) {
                                // Medicina agregada correctamente
                                Toast.makeText(getActivity(), "Medicina añadida exitosamente", Toast.LENGTH_SHORT).show();
                            } else {
                                // Hubo un error al agregar la medicina
                                Toast.makeText(getActivity(), "Error al añadir medicina", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Manejar error
                        error.printStackTrace();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorStr = new String(error.networkResponse.data);
                            Log.e("API Error Response", errorStr);
                        }
                    }
                });

        queue.add(jsonObjectRequest);
    }

}