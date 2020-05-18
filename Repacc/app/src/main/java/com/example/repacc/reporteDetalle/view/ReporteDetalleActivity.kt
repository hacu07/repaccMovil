package com.example.repacc.reporteDetalle.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.view.adapters.ServiciosAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.reportedetalle_content.*

class ReporteDetalleActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mReporte : Reporte

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

    private fun inicializar() {
        mReporte = intent.extras!!.getSerializable(Reporte::class.java.name) as Reporte

        repdet_etCodRep.setText("${mReporte.codigo}")
        repdet_etdir.setText("${mReporte.direccion}")
        repdet_ether.setText("${mReporte.numHeridos}")
        repdet_etDes.setText("${mReporte.descripcion}")
        repdet_fecha.setText(
            "${mReporte.date!!.hours}:${mReporte.date!!.minutes} ${mReporte.date!!.date}-${mReporte.date!!.month+1}-${mReporte.date!!.year+1900}"
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
            mServiciosAdapter = ServiciosAdapter(mReporte.serviciosSolicitados!!,this)
            rvRepDet.adapter = mServiciosAdapter
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
}
