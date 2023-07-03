package com.example.appptin.gestor.fragments.inventario

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
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

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.model.LatLng
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow


class MapaGestorEdge : AppCompatActivity() {
    lateinit var arguments: Bundle
    var mapView : MapView? = null

    var annotationApi : AnnotationPlugin? = null
    lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation" //Hard-coded

    //val edgeNumber = intent?.getIntExtra("parametreInt", 0)


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

    // Arrays per emmagatzemar les posicions
    private val carPositions: MutableList<CarData> = mutableListOf()
    private val dronePositions: MutableList<DronData> = mutableListOf()
    private val beehivePositions: MutableList<BeehiveData> = mutableListOf()

    private var popupDisplayedCotxe = false
    private var popupDisplayedDron = false

    //private var carBitmap: Bitmap? = null
    //private var droneBitmap: Bitmap? = null

    data class CarData(
        val id: Int,
        val matricula: String,
        val statusText: String,
        val bateria: String,
        val ultim_manteniment: String,
        val paquets: Array<String>,
        val puntInici: Point,
        val puntDesti: Point,
        val position: Point,

        )

    data class DronData(
        val id: Int,
        val statusText: String,
        val bateria: String,
        val ultim_manteniment: String,
        val autonomia: String,
        val id_order: String,
        val beehive: Int,
        val puntInici: Point,
        val puntDesti: Point,
        val position: Point,

        )

    data class BeehiveData(
        val id: Int,
        val position: Point,

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps_gestor)

        println("onCreate called") // Opcional: Imprimo un mensaje en la consola

        mapView = findViewById(R.id.mapView)

        val spinner = findViewById<Spinner>(R.id.mapSpinner)
        spinner.visibility = View.INVISIBLE;

        val edgeNumber = intent.getIntExtra("parametreInt", 0)
        println("Valor de edge: $edgeNumber")




        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedEdge = spinner.getItemAtPosition(edgeNumber).toString()
                println("selected edge:" + selectedEdge)
                if(edgeNumber==1){              //AIXÒ ES POT MILLORAR
                    selectedEdge = resources.getString(R.string.api_edge1_url)
                }
                else if(edgeNumber==0){
                    selectedEdge = resources.getString(R.string.api_edge0_url)
                }

                else if(edgeNumber==2){
                    selectedEdge = resources.getString(R.string.api_edge2_url)
                }

                if(selectedEdge != resources.getString(R.string.api_base_url)){

/*                    //Netejem les coordenades dels cotxes per nomes mostrar els drones
                    isCarsApiResponseReady = false
                    carLatitudeList.clear()
                    carLongitudeList.clear()

                    //Netejem les coordenades dels drones per nomes mostrar els cotxes
                    isDroneApiResponseReady = false
                    droneLatitudeList.clear()
                    droneLongitudeList.clear()*/

                    println("Llistes buides: " + carLatitudeList.isEmpty() + ", " + carLongitudeList.isEmpty())

                    //Deixem d'actualitzar la posicio dels cotxes
                    //stopUpdatingCarsPosition()
                    getDronePosition()
                    startUpdatingDronePosition()
                    getCarsPosition()
                    startUpdatingCarsPosition()


                    setMapLocation(edgeNumber)
                    
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
                    println("edge " + edgeNumber)
                    isDroneApiResponseReady = false
                    droneLatitudeList.clear()
                    droneLongitudeList.clear()

                    stopUpdatingDronePosition()
                    getCarsPosition()
                    startUpdatingCarsPosition()

                    //cameraActual = Point.fromLngLat(1.727446, 41.2151504)
                    //zoomActual = 7.0

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

    private fun setMapLocation(edgeNumber: Int) {
        when (edgeNumber) {
            1 -> { //BARCELONA

                cameraActual = Point.fromLngLat(2.171944, 41.399858)
                zoomActual = 13.0
                println("1 1")
                ZoomCamera()
            }
            0 -> {  //VILANOVA I LA GELTRÚ 41.221858, 1.726042
                cameraActual = Point.fromLngLat(1.726042, 41.221858) // Ubicación específica para edge 0
                zoomActual = 15.0
                ZoomCamera()
            }
            2 -> { //CUBELLES 41.206857, 1.675534
                cameraActual = Point.fromLngLat(1.675534, 41.206857) // Ubicación específica para edge 2
                zoomActual = 16.0
            }

        }

        ZoomCamera()
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

    private fun clearAnnotation(){
        //Netejem les coordenades dels cotxes per nomes mostrar els drones
        isCarsApiResponseReady = false
        carLatitudeList.clear()
        carLongitudeList.clear()

        //Netejem les coordenades dels drones per nomes mostrar els cotxes
        isDroneApiResponseReady = false
        droneLatitudeList.clear()
        droneLongitudeList.clear()

        markerList = ArrayList()
        pointAnnotationManager?.deleteAll()
    }

    //Funcio per afegir marcador
    private fun createMarkerOnMap() {

        // Remueve todos los marcadores
        //pointAnnotationManager?.deleteAll()
        //markerList.clear()

        clearAnnotation()
        println(droneLongitudeList)
        //Click event de marcadors
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
            annotation:PointAnnotation ->
            onMarkerItemClick(annotation)
            true
        })

        // Click event de marcadors
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener { annotation ->
            onMarkerItemClick(annotation)
            true
        })

