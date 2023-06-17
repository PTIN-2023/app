package com.example.appptin.gestor.fragments.pefilgestor.opciones;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appptin.Peticio;
import com.example.appptin.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstatPeticionsGestorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstatPeticionsGestorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerPeticions;

    public EstatPeticionsGestorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstatPeticionsGestorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstatPeticionsGestorFragment newInstance(String param1, String param2) {
        EstatPeticionsGestorFragment fragment = new EstatPeticionsGestorFragment();
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
        View view = inflater.inflate(R.layout.fragment_estat_peticions_gestor, container, false);
        recyclerPeticions = view.findViewById(R.id.peticions_recycler_g);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPeticions.setLayoutManager(layoutManager);

        ArrayList<Peticio> list_peticions = new ArrayList<>();
        PeticioGestorAdapter adapter = new PeticioGestorAdapter(list_peticions, getActivity());
        recyclerPeticions.setAdapter(adapter);

        return view;
    }
}