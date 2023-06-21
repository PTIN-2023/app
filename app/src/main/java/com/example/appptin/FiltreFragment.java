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
    private String[] via;

    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private EditText editTextMedName;
    private CheckBox checkBoxPrescription;
    private CheckBox checkBoxOral;

    private CheckBox checkBoxTabletas;
    private CheckBox checkBoxLiquidos;
    private CheckBox checkBoxCapsulas;
    private CheckBox checkBoxTopicos;

    private CheckBox checkBoxGel;
    private CheckBox checkBoxPols;
    private CheckBox checkBoxCrema;


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
        ArrayList<String> via = new ArrayList<>();
        ArrayList<String> format = new ArrayList<>();

        // nom medicament
        //editTextMedName = view.findViewById(R.id.nom_Med);

        // max i min preu
        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);

        // necessitat prescripció
        checkBoxPrescription = view.findViewById(R.id.checkBoxPrescription);

        // medicaments via
        checkBoxOral = view.findViewById(R.id.checkBoxOral);
        checkBoxTopicos = view.findViewById(R.id.checkBoxTopicos);


        // medicaments format
        checkBoxTabletas = view.findViewById(R.id.checkBoxTabletas);
        checkBoxLiquidos = view.findViewById(R.id.checkBoxLiquidos);
        checkBoxCapsulas = view.findViewById(R.id.checkBoxCapsulas);
        checkBoxGel = view.findViewById(R.id.checkBoxGel);
        checkBoxCrema = view.findViewById(R.id.checkBoxCrema);
        checkBoxPols = view.findViewById(R.id.checkBoxPols);


        // Initialize references to other checkboxes

        Button buttonApply = view.findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenim Nom medicament
                //String medName = editTextMedName.getText().toString();

                // Obtenim min i max price
                String minPrice = editTextMinPrice.getText().toString();
                String maxPrice = editTextMaxPrice.getText().toString();

                // obtenim necessitat prescripcio
                boolean prescriptionNeeded = checkBoxPrescription.isChecked();

                // obtenim via - type of administration
                boolean oralSelected = checkBoxOral.isChecked();
                if (oralSelected)
                    via.add ("Oral");

                boolean TopicosSelected = checkBoxTopicos.isChecked();
                if (TopicosSelected)
                    via.add("Topical");

                // obtenim format - type of administration
                boolean TabletasSelected = checkBoxTabletas.isChecked();
                if (TabletasSelected)
                    format.add("Tablets");
                boolean LiquidSelected = checkBoxLiquidos.isChecked();
                if (LiquidSelected)
                    format.add("Liquid");
                boolean CapsulasSelected = checkBoxCapsulas.isChecked();
                if (CapsulasSelected)
                    format.add("Capsules");
                boolean GelSelected = checkBoxGel.isChecked();
                if (GelSelected)
                    format.add("Gel");
                boolean CremaSelected = checkBoxCrema.isChecked();
                if (CremaSelected)
                    format.add("Cream");
                boolean PolsSelected = checkBoxPols.isChecked();
                if (PolsSelected)
                    format.add("Powder");


                // Create a bundle and set the selected values as arguments
                Bundle bundle = new Bundle();
                //bundle.putString("medName", medName);
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