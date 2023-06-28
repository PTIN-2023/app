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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appptin.R;
import com.google.android.gms.common.util.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class InventarioMedicamentoFragment extends Fragment {

    private ImageView iv_regresar;
    private View view;

    MedicamentosClass med;


    public InventarioMedicamentoFragment(MedicamentosClass med) {
        // Required empty public constructor
        this.med = med;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {

        // Crida a la funció per obtenir les quantitats dels medicaments i els preus de venda al públic
        TextView med_nom = view.findViewById(R.id.txt_inv_medic_nombre);
        TextView med_cn = view.findViewById(R.id.txt_codi_nacional);
        TextView med_tipus = view.findViewById(R.id.txt_tipus_medicament);
        TextView med_administration = view.findViewById(R.id.txt_inv_administracio);
        TextView med_form = view.findViewById(R.id.txt_inv_form);

        EditText med_pvp = view.findViewById(R.id.txt_inv_pvp);

        EditText med_quant = view.findViewById(R.id.txt_inv_quant);

        TextView med_recipe = view.findViewById(R.id.txt_inv_recipe);
        Button button = view.findViewById(R.id.button_delete);
        Button button_act = view.findViewById(R.id.button_actualitzar);
        iv_regresar = view.findViewById(R.id.iv_inventario_medicamento_back);
        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        button.setOnClickListener(eliminar);
        button_act.setOnClickListener(actualitzar);

        med_nom.setText(med.getNombre_medicamento());
        med_cn.setText(med.getCodiNacional_medicamento());
        System.out.println("tipus: " + med.getUseType_medicamento());
        med_tipus.setText(med.getUseType_medicamento());
        med_administration.setText(med.getAdministration_medicamento());
        med_form.setText(med.getForm_medicamento());
        med_pvp.setText(med.getPvP_medicamento());
        med_quant.setText(String.valueOf(med.getQuantitat_medicamento()));
        med_recipe.setText(med.getPrescription_medicamento());

        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_inventario_medicamento, container, false);

        TextView med_nom = view.findViewById(R.id.txt_inv_medic_nombre);
        TextView med_cn = view.findViewById(R.id.txt_codi_nacional);
        TextView med_tipus = view.findViewById(R.id.txt_tipus_medicament);
        TextView med_administration = view.findViewById(R.id.txt_inv_administracio);
        TextView med_form = view.findViewById(R.id.txt_inv_form);

        EditText med_pvp = view.findViewById(R.id.txt_inv_pvp);

        EditText med_quant = view.findViewById(R.id.txt_inv_quant);

        TextView med_recipe = view.findViewById(R.id.txt_inv_recipe);
        Button button = view.findViewById(R.id.button_delete);
        Button button_act = view.findViewById(R.id.button_actualitzar);
        iv_regresar = view.findViewById(R.id.iv_inventario_medicamento_back);
        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        button.setOnClickListener(eliminar);
        button_act.setOnClickListener(actualitzar);

        med_nom.setText(med.getNombre_medicamento());
        med_cn.setText(med.getCodiNacional_medicamento());
        System.out.println("tipus: " + med.getUseType_medicamento());
        med_tipus.setText(med.getUseType_medicamento());
        med_administration.setText(med.getAdministration_medicamento());
        med_form.setText(med.getForm_medicamento());
        med_pvp.setText(med.getPvP_medicamento());
        med_quant.setText(String.valueOf(med.getQuantitat_medicamento()));
        med_recipe.setText(med.getPrescription_medicamento());

        return view;
    }

    private View.OnClickListener regresar = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            //System.out.println("back button");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Si estás en un Fragment, utiliza getFragmentManager()
            if (fragmentManager.getBackStackEntryCount() > 0) {
                // Retrocede en la pila de fragmentos
                fragmentManager.popBackStack();
            }
        }
    };

    private View.OnClickListener eliminar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Resources r = getResources();
            String apiUrl = r.getString(R.string.api_base_url);
            String url = apiUrl + "/api/delete_medicine";
            JSONObject postParams = new JSONObject();
            try {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
                //String session_token = login.getSession_token();
                System.out.println(session_token);
                postParams.put("session_token", session_token);
                postParams.put("medicine_identifier", med.getCodiNacional_medicamento());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, postParams, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("result").equals("ok")) {
                                    // Medicamento eliminado correctamente
                                    Toast.makeText(getActivity(), "Medicina eliminada exitosamente", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hubo un error al eliminar la medicina
                                    Toast.makeText(getActivity(), "Error al eliminar medicina", Toast.LENGTH_SHORT).show();
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
                        }
                    });

            queue.add(jsonObjectRequest);
        }
    };

    private View.OnClickListener actualitzar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            Resources r = getResources();
            String apiUrl = r.getString(R.string.api_base_url);
            String url = apiUrl + "/api/update_medicine";
            JSONObject postParams = new JSONObject();
            try {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                String session_token = sharedPreferences.getString("session_token", "Valor nulo");  //SI AIXÒ NO FUNCIONA, FER-HO COM LA LINIA DE BAIX
                //String session_token = login.getSession_token();
                System.out.println(session_token);
                EditText med_quant = view.findViewById(R.id.txt_inv_quant);
                EditText med_pvp = view.findViewById(R.id.txt_inv_pvp);
                postParams.put("session_token", session_token);
                postParams.put("national_code", med.getCodiNacional_medicamento());
                postParams.put("quantity_available",String.valueOf(med_quant.getText()));
                postParams.put("pvp",Double.valueOf(String.valueOf(med_pvp.getText())));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, postParams, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("result").equals("ok")) {
                                    // Medicamento eliminado correctamente
                                    Toast.makeText(getActivity(), "Medicina actualitzada correctament", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Hubo un error al eliminar la medicina
                                    Toast.makeText(getActivity(), "Error al actualitzar medicament", Toast.LENGTH_SHORT).show();
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
                        }
                    });

            queue.add(jsonObjectRequest);
        }
    };


}