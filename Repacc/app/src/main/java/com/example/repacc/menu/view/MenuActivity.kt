
package com.example.repacc.menu.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.example.repacc.R
import com.example.repacc.contacto.view.ContactoActivity
import com.example.repacc.login.view.LoginActivity
import com.example.repacc.menu.MenuPresenter
import com.example.repacc.menu.MenuPresenterClass
import com.example.repacc.notificaciones.view.NotificacionesActivity
import com.example.repacc.perfil.view.PerfilActivity
import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.pojo.Notificacion
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporte.view.ReporteActivity
import com.example.repacc.reporteDetalle.view.ReporteDetalleActivity
import com.example.repacc.reportes.view.ReportesActivity
import com.example.repacc.trafico.view.TraficoActivity
import com.example.repacc.util.AlertCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.SocketRepacc
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.view.VehiculoActivity
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.toolbar.*


class MenuActivity :
    AppCompatActivity(),
    MenuView,
    android.location.LocationListener{

    private var primerOnResume = true
    private var mPresenter: MenuPresenter? = null

    private lateinit var locationManager: LocationManager
    private  var currentLocation: Location? = null // Gestionar la ubicacion actual

    // Sockets Listeners
    private var mSocketConnectListener : Emitter.Listener?  = null
    private var mSocketNotificationListener : Emitter.Listener?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbarMenu)
        mostrarSwitchDisponible()
        mPresenter = MenuPresenterClass(this)
        mPresenter?.onCreate()
        createNotificationChannel()
        SocketRepacc.init()
        SocketRepacc.mSocket.let {
            setListeners()
            it!!.connect()
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_notification_general)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SocketRepacc.mSocket.let {
            offListeners()
            it!!.disconnect()
        }

        if (mPresenter != null){
            mPresenter?.onDestroy()
        }
    }

    override fun onResume() {
        if (Util.esAgente() && !primerOnResume){
            if (!isGpsEnabled()){
                showInfoAlert()
            }
        }
        if (primerOnResume){
            primerOnResume = false
        }else{
            habilitarElementos(true)
        }

        super.onResume()
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
                    }else{
                        // No logró obtener la ubicacion del dispositivo
                        mostrarMsj(getString(R.string.no_location))
                        val estadoActual = swDisponible.isChecked
                        swDisponible.isChecked = !estadoActual
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
                    mostrarMsj(getString(R.string.no_gps_activate))
                    finish()
                }
            }
        )
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
            60000, // milisegundos
            50F, // metros
            this
        )
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            60000, // milisegundos
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
            if (swDisponible.isChecked && mPresenter != null){
                // actualiza la posicion del agente
                Constantes.config?.agente?.latitud = lat
                Constantes.config?.agente?.longitud = long
                mPresenter?.cambiarEstado(this, swDisponible.isChecked)
            }
        }catch (err: Exception){
            //ignore
            mostrarMsj(getString(R.string.intentar_nuevamente))
        }

    }

    private fun mostrarSwitchDisponible() {
        if (Util.esAgente()){
            swDisponible.visibility = View.VISIBLE

            // Activa el switch segun estado del agente
            swDisponible.isChecked =
                (Constantes.config?.agente?.estado?.codigo == Constantes.ESTADO_CODIGO_ACTIVO)

            // Si no ha aceptado permiso
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
                // Solicita permisos
                validarPermiso(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Constantes.LOCATION_PERMISSION_REQUEST_CODE
                )
            }else{
                if(!isGpsEnabled()){
                    showInfoAlert()
                }
            }
        }else{
            swDisponible.visibility = View.GONE
        }
    }

    /*
    * Opcion para editar perfil en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnirPerfil -> irPerfil()
            R.id.mnSesion -> cerrarSesion()
            R.id.mnNotif -> mPresenter?.obtenerNotificaciones(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cerrarSesion() {
        // Si es agente y su estado es activo
        if (Util.esAgente() && swDisponible.isChecked){
            mostrarMsj(getString(R.string.desactivar_disponibilidad))
        }else{
            Constantes.config = null
            Util.guardarConfig(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun irPerfil() {
        habilitarElementos(false)
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }

    fun irContactos(view: View) {
        habilitarElementos(false)
        val intent = Intent(this, ContactoActivity::class.java)
        startActivity(intent)
    }

    fun irVehiculos(view: View) {
        habilitarElementos(false)
        val intent = Intent(this, VehiculoActivity::class.java)
        startActivity(intent)
    }

    fun irReporte(view:View){
        habilitarElementos(false)
        val intent = Intent(this, ReporteActivity::class.java)
        startActivity(intent)
    }

    fun irReportes(view: View) {
        val intent = Intent(this, ReportesActivity::class.java)
        startActivity(intent)
    }

    fun irTrafico(view: View) {
        habilitarElementos(false)
        val intent = Intent(this, TraficoActivity::class.java)
        startActivity(intent)
    }

    /*****************************************
     * Cambia el estado de disponibilidad del agente
     * HAROLDC 22/05/2020
     */
    fun cambiarEstado(view: View) {
        if (swDisponible.isChecked && mPresenter != null){
            validarPermiso(android.Manifest.permission.ACCESS_FINE_LOCATION, Constantes.LOCATION_PERMISSION_REQUEST_CODE)
        }else{
            mPresenter?.cambiarEstado(this, swDisponible.isChecked)
        }
    }


    /*****************************************************
     * MENUVIEW
     */
    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun habilitarElementos(habilita: Boolean) {
        swDisponible.isEnabled = habilita
        cvTrafico.isEnabled = habilita
        cvReporte.isEnabled = habilita
        cvRegisReporte.isEnabled = habilita
        cvVehiculo.isEnabled = habilita
        cvContacto.isEnabled = habilita
        barMenu.isEnabled = habilita
        pbMenu.visibility = if (habilita) View.GONE else View.VISIBLE
    }

    override fun asignarEstado(estado: Boolean) {
        swDisponible.isChecked = estado
    }

    override fun setEmitterListener(
        socketUsuario: SocketUsuario?,
        lastSocketId: String?
    ) {
        lastSocketId.let { pLastSocketId ->
            mSocketNotificationListener.let {listener ->
                SocketRepacc.mSocket?.off(Constantes.config!!.usuario!!.usuario, listener)
            }
        }

        mSocketNotificationListener = Emitter.Listener {args ->
            runOnUiThread{
                mPresenter?.getNotification(args)
            }
        }
        SocketRepacc.notificationListeners(mSocketNotificationListener!!, Constantes.config!!.usuario!!.usuario!!)
    }

    override fun showNotification(notificacion: Notificacion?) {
        // Create an Intent for the activity you want to start
        val resultIntent = Intent(this, ReporteDetalleActivity::class.java)
        resultIntent.putExtra(Reporte::class.java.name,notificacion!!.reporte)
        // Create the TaskStackBuilder
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        var builder = NotificationCompat.Builder(this, getString(R.string.channel_notification_general))
            .setSmallIcon(R.drawable.ic_notifications_none)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(notificacion?.mensaje)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificacion?.mensaje))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        builder.apply {
            setContentIntent(resultPendingIntent)
        }

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificacion!!.createdAt.time.toInt(), builder.build())
        }

    }

    /****************************************************
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
        if (isGpsEnabled() && swDisponible.isChecked && mPresenter != null){
            swDisponible.isChecked = true
            mPresenter?.cambiarEstado(this,swDisponible.isChecked)
        }
    }

    override fun onProviderDisabled(p0: String?) {
        if (mPresenter != null){
            swDisponible.isChecked = false
            Constantes.config?.agente?.latitud = 0.0
            Constantes.config?.agente?.longitud = 0.0
            mPresenter?.cambiarEstado(this,swDisponible.isChecked)
        }
        showInfoAlert()
    }

    override fun mostrarNotificaciones(notificaciones: ArrayList<Notificacion>) {
        val intent = Intent(this,NotificacionesActivity::class.java)
        intent.putExtra(NotificacionesActivity::class.java.name,notificaciones)
        startActivity(intent)
    }

    /**************************************************************+
     * Socket Listeners
     */
    private fun setListeners() {
        // Socket de conexión
        mSocketConnectListener = Emitter.Listener { args ->
            runOnUiThread {
                mPresenter?.initSocket(args)
            }
        }
        SocketRepacc.connectListener(mSocketConnectListener!!)
    }

    private fun offListeners() {
        try{
            mSocketConnectListener.let { SocketRepacc.mSocket?.off(Util.NEW_SOCKET_CONNECTION, it!!) }
            mSocketNotificationListener.let { SocketRepacc.mSocket?.off(Constantes.config!!.usuario!!.usuario!!, it!!)}
        }catch (e: Exception){
            //ignore this
        }
    }
}
