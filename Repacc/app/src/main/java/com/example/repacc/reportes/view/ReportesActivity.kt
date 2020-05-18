package com.example.repacc.reportes.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.view.ReporteDetalleActivity
import com.example.repacc.reportes.ReportesPresenter
import com.example.repacc.reportes.ReportesPresenterClass
import com.example.repacc.reportes.view.adapters.OnReporteClickListener
import com.example.repacc.reportes.view.adapters.ReportesAdapter
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.activity_reportes.*
import java.io.Serializable

class ReportesActivity : AppCompatActivity(), ReportesView, OnReporteClickListener, Serializable {

    private lateinit var mPresenter: ReportesPresenter
    private lateinit var mReportesAdapter: ReportesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes)

        inicializar()

        mPresenter = ReportesPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerListaReportes(this)
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        rvReportes.layoutManager = GridLayoutManager(this,3)
    }

    override fun mostrarProgreso(mostrar: Boolean) {
        pbReportes.visibility = if(mostrar) View.VISIBLE else View.GONE
    }

    override fun habilitarElementos(habilita: Boolean) {
        svReportes.isEnabled = habilita
        rvReportes.isEnabled = habilita
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun cargarReportes(listaReportes: ArrayList<Reporte>) {
        mReportesAdapter = ReportesAdapter(listaReportes,this,this)
        rvReportes.adapter = mReportesAdapter
    }

    override fun mostrarReporte(reporte: Reporte) {
        val intent = Intent(this,ReporteDetalleActivity::class.java)
        intent.putExtra(Reporte::class.java.name,reporte)
        startActivity(intent)
    }

    /*************************
     * OnReporteClickListener
     */
    override fun onReporteClickListener(reporte: Reporte) {
        mPresenter.obtenerReporte(this,reporte._id!!)
    }
}
