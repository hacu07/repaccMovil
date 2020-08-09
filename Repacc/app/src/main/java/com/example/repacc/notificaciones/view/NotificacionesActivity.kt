package com.example.repacc.notificaciones.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.notificaciones.NotificacionesPresenter
import com.example.repacc.notificaciones.NotificacionesPresenterClass
import com.example.repacc.notificaciones.view.adapters.NotificationsAdapter
import com.example.repacc.notificaciones.view.adapters.OnNotificationClickListener
import com.example.repacc.pojo.Notificacion
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.view.ReporteDetalleActivity
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_notificaciones.*

class NotificacionesActivity : AppCompatActivity(), OnNotificationClickListener, NotificacionesView {

    private var notificaciones : ArrayList<Notificacion>? = null
    private var mAdapter: NotificationsAdapter? = null
    private var mPresenter : NotificacionesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificaciones)

        inicializar()
        cargarNotificaciones()
        mPresenter = NotificacionesPresenterClass(this)
        mPresenter?.onCreate()
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    private fun cargarNotificaciones() {
        if (notificaciones != null){
            mAdapter = NotificationsAdapter(this,notificaciones!!,this)
            rvNoti.adapter = mAdapter
        }
    }

    private fun inicializar() {
        //Captura las notificaciones enviadas entre actividades
        notificaciones = intent.extras!!.getSerializable(NotificacionesActivity::class.java.name) as ArrayList<Notificacion>
        rvNoti.layoutManager = LinearLayoutManager(this)
    }

    /***************************************************
     * OnNotificationsClickListener
     */
    override fun onNotificationClickListener(notificacion: Notificacion) {
        mPresenter?.obtenerReporte(this,notificacion.reporte)
    }

    /****************************************************
     * NotificacionesView
     */
    override fun habilitarElementos(habilita: Boolean) {
        rvNoti.isEnabled = habilita
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun irDetalleReporte(reporte: Reporte) {
        val intent = Intent(this, ReporteDetalleActivity::class.java)
        intent.putExtra(Reporte::class.java.name,reporte)
        startActivity(intent)
    }
}
