package com.example.appptin.gestor.fragments.inventario;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appptin.R;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

public class MapaGestor_bkp extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_gestor);
        mapView = findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
    }
}
