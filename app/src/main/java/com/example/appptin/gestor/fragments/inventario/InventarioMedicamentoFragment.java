package com.example.appptin.gestor.fragments.inventario;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appptin.R;
import com.google.android.gms.common.util.JsonUtils;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_inventario_medicamento, container, false);

        TextView med_nom = view.findViewById(R.id.txt_inv_medic_nombre);
        TextView med_cn = view.findViewById(R.id.txt_codi_nacional);
        TextView med_tipus = view.findViewById(R.id.txt_tipus_medicament);
        TextView med_administration = view.findViewById(R.id.txt_inv_administracio);
        TextView med_form = view.findViewById(R.id.txt_inv_form);
        TextView med_pvp = view.findViewById(R.id.txt_inv_pvp);
        TextView med_recipe = view.findViewById(R.id.txt_inv_recipe);

        iv_regresar = view.findViewById(R.id.iv_inventario_medicamento_back);
        // LISTENERS
        iv_regresar.setOnClickListener(regresar);
        med_nom.setText(med.getNombre_medicamento());
        med_cn.setText(med.getCodiNacional_medicamento());
        System.out.println("tipus: " + med.getUseType_medicamento());
        med_tipus.setText(med.getUseType_medicamento().replace("€", ""));
        med_administration.setText(med.getAdministration_medicamento());
        med_form.setText(med.getForm_medicamento());
        med_pvp.setText(med.getPvP_medicamento());
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
}