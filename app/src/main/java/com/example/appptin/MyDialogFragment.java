package com.example.appptin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyDialogFragment extends DialogFragment {

    private String[] items;

    public static MyDialogFragment newInstance(String[] items) {
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray("items", items);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getStringArray("items");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_dialog, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        for (String item : items) {
            View itemView = inflater.inflate(R.layout.item_my_dialog, null);
            TextView textView = itemView.findViewById(R.id.textView);
            textView.setText(item);
            Button button = itemView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Aquí puedes agregar el código para agregar el elemento a una lista
                }
            });
            linearLayout.addView(itemView);
        }
        return view;
    }
}
