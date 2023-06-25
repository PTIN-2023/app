package com.example.appptin.gestor.fragments.inventario

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.appptin.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class MapaGestor : AppCompatActivity() {
    var mapView : MapView? = null

    var annotationApi : AnnotationPlugin? = null
    lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation" //Hard-coded

    var pointAnnotationManager : PointAnnotationManager? = null

    //Array per mostrar diferentes anotacions
    var markerList :  ArrayList<PointAnnotationOptions> = ArrayList()

    //Per provar
    var latitudeList : ArrayList<Double> = ArrayList()
    var longitudeList : ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_gestor)

        mapView = findViewById(R.id.mapView)

        createDummyAList()

        mapView?.getMapboxMap()?.loadStyleUri(
                Style.MAPBOX_STREETS,
                object : Style.OnStyleLoaded{
                    override fun onStyleLoaded(style: Style) {
                        ZoomCamera()

                        annotationApi = mapView?.annotations
                        annotationConfig = AnnotationConfig(
                            layerId = layerIDD
                        )

                        //Inicialitzem el point annotation manager
                        pointAnnotationManager = annotationApi?.createPointAnnotationManager(annotationConfig)
                        createMarkerOnMap()
                    }
                }
        )
    }

    private fun ZoomCamera(){
        mapView!!.getMapboxMap().setCamera(
                CameraOptions.Builder().center(Point.fromLngLat(1.727446, 41.2151504))
                        .zoom(11.0)
                        .build()
        )
    }

    //Llista d'exemple de lat lng
    private fun createDummyAList() {
        latitudeList.add(41.224950)
        longitudeList.add(1.733128)

        latitudeList.add(41.224950)
        longitudeList.add(1.743128)

        latitudeList.add(41.234950)
        longitudeList.add(1.733128)


    }

    //Funcio per afegir marcador
    private fun createMarkerOnMap() {


        var bitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.baseline_airplanemode_active_24))
        for (i in 0 until 3){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitudeList.get(i), latitudeList.get(i)))
                .withIconImage(bitmap!!)

            markerList.add(pointAnnotationOptions)
        }
        pointAnnotationManager?.create(markerList)

    }

    //Funcio per convertir de Drawable a Bitmap
    private fun convertDrawableToBitMap(sourceDrawable: Drawable?):Bitmap {

        return if(sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        }else {
            val constatState = sourceDrawable?.constantState
            val drawable = constatState?.newDrawable()?.mutate()
            val bitmap : Bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,drawable!!.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }

    }

    private fun getDronePositions() {
        val queue = Volley.newRequestQueue(this)
        val url = resources.getString(R.string.api_base_url) + "/drones_pos_info" // Reemplaza con la URL de tu API

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Maneja la respuesta de la API

            },
            { error ->
                // Maneja el error de la solicitud
            }
        )

        queue.add(jsonObjectRequest)
    }
}