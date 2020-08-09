package com.example.repacc.reporteDetalle.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Entidad
import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Reporte
import com.example.repacc.pojo.Servicio
import com.example.repacc.reporteDetalle.ReporteDetallePresenter
import com.example.repacc.reporteDetalle.ReporteDetallePresenterClass
import com.example.repacc.reporteDetalle.view.adapters.OnStateSelected
import com.example.repacc.reporteDetalle.view.adapters.ServiciosAdapter
import com.example.repacc.reporteDetalle.view.fragments.callback.InfoAtendidoCallback
import com.example.repacc.reporteDetalle.view.fragments.view.ClinicaFragment
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
import kotlinx.android.synthetic.main.reportedetalle_content.*
import java.text.SimpleDateFormat

class ReporteDetalleActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    ReporteDetalleView,
    OnStateSelected{

    private val context: Context = this

    private lateinit var mMap: GoogleMap
    private lateinit var mReporte : Reporte

    private lateinit var mPresenter: ReporteDetallePresenter

    private lateinit var mServiciosAdapter: ServiciosAdapter

    private lateinit var cameraPosition: CameraPosition
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte_detalle)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        inicializar()
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        mReporte = intent.extras!!.getSerializable(Reporte::class.java.name) as Reporte

        repdet_etCodRep.setText("${mReporte.codigo}")
        repdet_etdir.setText("${mReporte.direccion}")
        repdet_ether.setText("${mReporte.numHeridos}")
        repdet_etDes.setText("${mReporte.descripcion}")

        val format = SimpleDateFormat("hh:mm dd/MM/yyy")
        repdet_fecha.setText(
            format.format(mReporte.date)
        )

        if(mReporte.imagen != null){
            Glide.with(this)
                .load(mReporte.imagen)
                .error(android.R.drawable.ic_menu_camera)
                .centerCrop()
                .into(repdet_ima)
        }

        if (mReporte.serviciosSolicitados != null){
            rvRepDet.layoutManager = LinearLayoutManager(this)
            mServiciosAdapter = ServiciosAdapter(
                mReporte.serviciosSolicitados!!,
                this,
                null,
                this,
                null,
                mReporte
            )
            rvRepDet.adapter = mServiciosAdapter
        }

        mPresenter = ReporteDetallePresenterClass(this, mReporte)
        mPresenter.onCreate()
        obtenerEstados()
    }

    /**************************************
     * Solo para rol agente:
     * Obtiene los estados de servicio solo si el tipo de servicio del agente esta entre los solicitados
     * del reporte
     */
    private fun obtenerEstados() {
        if(Util.esAgente()){
            // Busca si el servicio que presta el agente es solicitado en el reporte
            val servicio =
                mReporte.serviciosSolicitados?.find {
                        servicio ->
                    servicio.tipo.codigo == Constantes.config?.agente?.servicio
                }

            // Encontro servicio en reporte
            if (servicio != null){
                mPresenter.obtenerEstadosServicios(this)
            }

        }
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
        mMap.setMaxZoomPreference(17f)
        mMap.setMinZoomPreference(13f)
        mMap.uiSettings.isMapToolbarEnabled = false

        createOrUpdateMarkerByLocation(mReporte.latitud!!, mReporte.longitud!!)
        zoomToLocation(mReporte.latitud!!, mReporte.longitud!!)
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
                ).draggable(false).title(mReporte.direccion)
            )
        }else{
            marker?.position = latLng
        }

    }

    fun asignarUbicacion(view: View) {
        zoomToLocation(mReporte.latitud!!, mReporte.longitud!!)
    }

    /***********************************
     * Interface View
     */
    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun validarFalsaAlarma() {
        Util.showInfoAlert(
            context = this,
            title = getString(R.string.validar_reporte_title),
            msg = getString(R.string.validar_reporte_msj),
            textBtnPos = getString(R.string.reporte_valido),
            textBtnNeg = getString(R.string.falsa_alarma),
            callback = object : AlertCallback {
                override fun onClickPositive() {
                    actualizarEstado(false)
                }

                override fun onClickNegative() {
                    actualizarEstado(true)
                }
            }
        )
    }

    private fun actualizarEstado(esFalsaAlarma: Boolean) {
        mReporte.let {
            it.esFalAlarm = esFalsaAlarma
            mPresenter.actualizarEstadoReporte(this,it)
        }
    }

    override fun setReporte(reporte: Reporte) {
        mReporte = reporte
        mServiciosAdapter.setReporte(mReporte)
    }

    // Si el agente actual presta algun servicio solicitado en el reporte
    override fun reloadServices(
        listaEstados: MutableList<String>,
        estados: ArrayList<Estado>
    ) {
        if (mReporte.serviciosSolicitados != null){
            rvRepDet.layoutManager = LinearLayoutManager(this)
            mServiciosAdapter = ServiciosAdapter(
                mReporte.serviciosSolicitados!!,
                this,
                listaEstados,
                this,
                estados,
                mReporte)
            rvRepDet.adapter = mServiciosAdapter
        }
    }

    /*************************************
     * OnStateSelected
     */
    override fun onClickStateServiceListener(servicio: Servicio, position: Int,  estado: Estado?) {
        var unidadMedica: Entidad? = null
        var descriptraslado: String? = null
        // Si el servicio se atendio y fue un agente tipo "Ambulancia"
        if(estado?.codigo == Constantes.ESTADO_CODIGO_ATENDIDO &&
                Constantes.config?.agente?.servicio == Constantes.SERVICIO_AMBULANCIA){
            // Obtiene entidad y descripcion de traslado
            ClinicaFragment(
                mReporte,
                object : InfoAtendidoCallback{
                    override fun atendidoCallback(unidadMedica: Entidad?, descripTraslado: String?) {
                        mPresenter?.actualizarEstadoAgenRepo(
                            context,
                            position,
                            servicio,
                            unidadMedica,
                            descripTraslado)
                    }
                })
                .show(supportFragmentManager, getString(R.string.seleccionar_clinica))
        }else{
            mPresenter?.actualizarEstadoAgenRepo(this,position,
                servicio, unidadMedica, descriptraslado)
        }


    }
}