        // Implementa el touch listener para el mapa
        mapView?.getMapboxMap()?.addOnMapClickListener { point ->
            // Obtén las coordenadas del punto en el que se hizo clic
            val latitude = point.latitude()
            val longitude = point.longitude()

            // Haz algo con las coordenadas
            // Por ejemplo, puedes mostrar un mensaje con las coordenadas en la consola
            println("Clic en: $latitude, $longitude")
            true
        }

        //var carBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.baseline_directions_car_24))
        //for (i in 0 until carLongitudeList.size){
        //    val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
        //        //.withPoint(Point.fromLngLat(carLongitudeList.get(i), carLatitudeList.get(i)))
        //        .withPoint(carPositions[i].position)
        //        .withIconImage(carBitmap!!)
//
        //    markerList.add(pointAnnotationOptions)
        //}

        var droneBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.dron))
        for (i in 0 until droneLongitudeList.size){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(droneLongitudeList.get(i), droneLatitudeList.get(i)))
               //    val position = Point.fromLngLat(droneLongitudeList.get(i), droneLatitudeList.get(i))
               //            dronePositions[i] = position
               //.withPoint(dronePositions[i].position)
                .withIconImage(droneBitmap!!)

            markerList.add(pointAnnotationOptions)
        }

        var beehiveBitmap = convertDrawableToBitMap(AppCompatResources.getDrawable(this, R.drawable.beehive_icon))
        for (i in 0 until beehiveLongitudeList.size){
            val pointAnnotationOptions : PointAnnotationOptions = PointAnnotationOptions()
                //.withPoint(Point.fromLngLat(beehiveLongitudeList.get(i), beehiveLatitudeList.get(i)))
                .withPoint(beehivePositions[i].position)
                .withIconImage(beehiveBitmap!!)

            markerList.add(pointAnnotationOptions)
        }
        pointAnnotationManager?.create(markerList)
        droneLatitudeList.clear()
        droneLongitudeList.clear()
        markerList.clear()
        println("Limpiar")

    }

    private fun onMarkerItemClick(marker: PointAnnotation){
            var trobat: Boolean = false
            val clickedPosition = LatLng(marker.geometry.latitude(), marker.geometry.longitude())
            println("Clic en: $clickedPosition")

            for (carPosition in carPositions) {
                println("Posicions de:${carPosition.position.latitude()}, ${carPosition.position.longitude()}")
                val carLatLng = LatLng(carPosition.position.latitude(), carPosition.position.longitude())
                val distance = calculateDistance(clickedPosition, carLatLng)
                println("Distancia de:$distance")

                // Ajustar tolerància distància entre click i marker
                val thresholdDistance = 0.001 // Exemple: 0.01 graus

                if (distance <= thresholdDistance) {
                    trobat = true
                    // És una posició de cotxe propera al clic
                    val message = "Matrícula: " + carPosition.matricula + "\n" +
                            "Estat: " + carPosition.statusText + "\n" +
                            "Bateria: " + carPosition.bateria + "\n" +
                            "Data últim manteniment: " + carPosition.ultim_manteniment + "\n" +
                            "Posició actual: [" + carPosition.position.latitude() + ", " + carPosition.position.longitude() + "]\n" +
                            "Posició d'inici: [" + carPosition.puntInici.latitude() + ", " + carPosition.puntInici.longitude() + "]\n" +
                            "Posició destí: [" + carPosition.puntDesti.latitude() + ", " + carPosition.puntDesti.longitude() + "]"
                    if (!popupDisplayedCotxe) {
                        popupDisplayedCotxe = true
                        AlertDialog.Builder(this)
                            .setTitle("Informació cotxe ${carPosition.id}")
                            .setMessage(message)
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                                popupDisplayedCotxe = false
                            }
                            .show()
                    }
                }
            }

        for (dronPosition in dronePositions) {
            println("Posicions de:${dronPosition.position.latitude()}, ${dronPosition.position.longitude()}")
            val dronLatLng = LatLng(dronPosition.position.latitude(), dronPosition.position.longitude())
            val distance = calculateDistance(clickedPosition, dronLatLng)
            println("Distancia de:$distance")

            // Ajustar tolerància distància entre click i marker
            val thresholdDistance = 0.001 // Exemple: 0.01 graus

            if (distance <= thresholdDistance) {
                trobat = true
                // És una posició de drone propera al clic
                val message = "ID comanda: " + dronPosition.id_order + "\n" +
                        "ID colmena: " + dronPosition.beehive + "\n" +
                        "Estat: " + dronPosition.statusText + "\n" +
                        "Bateria: " + dronPosition.bateria + "\n" +
                        "Autonomia: " + dronPosition.autonomia + "\n" +
                        "Data últim manteniment: " + dronPosition.ultim_manteniment + "\n" +
                        "Posició actual: [" + dronPosition.position.latitude() + ", " + dronPosition.position.longitude() + "]\n" +
                        "Posició d'inici: [" + dronPosition.puntInici.latitude() + ", " + dronPosition.puntInici.longitude() + "]\n" +
                        "Posició destí: [" + dronPosition.puntDesti.latitude() + ", " + dronPosition.puntDesti.longitude() + "]"
                if (!popupDisplayedDron) {
                    popupDisplayedDron = true
                    AlertDialog.Builder(this)
                        .setTitle("Informació dron ${dronPosition.id}")
                        .setMessage(message)
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                            popupDisplayedDron = false
                        }
                        .show()
                }
            }
        }

        for (beehivePosition in beehivePositions) {
            println("Posicions de:${beehivePosition.position.latitude()}, ${beehivePosition.position.longitude()}")
            val carLatLng = LatLng(beehivePosition.position.latitude(), beehivePosition.position.longitude())
            val distance = calculateDistance(clickedPosition, carLatLng)
            println("Distancia de:$distance")

            // Ajustar tolerància distància entre click i marker
            val thresholdDistance = 0.001 // Exemple: 0.01 graus

            if (distance <= thresholdDistance) {
                trobat = true
                // És una posició de cotxe propera al clic
                val message = "Posició actual: [" + beehivePosition.position.latitude() + ", " + beehivePosition.position.longitude() + "]\n"
                AlertDialog.Builder(this)
                    .setTitle("Informació colmena " + beehivePosition.id)
                    .setMessage(message)
                    .setPositiveButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }


            true
        /*if (iconImageBitmap == carBitmap) {
            // La anotación es un carbitmap
            AlertDialog.Builder(this)
                .setTitle("Car Marker Click")
                .setMessage("Car Marker Clicked")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }*/
    }

    //Calcular distància entre punts
    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val lat1 = Math.toRadians(start.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon2 = Math.toRadians(end.longitude)

        val dlon = lon2 - lon1
        val dlat = lat2 - lat1

        val a = (Math.sin(dlat / 2).pow(2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2).pow(2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val radius = 6371 // Radi de la Terra en quilòmetres

        return radius * c
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
        //if (isMapViewReady && (isCarsApiResponseReady || isDroneApiResponseReady) && isBeehivesApiResponseReady) {

            //Crida a la funcio que dibuixa els marcadors
            createMarkerOnMap()
        //}
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
                    val identificador = carObject.getInt("id_car")
                    val matricula = carObject.getString("license_plate")
                    val estat = carObject.getString("status_text")
                    val bateria = carObject.getString("battery")
                    val ultim_manteniment = carObject.getString("last_maintenance_date")
                    val paquets = carObject.getJSONArray("packages")

                    val punt_inici = carObject.getJSONObject("location_in ")
                    val latitude_inici = punt_inici.getDouble("latitude")
                    val longitude_inici = punt_inici.getDouble("longitude")

                    val punt_desti = carObject.getJSONObject("location_end")
                    val latitude_desti = punt_desti.getDouble("latitude")
                    val longitude_desti = punt_desti.getDouble("longitude")

                    val locationAct = carObject.getJSONObject("location_act")
                    val latitude = locationAct.getDouble("latitude")
                    val longitude = locationAct.getDouble("longitude")

                    println("Coordenades del cotxe " + carObject.getInt("id_car") + ": "
                    + latitude + ", " + longitude)

                    println("Dades cotxe: " + identificador + ", " + matricula + ", "
                            + estat + ", " + bateria + ", " + ultim_manteniment + ", " + paquets + ", " + punt_inici + ", " + punt_desti)

                    carLatitudeList.add(latitude)
                    carLongitudeList.add(longitude)

                    //Guarda posicio drone en el array de posicions dels drones
                    val pointInici = Point.fromLngLat(longitude_inici, latitude_inici)
                    val pointDesti = Point.fromLngLat(longitude_desti, latitude_desti)
                    val point = Point.fromLngLat(longitude, latitude)
                    val carData = MapaGestorEdge.CarData(identificador, matricula, estat, bateria, ultim_manteniment, arrayOf(), pointInici, pointDesti, point)
                    carPositions.add(carData)
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
        //droneLatitudeList.clear()
        //droneLongitudeList.clear()
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
                        val identificador = droneObject.getInt("id_dron")
                        val autonomia = droneObject.getString("autonomy")
                        val estat = droneObject.getString("status")
                        val bateria = droneObject.getString("battery")
                        val ultim_manteniment = droneObject.getString("last_maintenance_date")
                        val id_order = droneObject.getString("order_identifier")
                        val id_beehive = droneObject.getInt("id_beehive")

                        val punt_inici = droneObject.getJSONObject("location_in ")
                        val latitude_inici = punt_inici.getDouble("latitude")
                        val longitude_inici = punt_inici.getDouble("longitude")

                        val punt_desti = droneObject.getJSONObject("location_end")
                        val latitude_desti = punt_desti.getDouble("latitude")
                        val longitude_desti = punt_desti.getDouble("longitude")

                        val locationAct = droneObject.getJSONObject("location_act")
                        val latitude = locationAct.getDouble("latitude")
                        val longitude = locationAct.getDouble("longitude")
                        println("Coordenades del drone " + droneObject.getInt("id_dron") + ": "
                                + latitude + ", " + longitude)

                        droneLatitudeList.add(latitude)
                        droneLongitudeList.add(longitude)

                        //Guarda posicio drons en el array de posicions dels drons
                        val pointInici = Point.fromLngLat(longitude_inici, latitude_inici)
                        val pointDesti = Point.fromLngLat(longitude_desti, latitude_desti)
                        val point = Point.fromLngLat(longitude, latitude)
                        val dronData = MapaGestorEdge.DronData(identificador, estat, bateria, ultim_manteniment, autonomia, id_order, id_beehive, pointInici, pointDesti, point)
                        dronePositions.add(dronData)
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
                    val id_beehive = beehiveObject.getInt("id_beehive")
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

                    //Guarda posicio beehive en el array de posicions dels beehives
                    val point = Point.fromLngLat(longitude, latitude)
                    val beehiveData = MapaGestorEdge.BeehiveData(id_beehive, point)
                    beehivePositions.add(beehiveData)

                }

                //val spinner = findViewById<Spinner>(R.id.mapSpinner)

                // Crear un adaptador personalizat
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinner_edges)

                // Especificar l'estil del adaptador
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // Asignar l'adaptador al Spinner
                //spinner.adapter = adapter

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
        }, 0, 700) // Actualiza cada 20 segundos (20,000 milisegundos)
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
        }, 0, 700) // Actualiza cada 20 segundos (20,000 milisegundos)
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

    fun configurarMapa(numEdge: Int) {

    }

}