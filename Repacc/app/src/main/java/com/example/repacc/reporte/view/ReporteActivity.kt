package com.example.repacc.reporte.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.repacc.BuildConfig
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_reporte.*
import kotlinx.android.synthetic.main.content_reporte.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
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

    // Imagen reporte
    private lateinit var mCurrentPhotoPath : String
    private var mPhotoSelectedUri: Uri? = null
    private lateinit var bitmap: Bitmap

    private val MY_PHOTO = "my_photo" // Nombre de foto, nombre local y en nube


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

            val direccion = addresses!![0].getAddressLine(0).split(',')
            val lengthSplit = direccion.size
            val reporte = Reporte(
                placas = mPlacasAdapter!!.obtenerPlacas(),
                latlong = "${latLong!!.latitude.toString()},${latLong!!.longitude.toString()}",
                latitud = latLong!!.latitude,
                longitud = latLong!!.longitude,
                direccion = addresses!![0].getAddressLine(0),
                pais = direccion[lengthSplit-1].trim(),//addresses!![0].countryName,
                departamento = direccion[lengthSplit-2].trim(),//addresses!![0].adminArea,
                municipio = direccion[lengthSplit-3].trim(),//addresses!![0].locality,
                usuarioReg = Constantes.config!!.usuario!!,
                servicios = servicios,
                numHeridos = numHeri,
                descripcion = etDescripReporte.text.toString().trim()
            )
            mPresenter.registrarReporte(this,reporte,mPhotoSelectedUri)
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
        }
        // Si no acepto el permiso de ubicacion
        else if (requestCode == Constantes.LOCATION_PERMISSION_REQUEST_CODE &&
            !(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) ){
            // No Acepto los permisos de ubicacion
            mostrarMsj( getString(R.string.msj_permiso_ubicacion))
            finish()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    /* Ejecuta las acciones una vez se ha validado que se tiene el permiso
    * */
    private fun ejecutarPermisos(requestCode: Int) {
        when(requestCode){
            // Acepto el permiso de ubicacion
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

            // Acepto el permiso de galeria
            Util.RC_GALLERY -> {
                fromGallery()
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

    /****************************************
     * Carga imagen desde galeria o camara
     */
    fun cargarImagen(view: View) {
        Util.showInfoAlert(
            context = this,
            title = getString(R.string.cargar_imagen_reporte),
            msg = getString(R.string.seleccion_carga_imagen),
            textBtnPos = getString(R.string.camara),
            textBtnNeg = getString(R.string.galeria),
            callback = object: AlertCallback{
                override fun onClickPositive() {
                    // Camara
                    checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, Util.RP_CAMERA)
                }

                override fun onClickNegative() {
                    // Galeria
                    checkPermissionToApp(Manifest.permission.READ_EXTERNAL_STORAGE, Util.RP_STORAGE)
                }
            }
        )
    }

    /*********************************
     * Sirve para solicitar servicios
     * NO SE USA
     */
    private fun checkPermissionToApp(permissionStr: String, requestPermission: Int) {
        // Valida los permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this,permissionStr) != PackageManager.PERMISSION_GRANTED){
                // Si el permiso no se ha concedido, se solicita
                val arrayPermission = arrayOf(permissionStr)
                ActivityCompat.requestPermissions(this,arrayPermission, requestPermission)
                return // Corta proceso y queda a la espera de la respuesta del usuario
            }
        }

        when(requestPermission){
            Util.RP_STORAGE -> {
                fromGallery()
            }
            Util.RP_CAMERA->{
                dispacthTakePictureIntent()
            }
        }
    }

    private fun dispacthTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile: File?
            photoFile = CreateImageFile()
            if (photoFile != null) {
                //Extrae ubicacion del archivo del fileprovider (facilita de forma segura compartir archivos asociados a la app)
                val photoUri: Uri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider", photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(takePictureIntent, Util.RC_CAMERA)
            }
        }
    }

    private fun CreateImageFile(): File? {
        val timestamp: String =
            SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.ROOT).format(Date())
        val imageFileName: String = MY_PHOTO.toString() + timestamp + "_"

        //Directorio unico donde se aloja la imagen

        //Directorio unico donde se aloja la imagen
        val storageDir =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES) //Hace que las fotos sean solo privadas para la app


        var image: File? = null

        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
            //Extrae ruta de la imagen temporal
            mCurrentPhotoPath = image.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image
    }

    /*********************************
     * Obtiene imagen desde la galeria
     */
    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, Util.RC_GALLERY)
    }

    /*********************************+
     * Respuesta a Activity for result
     * se usa para obtener imagen de la galeria o de la camara
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            // Controla respuesta desde la galeria o camara
            when(requestCode){
                // Respuesta de la galeria
                Util.RC_GALLERY -> {
                    getImageFromGallery(data)
                }

                // Respuesta de la camara
                Util.RC_CAMERA -> {
                    mPhotoSelectedUri = addPicGallery()!!
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            mPhotoSelectedUri
                        )
                        this.bitmap = bitmap
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    asignarImagenReporte(bitmap)
                    rotarImgRep.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun asignarImagenReporte(bitmap: Bitmap?) {
        fotoReporte.setImageBitmap(bitmap)
        fotoReporte.scaleType = ImageView.ScaleType.CENTER_INSIDE
        fotoReporte.layoutParams.height = Util.IMAGE_HEIGHT
        fotoReporte.requestLayout()
    }

    /*******************************
     * Carga la imagen tomada desde la galeria
     */
    private fun getImageFromGallery(data: Intent?) {
        if (data != null){
            mPhotoSelectedUri = data.data!!

            try {
                //Contiene el bitmap segun la Uri especificada
                bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, mPhotoSelectedUri)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
                asignarImagenReporte(bitmap)
                rotarImgRep.visibility = View.VISIBLE
            }catch (e: Exception){
                mostrarMsj(getString(R.string.error_load_image))
            }
        }
    }

    fun rotarImagenReporte(view: View) {
        if (this.bitmap != null){
            val bitmap: Bitmap? = Util.rotateBitmap(this.bitmap, Util.ROTATE_90 )
            if (bitmap != null){
                this.bitmap = bitmap
                fotoReporte.setImageBitmap(bitmap)
            }
        }
    }

    //Imagen de la camara
    private fun addPicGallery(): Uri? {
        // Indica que se escanee el archivo y lo añade a la BD multimedia del dispositivo -  si no, no aparece en galeria
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(file) // Envia desde una Uri
        mediaScanIntent.data = contentUri // Envia como parametro
        this.sendBroadcast(mediaScanIntent)
        //mCurrentPhotoPath = null
        return contentUri
    }
}
