package com.example.appptin.gestor.fragments.inventario

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
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
import com.google.android.gms.maps.model.CameraPosition
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class MapaGestor : AppCompatActivity() {
    var mapView : MapView? = null

    var annotationApi : AnnotationPlugin? = null
    lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation" //Hard-coded

    var pointAnnotationManager : PointAnnotationManager? = null

    //Array per mostrar diferentes anotacions
    var markerList :  ArrayList<PointAnnotationOptions> = ArrayList()

    //Llistes de coordenades
    var carLatitudeList : ArrayList<Double> = ArrayList()
    var carLongitudeList : ArrayList<Double> = ArrayList()

    var droneLatitudeList : ArrayList<Double> = ArrayList()
    var droneLongitudeList : ArrayList<Double> = ArrayList()

    var beehiveLatitudeList: ArrayList<Double> = ArrayList()
    var beehiveLongitudeList: ArrayList<Double> = ArrayList()

    //Llista de edges
    var url_edges: ArrayList<JSONObject> = ArrayList()

    //Lista para el spinner
    var spinner_edges: ArrayList<String> = ArrayList()

    var selectedEdge: String? = null

    var cameraActual = Point.fromLngLat(1.727446, 41.2151504)
    var zoomActual : Double? = 7.0

    //Variables per assegurarnos que tant el mapa com la resposta de la api estan llestos.
    private var isMapViewReady = false
    private var isCarsApiResponseReady = false
    private var isDroneApiResponseReady = false
    private var isBeehivesApiResponseReady = false

    //Temporitzador per actualitzar la posició dels vehicles cada x temps
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_gestor)

        println("onCreate called") // Opcional: Imprimo un mensaje en la consola

        mapView = findViewById(R.id.mapView)

        val spinner = findViewById<Spinner>(R.id.mapSpinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedEdge = spinner.getItemAtPosition(position).toString()

                if(selectedEdge != resources.getString(R.string.api_base_url)){

                    //Netejem les coordenades dels cotxes per nomes mostrar els drones
                    isCarsApiResponseReady = false
                    carLatitudeList.clear()
                    carLongitudeList.clear()

                    //Netejem les coordenades dels drones per nomes mostrar els cotxes
                    isDroneApiResponseReady = false
                    droneLatitudeList.clear()
                    droneLongitudeList.clear()

                    println("Llistes buides: " + carLatitudeList.isEmpty() + ", " + carLongitudeList.isEmpty())

                    //Deixem d'actualitzar la posicio dels cotxes
                    stopUpdatingCarsPosition()
                    getDronePosition()
                    startUpdatingDronePosition()

                    val urlObject = getUrlObject(spinner.getItemAtPosition(position).toString())
                    if (urlObject != null) {
                        val latitude = urlObject.getDouble("latitude")
                        val longitude = urlObject.getDouble("longitude")
                        cameraActual = Point.fromLngLat(longitude, latitude)
                        zoomActual = 13.0
                        ZoomCamera()
                    } else {
                        // No se encontró ningún objeto JSON con la misma URL
                    }
                }
                else{
                    //Netejem les coordenades dels drones per nomes mostrar els cotxes
                    isDroneApiResponseReady = false
                    droneLatitudeList.clear()
                    droneLongitudeList.clear()

                    stopUpdatingDronePosition()
                    getCarsPosition()
                    startUpdatingCarsPosition()

                    cameraActual = Point.fromLngLat(1.727446, 41.2151504)
                    zoomActual = 7.0
                    ZoomCamera()
                }
                println("Server actuak: " + selectedEdge)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Este método se llama cuando no se selecciona ninguna opción del Spinner
                // Puedes manejarlo según tus necesidades
            }
        }

        //Funcio de prova per posar marcadors
        //createDummyAList()
        //getCarsPosition()
        getGlobalBeehivesPosition()

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

                        isMapViewReady = true
                        checkIfReadyToDrawMarkers()
                        startUpdatingCarsPosition()
                    }
                }
        )
    }

    private fun ZoomCamera(){
        mapView!!.getMapboxMap().setCamera(
                CameraOptions.Builder().center(cameraActual)
                        .zoom(zoomActual)
                        .build()
        )
    }

    //Llista d'exemple de lat lng
    private fun createDummyAList() {
        carLatitudeList.add(41.224950)
        carLongitudeList.add(1.733128)

        carLatitudeList.add(41.224950)
        carLongitudeList.add(1.743128)

        carLatitudeList.add(41.234950)
        carLongitudeList.add(1.733128)


    }

    //Funcio per afegir marcador
    private fun createMarkerOnMap() {

        // Remueve todos los marcadores
        pointAnnotationManager?.deleteAll()
        markerList.clear()

        var carBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.baseline_directions_car_24))
        for (i in 0 until carLongitudeList.size){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(carLongitudeList.get(i), carLatitudeList.get(i)))
                .withIconImage(carBitmap!!)

            markerList.add(pointAnnotationOptions)
        }

        var droneBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.baseline_airplanemode_active_24))
        for (i in 0 until droneLongitudeList.size){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(droneLongitudeList.get(i), droneLatitudeList.get(i)))
                .withIconImage(droneBitmap!!)

            markerList.add(pointAnnotationOptions)
        }

        var beehiveBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.beehive_icon))
        for (i in 0 until beehiveLongitudeList.size){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(beehiveLongitudeList.get(i), beehiveLatitudeList.get(i)))
                .withIconImage(beehiveBitmap!!)

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

    private fun checkIfReadyToDrawMarkers() {
        if (isMapViewReady && (isCarsApiResponseReady || isDroneApiResponseReady) && isBeehivesApiResponseReady) {

            //Crida a la funcio que dibuixa els marcadors
            createMarkerOnMap()
        }
    }

    private fun getCarsPosition() {
        val queue = Volley.newRequestQueue(this)
        val url = resources.getString(R.string.api_base_url) + "/api/cars_full_info"
        val jsonObject = JSONObject()

        val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val sessionToken = sharedPref.getString("session_token", "Valor nulo")  // SI ESTO NO FUNCIONA, UTILIZA LA LÍNEA DE ABAJO
        //val sessionToken = login.sessionToken
        println(sessionToken)

        jsonObject.put("session_token", sessionToken)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                // Maneja la respuesta de la API
                println("Resposta: " + response)
                val carsArray = response.getJSONArray("cars")
                println("Numero de cotxes: " + carsArray.length())
                for (i in 0 until carsArray.length()) {
                    val carObject = carsArray.getJSONObject(i)
                    val locationAct = carObject.getJSONObject("location_act")
                    val latitude = locationAct.getDouble("latitude")
                    val longitude = locationAct.getDouble("longitude")
                    println("Coordenades del cotxe " + carObject.getInt("id_car") + ": "
                    + latitude + ", " + longitude)

                    carLatitudeList.add(latitude)
                    carLongitudeList.add(longitude)
                }

                // Después de añadir las coordenadas, crea los marcadores en el mapa
                isCarsApiResponseReady = true
                checkIfReadyToDrawMarkers()
            },
            { error ->
                // Maneja el error de la solicitud
                println(error)
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun getDronePosition() {
        val queue = Volley.newRequestQueue(this)
        val url = selectedEdge + "/api/drones_full_info" // Reemplaza con la URL de tu API
        val jsonObject = JSONObject()

        val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val sessionToken = sharedPref.getString("session_token", "Valor nulo")  // SI ESTO NO FUNCIONA, UTILIZA LA LÍNEA DE ABAJO
        //val sessionToken = login.sessionToken
        println(sessionToken)

        jsonObject.put("session_token", sessionToken)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                if (response.get("result") == "ok"){
                    // Maneja la respuesta de la API
                    println(response)
                    val droneArray = response.getJSONArray("drones")
                    println("Numero de drones: " + droneArray.length())
                    for (i in 0 until droneArray.length()) {
                        val droneObject = droneArray.getJSONObject(i)
                        val locationAct = droneObject.getJSONObject("location_act")
                        val latitude = locationAct.getDouble("latitude")
                        val longitude = locationAct.getDouble("longitude")
                        println("Coordenades del drone " + droneObject.getInt("id_dron") + ": "
                                + latitude + ", " + longitude)

                        droneLatitudeList.add(latitude)
                        droneLongitudeList.add(longitude)
                    }

                    // Después de añadir las coordenadas, crea los marcadores en el mapa
                    isDroneApiResponseReady = true
                    checkIfReadyToDrawMarkers()
                }
            },
            { error ->
                // Maneja el error de la solicitud
                println(error)
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun getGlobalBeehivesPosition() {
        val queue = Volley.newRequestQueue(this)
        val base_url = resources.getString(R.string.api_base_url)
        val url = base_url + "/api/beehives_global"

        println("Inici consulta: " + url)

        val edgeObject = JSONObject()
        edgeObject.put("url", base_url)
        edgeObject.put("latitude", 41.8375)
        edgeObject.put("longitude", 1.5377777777778)
        url_edges.add(edgeObject)

        spinner_edges.add(base_url)

        val jsonObject = JSONObject()

        val sharedPref = getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        val sessionToken = sharedPref.getString("session_token", "Valor nulo")
        println("Session token getBeehives: " + sessionToken)
        jsonObject.put("session_token", "internal")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            { response ->
                // Maneja la respuesta de la API
                println("getBeehives: " + response)
                val beehivesArray = response.getJSONArray("beehives")

                for (i in 0 until beehivesArray.length()) {
                    val beehiveObject = beehivesArray.getJSONObject(i)
                    val latitude = beehiveObject.getDouble("latitude")
                    val longitude = beehiveObject.getDouble("longitude")
                    val url_beehive = beehiveObject.getString("url_beehive")

                    println("Url beehive: " + url_beehive)

                    beehiveLatitudeList.add(latitude)
                    beehiveLongitudeList.add(longitude)

                    val edgeObject = JSONObject()
                    edgeObject.put("url", url_beehive)
                    edgeObject.put("latitude", latitude)
                    edgeObject.put("longitude", longitude)
                    url_edges.add(edgeObject)

                    if(!spinner_edges.contains(url_beehive)){
                        spinner_edges.add(url_beehive)
                    }

                }

                val spinner = findViewById<Spinner>(R.id.mapSpinner)

                // Crear un adaptador personalizat
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_edges)

                // Especificar l'estil del adaptador
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Asignar l'adaptador al Spinner
                spinner.adapter = adapter

                // Después de añadir las coordenadas, crea los marcadores de colmenas en el mapa
                isBeehivesApiResponseReady = true
                checkIfReadyToDrawMarkers()
            },
            { error ->
                // Maneja el error de la solicitud
                println(error)
            }
        )

        queue.add(jsonObjectRequest)
    }

    private fun startUpdatingCarsPosition() {
        // Cancela el temporizador si ya está en ejecución
        stopUpdatingCarsPosition()

        // Crea un nuevo temporizador
        timer = Timer()

        // Programa una tarea para actualizar la posición de los coches cada 20 segundos
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // Lógica para actualizar la posición de los coches
                    getCarsPosition()
                }
            }
        }, 0, 20000) // Actualiza cada 20 segundos (20,000 milisegundos)
    }

    private fun stopUpdatingCarsPosition() {
        timer?.cancel()
        timer = null
    }

    private fun startUpdatingDronePosition() {
        // Cancela el temporizador si ya está en ejecución
        stopUpdatingDronePosition()

        // Crea un nuevo temporizador
        timer = Timer()

        // Programa una tarea para actualizar la posición de los coches cada 20 segundos
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // Lógica para actualizar la posición de los coches
                    getDronePosition()
                }
            }
        }, 0, 20000) // Actualiza cada 20 segundos (20,000 milisegundos)
    }

    private fun stopUpdatingDronePosition() {
        timer?.cancel()
        timer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopUpdatingCarsPosition()
        stopUpdatingDronePosition()
    }

    fun getUrlObject(url: String): JSONObject? {
        for (edgeObject in url_edges) {
            val edgeUrl = edgeObject.optString("url", "")
            if (edgeUrl == url) {
                return edgeObject
            }
        }
        return null
    }

}