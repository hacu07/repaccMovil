package com.example.repacc.reporte.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import com.example.repacc.pojo.Tipo
import com.example.repacc.reporte.ReportePresenter
import com.example.repacc.reporte.ReportePresenterClass
import com.example.repacc.reporte.view.adapters.OnItemServicioClickListener
import com.example.repacc.reporte.view.adapters.OnPlacaClickListener
import com.example.repacc.reporte.view.adapters.PlacasAdapter
import com.example.repacc.reporte.view.adapters.ServiciosAdapter
import com.example.repacc.util.AlertCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_reporte.*
import kotlinx.android.synthetic.main.content_reporte.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ReporteActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    ReporteView,
    android.location.LocationListener,
    GoogleMap.OnMarkerDragListener,
    OnItemServicioClickListener,
    OnPlacaClickListener{

    private lateinit var mPresenter: ReportePresenter
    private var mServiciosAdapter: ServiciosAdapter? = null
    private var mPlacasAdapter: PlacasAdapter? = null

    private lateinit var mMap: GoogleMap

    private lateinit var locationManager: LocationManager
    private  var currentLocation: Location? = null // Gestionar la ubicacion actual

    private var marker: Marker? = null
    private lateinit var cameraPosition: CameraPosition

    // Cuando usa el marcador no actualiza mas la ubicacion
    private var isMarkerDraged = false

    // Variables para agregar al objeto Reporte
    private var servicios: ArrayList<Tipo>? = null
    private var placas: ArrayList<String>? = null
    private var addresses : List<Address>? = null
    private var latLong : LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte)
        setSupportActionBar(toolbarMenu)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        inicializar()
        mPresenter = ReportePresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerServicios(this)
    }

    private fun inicializar() {
        rvServicios.layoutManager = LinearLayoutManager(this)
        rvPlacas.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    /***************************************************
     * Menu - Toolbar
     */
    /*
    * Opcion para editar perfil en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_reporte, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnGuardarReporte -> registrarReporte()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registrarReporte() {
        if(validarDatosReporte()){

            var numHeri = 0
            // Numero de heridos
            if(!etHeridosReporte.text.toString().isNullOrEmpty()){
                numHeri =  etHeridosReporte.text.toString().toInt()
            }

            val reporte = Reporte(
                placas = mPlacasAdapter!!.obtenerPlacas(),
                latlong = "${latLong!!.latitude.toString()},${latLong!!.longitude.toString()}",
                latitud = latLong!!.latitude,
                longitud = latLong!!.longitude,
                direccion = addresses!![0].getAddressLine(0),
                pais = addresses!![0].countryName,
                departamento = addresses!![0].adminArea,
                municipio = addresses!![0].locality,
                usuarioReg = Constantes.config!!.usuario!!,
                servicios = servicios,
                numHeridos = numHeri,
                descripcion = etDescripReporte.text.toString().trim()
            )
            mPresenter.registrarReporte(this,reporte)
        }
    }


    private fun validarDatosReporte(): Boolean {
        var esValido = true

        if (marker == null){
            esValido = false
            mostrarMsj(getString(R.string.location_empty))
        }

        if(etDescripReporte.text.toString().isNullOrEmpty()){
            esValido = false
            tilDescripReporte.error = getString(R.string.descripcion_empty)
        }else{
            tilDescripReporte.error = null
        }

        if (mPlacasAdapter == null){
            esValido = false
            mostrarMsj(getString(R.string.placas_empty))
        }else{
            if (mPlacasAdapter!!.obtenerPlacas().isNullOrEmpty()){
                esValido = false
                mostrarMsj(getString(R.string.placas_empty))
            }
        }

        return esValido
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
            updateAddress(lat,long)
            createOrUpdateMarkerByLocation(lat,long);
            zoomToLocation(lat,long);
            latLong = LatLng(lat,long)
        }catch (err:Exception){
            //ignore
            mostrarMsj(getString(R.string.intentar_nuevamente))
        }

    }

    private fun updateAddress(lat : Double, long: Double) {
        this.addresses = Geocoder(this, Locale.getDefault())
            .getFromLocation(lat,long,1)
        tvDireccion.setText(addresses!![0].getAddressLine(0))
    }

    private fun zoomToLocation(lat : Double, long: Double) {
        cameraPosition = CameraPosition.Builder()
            .target( LatLng( lat, long))
            .zoom(15F) // limit 21
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun createOrUpdateMarkerByLocation(lat : Double, long: Double) {
        val latLng = LatLng( lat, long)

        if (marker == null){
            marker = mMap.addMarker(
                MarkerOptions().position(
                    latLng
                ).draggable(true)
            )
        }else{
            marker?.position = latLng
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
            callback = object: AlertCallback{
                override fun onClickPositive() {
                    // Presentar interfaz de android para encender GPS
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                override fun onClickNegative() {
                    mostrarMsj(getString(R.string.no_encendio_gps))
                    //finish()
                }
            }
        )
    }

    /****************************
     * El usuario ingreso la placa de un vehiculo a registrar
     */
    fun agregarPlaca(view: View) {
        val placa = etPlacaReporte.text.toString().trim().toUpperCase()

        if (placas == null){
            // inicializa array y adapter
            placas = ArrayList()
            mPlacasAdapter = PlacasAdapter(placas!!, this,this)
            rvPlacas.adapter = mPlacasAdapter
        }

        if(validarPlaca(placa)){
            if (!mPlacasAdapter!!.obtenerPlacas().contains(placa)){
                // modifica el recyclerView
                mPlacasAdapter!!.agregarPlaca(placa)
                etPlacaReporte.setText("")
            }else{
                mostrarMsj(getString(R.string.placa_registrada))
            }
        }

        if (!placas!!.isEmpty()){
            rvPlacas.visibility = View.VISIBLE
        }
    }

    private fun validarPlaca(placa: String): Boolean {
        var esValido = true

        if (placa.isNullOrEmpty()){
            esValido = false
            tilPlacaReporte.isErrorEnabled = true
            tilPlacaReporte.error = getString(R.string.error_placa)
        }else{
            tilPlacaReporte.isErrorEnabled = false
        }

        if (placa.length < 5){
            tilPlacaReporte.isErrorEnabled = true
            tilPlacaReporte.error = getString(R.string.error_length_placa)
        }else{
            tilPlacaReporte.isErrorEnabled = false
        }

        return esValido
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
        mMap.setMaxZoomPreference(17f)
        mMap.setMinZoomPreference(13f)
        mMap.setOnMarkerDragListener(this)

        validarPermiso(android.Manifest.permission.ACCESS_FINE_LOCATION, Constantes.LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun habilitarElementos(habilita: Boolean) {
        fabLocation.isEnabled = habilita
        fotoReporte.isEnabled = habilita
        tilPlacaReporte.isEnabled = habilita
        tilHeridosReporte.isEnabled = habilita
        tilDescripReporte.isEnabled = habilita
        rvServicios.isEnabled = habilita
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun mostrarProgreso(mostrar: Boolean) {
        pbReporte.visibility = if(mostrar) View.VISIBLE else View.GONE
    }

    override fun cargarServicios(listaServicios: ArrayList<Tipo>) {
        mServiciosAdapter = ServiciosAdapter(listaServicios,this,this)
        rvServicios.adapter = mServiciosAdapter
    }

    override fun mostrarDialogo(msj: String) {
        Util.showInfoAlert(
            context = this,
            title = getString(R.string.title_activity_reporte),
            msg = msj,
            textBtnPos = getString(R.string.aceptar),
            textBtnNeg = getString(R.string.cancelar),
            callback = object : AlertCallback {
                override fun onClickPositive() {
                    finish()
                }

                override fun onClickNegative() {
                    finish()
                }
            }
        )
    }

    /************************************
     * LOCATION LISTENER
     */
    override fun onLocationChanged(location: Location?) {
        if(location != null && !isMarkerDraged){
            actualizarLocation(location.latitude, location.longitude)
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    // Encendio el gps
    override fun onProviderEnabled(p0: String?) {
        ejecutarPermisos(Constantes.LOCATION_PERMISSION_REQUEST_CODE)
    }

    // Apago el gps
    override fun onProviderDisabled(p0: String?) {
        showInfoAlert()
    }

    fun obtenerUbicacion(view: View) {
        if (Util.validarInternet(applicationContext)){
            ejecutarPermisos(Constantes.LOCATION_PERMISSION_REQUEST_CODE)
        }else{
            mostrarMsj(getString(R.string.no_internet))
        }
    }

    /*********************************************
     *  OnMarkerDragListener
     * */
    override fun onMarkerDragEnd(marker: Marker?) {
        if (marker != null){
            isMarkerDraged = true
            actualizarLocation(marker!!.position!!.latitude,marker!!.position!!.longitude)
        }
    }

    override fun onMarkerDragStart(p0: Marker?) {
        //ignore
    }

    override fun onMarkerDrag(p0: Marker?) {
        //ignore
    }

    /*********************************************
     *  onClickServicioListener
     * */
    override fun onClickServicioListener(isChecked: Boolean, servicio: Tipo) {
        if (servicios == null){
            servicios = ArrayList<Tipo>()
        }

        // Si marcó el servicio
        if (isChecked){
            if (!servicios!!.contains(servicio)){
                servicios!!.add(servicio)
            }
        }else{
            // Desmarcó el servicio
            if (servicios!!.contains(servicio)){
                val index = servicios!!.indexOf(servicio)
                servicios!!.removeAt(index)
            }
        }
    }


    /*********************************************
     *  onPlacaClickListener
     * */
    override fun onPlacaClickListener(placa: String) {
        // Elimina la placa de la lista
        mPlacasAdapter!!.eliminarPlaca(placa)

        if (mPlacasAdapter!!.obtenerPlacas().isNullOrEmpty()){
            rvPlacas.visibility = View.GONE
        }
    }

}
