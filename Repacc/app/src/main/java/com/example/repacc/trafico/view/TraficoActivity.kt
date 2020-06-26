package com.example.repacc.trafico.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.repacc.R
import com.example.repacc.util.AlertCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception

class TraficoActivity : AppCompatActivity(),
    OnMapReadyCallback,
        android.location.LocationListener{

    private lateinit var mMap: GoogleMap

    private lateinit var locationManager: LocationManager
    private  var currentLocation: Location? = null // Gestionar la ubicacion actual

    private var marker: Marker? = null
    private lateinit var cameraPosition: CameraPosition

    private var latLong : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trafico)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Acepto los permisos
        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            ejecutarPermisos(requestCode)
        }else{
            // No Acepto los permisos
            mostrarMsj( getString(R.string.msj_permiso_ubicacion))
            finish()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = false // Quita btn azul al obtener location
        mMap.uiSettings.isMapToolbarEnabled = false // botones para ir a app de google maps
        mMap.isTrafficEnabled = true
        mMap.setMaxZoomPreference(17f)
        mMap.setMinZoomPreference(13f)

        validarPermiso(android.Manifest.permission.ACCESS_FINE_LOCATION, Constantes.LOCATION_PERMISSION_REQUEST_CODE)
    }

    /*  Valida si el permiso es aceptado por el usuario
    * */
    private fun validarPermiso(permissionStr: String, requestPermission: Int) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Si no lo ha permitido
            if(ContextCompat.checkSelfPermission(this, permissionStr) !=
                PackageManager.PERMISSION_GRANTED){
                var permisos: Array<String>
                if(requestPermission == Constantes.LOCATION_PERMISSION_REQUEST_CODE){
                    permisos = arrayOf(permissionStr,android.Manifest.permission.ACCESS_COARSE_LOCATION)
                }else{
                    permisos = arrayOf(permissionStr)
                }
                ActivityCompat.requestPermissions(this,
                    permisos!!, requestPermission)
                return
            }
        }
        // El permiso ha sido aceptado anteriormente...
        ejecutarPermisos(requestPermission)
    }

    /* Ejecuta las acciones una vez se ha validado que se tiene el permiso
    * */
    private fun ejecutarPermisos(requestCode: Int) {
        when(requestCode){
            Constantes.LOCATION_PERMISSION_REQUEST_CODE -> {
                // Valida si no tiene el gps del dispositivo encendido
                if (!isGpsEnabled()){
                    // Muestra dialogo para encender el gps
                    showInfoAlert()
                }else{
                    //Tiene el gps encendido, obtiene ultima localizacion conocida y ha aceptado los permisos
                    currentLocation = getLocation()

                    if(currentLocation != null){
                        //actualiza la posicion del marcados
                        actualizarLocation(currentLocation!!.latitude, currentLocation!!.longitude)
                    }
                }
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        try{
            val  gpsSignal = Settings.Secure.getInt(this.contentResolver, Settings.Secure.LOCATION_MODE)
            // Si es igual a 0 debe retornar false (gps apagado)
            return !(gpsSignal == 0)
        }catch (error: Settings.SettingNotFoundException){
            return false
        }
    }

    /*
    * Muestra cuadro de dialogo indicando opciones al usuario para encender el gps
    * en caso de no estarlo.
    * */
    private fun showInfoAlert(){
        Util.showInfoAlert(
            context = this,
            title = getString(R.string.titulo_encender_gps),
            msg = getString(R.string.activar_gps),
            textBtnPos = getString(R.string.aceptar),
            textBtnNeg = getString(R.string.cancelar),
            callback = object: AlertCallback {
                override fun onClickPositive() {
                    // Presentar interfaz de android para encender GPS
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                override fun onClickNegative() {
                    mostrarMsj(getString(R.string.no_encendio_gps_trafico))
                    //finish()
                }
            }
        )
    }

    fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    private fun getLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000, // milisegundos
            50F, // metros
            this
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            5000, // milisegundos
            50F, // metros
            this
        )


        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null){
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        return location
    }

    fun actualizarLocation(lat : Double, long: Double){
        try {
            createOrUpdateMarkerByLocation(lat,long);
            zoomToLocation(lat,long);
            latLong = LatLng(lat,long)
        }catch (err: Exception){
            //ignore
            mostrarMsj(getString(R.string.intentar_nuevamente))
        }

    }

    private fun createOrUpdateMarkerByLocation(lat : Double, long: Double) {
        val latLng = LatLng( lat, long)

        if (marker == null){
            marker = mMap.addMarker(
                MarkerOptions().position(
                    latLng
                )
            )
        }else{
            marker?.position = latLng
        }

    }

    private fun zoomToLocation(lat : Double, long: Double) {
        cameraPosition = CameraPosition.Builder()
            .target( LatLng( lat, long))
            .zoom(15F) // limit 21
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun obtenerUbicacion(view: View) {
        if (Util.validarInternet(applicationContext)){
            ejecutarPermisos(Constantes.LOCATION_PERMISSION_REQUEST_CODE)
        }else{
            mostrarMsj(getString(R.string.no_internet))
        }
    }

    /***************
     * LOCATION LISTENER
     */
    override fun onLocationChanged(location: Location?) {
        if(location != null){
            actualizarLocation(location.latitude, location.longitude)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {
        ejecutarPermisos(Constantes.LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onProviderDisabled(p0: String?) {
        showInfoAlert()
    }

}
