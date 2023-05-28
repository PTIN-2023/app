package com.example.appptin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.ArrayList;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> via;
    private ArrayList<String> format;
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private EditText editTextMedName;
    private CheckBox checkBoxPrescription;
    private CheckBox checkBoxOral;
    private CheckBox checkBoxInyectable;
    private CheckBox checkBoxSublingual;
    private CheckBox checkBoxVaginal;
    private CheckBox checkBoxOcular;
    private CheckBox checkBoxTabletas;
    private CheckBox checkBoxLiquidos;
    private CheckBox checkBoxCapsulas;
    private CheckBox checkBoxTopicos;
    private CheckBox checkBoxSupositorios;
    private CheckBox checkBoxGotas;
    private CheckBox checkBoxInhaladores;
    private CheckBox checkBoxInyecciones;
    private CheckBox checkBoxImplantes;


    public FiltreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FiltreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltreFragment newInstance(String param1, String param2) {
        FiltreFragment fragment = new FiltreFragment();
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
        View view = inflater.inflate(R.layout.fragment_filtre, container, false);
        Button closeButton = view.findViewById(R.id.bt_X);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Test"); // Cierra la actividad actual
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new MedicamentsFragment());
                fragmentTransaction.commit();
            }
        });

        // definim 2 arrays
        via = new ArrayList<>();
        format = new ArrayList<>();

        // nom medicament
        editTextMedName = view.findViewById(R.id.nom_Med);

        // max i min preu
        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);

        // necessitat prescripció
        checkBoxPrescription = view.findViewById(R.id.checkBoxPrescription);

        // medicaments via
        checkBoxOral = view.findViewById(R.id.checkBoxOral);
        checkBoxInyectable = view.findViewById(R.id.checkBoxInyectable);
        checkBoxSublingual = view.findViewById(R.id.checkBoxSublingual);
        checkBoxVaginal = view.findViewById(R.id.checkBoxVaginal);
        checkBoxOcular = view.findViewById(R.id.checkBoxOcular);

        // medicaments format
        checkBoxTabletas = view.findViewById(R.id.checkBoxTabletas);
        checkBoxLiquidos = view.findViewById(R.id.checkBoxLiquidos);
        checkBoxCapsulas = view.findViewById(R.id.checkBoxCapsulas);
        checkBoxTopicos = view.findViewById(R.id.checkBoxTopicos);
        checkBoxSupositorios = view.findViewById(R.id.checkBoxSupositorios);
        checkBoxGotas = view.findViewById(R.id.checkBoxGotas);
        checkBoxInhaladores = view.findViewById(R.id.checkBoxInhaladores);
        checkBoxInyecciones = view.findViewById(R.id.checkBoxInyecciones);
        checkBoxImplantes = view.findViewById(R.id.checkBoxImplantes);

        // Initialize references to other checkboxes

        Button buttonApply = view.findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenim Nom medicament
                String medName = editTextMaxPrice.getText().toString();

                // Obtenim min i max price
                String minPrice = editTextMinPrice.getText().toString();
                String maxPrice = editTextMaxPrice.getText().toString();

                // obtenim necessitat prescripcio
                boolean prescriptionNeeded = checkBoxPrescription.isChecked();

                // obtenim via
                boolean oralSelected = checkBoxOral.isChecked();
                if (oralSelected)
                    via.add("oral");
                boolean inyectableSelected = checkBoxInyectable.isChecked();
                if (inyectableSelected)
                    via.add("inyectable");
                boolean SublingualSelected = checkBoxSublingual.isChecked();
                if (SublingualSelected)
                    via.add("Sublingual");
                boolean VaginalSelected = checkBoxVaginal.isChecked();
                if (VaginalSelected)
                    via.add("Vaginal");
                boolean OcularSelected = checkBoxOcular.isChecked();
                if (OcularSelected)
                    via.add("Ocular");

                // obtenim format
                boolean TabletasSelected = checkBoxTabletas.isChecked();
                if (TabletasSelected)
                    format.add("Tabletas");
                boolean LiquidosSelected = checkBoxLiquidos.isChecked();
                if (LiquidosSelected)
                    format.add("Liquidos");
                boolean CapsulasSelected = checkBoxCapsulas.isChecked();
                if (CapsulasSelected)
                    format.add("Capsulas");
                boolean TopicosSelected = checkBoxTopicos.isChecked();
                if (TopicosSelected)
                    format.add("Topicos");
                boolean SupositoriosSelected = checkBoxSupositorios.isChecked();
                if (SupositoriosSelected)
                    format.add("Supositorios");
                boolean GotasSelected = checkBoxGotas.isChecked();
                if (GotasSelected)
                    format.add("Gotas");
                boolean InhaladoresSelected = checkBoxInhaladores.isChecked();
                if (InhaladoresSelected)
                    format.add("Inhaladores");
                boolean InyeccionesSelected = checkBoxInyecciones.isChecked();
                if (InyeccionesSelected)
                    format.add("Inyecciones");
                boolean ImplantesSelected = checkBoxImplantes.isChecked();
                if (ImplantesSelected)
                    format.add("Implantes");



                // TODO: Do something with the selected values (e.g., save to preferences, send to server)

                // For demonstration purposes, display the selected values


                // Create a bundle and set the selected values as arguments
                Bundle bundle = new Bundle();
                bundle.putString("medName", medName);
                bundle.putString("minPrice", minPrice);
                bundle.putString("maxPrice", maxPrice);
                bundle.putBoolean("prescriptionNeeded", prescriptionNeeded);
                bundle.putStringArrayList("via", via);
                bundle.putStringArrayList("format", format);

                // Create an instance of the fragment and set the arguments
                MedicamentsFragment fragment = new MedicamentsFragment();
                fragment.setArguments(bundle);

                // Perform fragment transaction to display the fragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment);
                transaction.commit();

                //Toast.makeText(YourActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}