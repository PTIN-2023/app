package com.example.appptin.gestor.fragments.inventario;

import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appptin.R;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

public class MapaGestor extends AppCompatActivity {

    MapboxMap mapboxMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYXJuYXUxMjMxMiIsImEiOiJjbGhuaWdoaG4xbWVxM21waHlyZ2YwcTV4In0.RvRXUU9nWEI6nDI10ize7w");
        setContentView(R.layout.maps_gestor);

        MapView mapView = findViewById(R.id.mapView);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        style.addImage("plane-icon-id", BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.baseline_airplanemode_active_24)));
                        style.addLayer(new SymbolLayer("icon-layer-id", "icon-source-id").withProperties(
                                PropertyFactory.iconImage("plane-icon-id"),
                                PropertyFactory.iconIgnorePlacement(true),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconOffset(new Float[]{0f, -9f})
                        ));
                        GeoJsonSource iconGeoJsonSource = new GeoJsonSource("icon-source-id", Feature.fromGeometry(Point.fromLngLat(1.7255, 41.2249)));
                        style.addSource(iconGeoJsonSource);
                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1.7255, 41.2249), 15.7));

                    }
                });
            }
        });

    }
}