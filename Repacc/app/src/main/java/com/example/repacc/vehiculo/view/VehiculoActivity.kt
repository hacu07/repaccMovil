package com.example.repacc.vehiculo.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repacc.R
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.VehiculoPresenter
import com.example.repacc.vehiculo.VehiculoPresenterClass
import com.example.repacc.vehiculo.view.adapter.OnItemVehiculoListener
import com.example.repacc.vehiculo.view.adapter.VehiculoAdapter
import com.example.repacc.vehiculoAgregar.view.VehiculoAgregarActivity
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_vehiculo.*
import java.io.Serializable

class VehiculoActivity : Activity(), VehiculoView, OnItemVehiculoListener, Serializable{

    companion object{
        val FLAG_VEHICULO_ACTIVITY = "VEHICULO_ACTIVITY"
    }

    private lateinit var vehiculoAdapter: VehiculoAdapter
    private lateinit var mPresenter: VehiculoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo)
        inicializar()
        mPresenter = VehiculoPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerVehiculos(this)
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }

    private fun inicializar() {
        rvVehiculos.layoutManager = LinearLayoutManager(this)
    }

    fun agregarVehiculo(view: View){
        Constantes.vehiculoAgregar = this
        val intent = Intent(this, VehiculoAgregarActivity::class.java).apply {
            //val bundle = Bundle()
            //bundle.putSerializable(FLAG_VEHICULO_ACTIVITY, this@VehiculoActivity::class.java)
            //putExtras(bundle)
        }
        startActivity(intent)
    }

    override fun mostrarProgreso(mostrar: Boolean) {
        pbVehiculo.visibility = if(mostrar) View.VISIBLE else View.GONE
    }

    override fun habilitarElementos(habilita: Boolean) {
        rvVehiculos.isEnabled = habilita
        fabVehiculo.isEnabled = habilita
    }

    override fun cargarVehiculos(listaVehiculos: ArrayList<Vehiculo>) {
        vehiculoAdapter = VehiculoAdapter(listaVehiculos,this,this)
        rvVehiculos.adapter = vehiculoAdapter
    }

    override fun onItemVehiculoListener(vehiculo: Vehiculo) {

    }

    fun agregarVehiculo(vehiculo: Vehiculo){
        vehiculoAdapter.agregarVehiculo(vehiculo)
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this, msj)
    }
}
