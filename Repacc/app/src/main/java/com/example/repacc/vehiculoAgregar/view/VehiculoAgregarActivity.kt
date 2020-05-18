package com.example.repacc.vehiculoAgregar.view

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.example.repacc.R
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.view.VehiculoActivity
import com.example.repacc.vehiculo.view.onAddVehicleListener
import com.example.repacc.vehiculoAgregar.VehiculoAgregarPresenter
import com.example.repacc.vehiculoAgregar.VehiculoAgregarPresenterClass
import kotlinx.android.synthetic.main.activity_vehiculo_agregar.*
import kotlinx.android.synthetic.main.vehiculo_contenido_edicion.*
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.Serializable

class VehiculoAgregarActivity : AppCompatActivity(), VehiculoAgregarView {
    private var colores: Array<String>? = null
    private lateinit var mPresenter: VehiculoAgregarPresenter
    private val context :Context = this
    private var vehiculoActivity: VehiculoActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehiculo_agregar)

        inicializar()
        configToolbar()
        mPresenter = VehiculoAgregarPresenterClass(this)
        mPresenter.onCreate()
        mPresenter.obtenerTipos(this)
    }

    private fun inicializar() {
        //this.vehiculoActivity = intent.extras?.getSerializable(VehiculoActivity.FLAG_VEHICULO_ACTIVITY) as VehiculoActivity
        if(Constantes.vehiculoAgregar != null){
            this.vehiculoActivity = Constantes.vehiculoAgregar
        }

    }

    private fun configToolbar() {
        setSupportActionBar(toolbarVehiculoEdicion)
    }

    /*
    * Opcion para guarda vehiculo en toolbar
    * */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_vehiculo_edicion, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnGuardarVehiculo -> guardarPerfil()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun guardarPerfil() {
        if(validarDatos()){
            mPresenter.registrarVehiculo(
                this,
                etPlacaVeh.text.toString().trim(),
                swEsParticular.isChecked,
                this.colores!!
            )
        }
    }

    private fun validarDatos(): Boolean {
        var esValido = true

        if (etPlacaVeh.text.toString().isNullOrEmpty()){
            esValido = false
            etPlacaVeh.error = getString(R.string.error_placa)
            etPlacaVeh.requestFocus()
        }else{
            etPlacaVeh.error = null
        }

        if (colores == null){
            esValido = false
            mostrarMsj(getString(R.string.error_color_vehiculo))
        }

        return esValido
    }

    override fun onDestroy() {
        Constantes.vehiculoAgregar = null
        mPresenter.onDestroy()
        super.onDestroy()
    }

    override fun mostrarProgreso(siMostrar: Boolean) {
        pbVehiculoAgregar.visibility = if(siMostrar) View.VISIBLE else View.GONE
    }

    override fun habilitarElementos(siHabilita: Boolean) {
        tilPlacaVeh.isEnabled = siHabilita
        spiTipVeh.isEnabled = siHabilita
        spiMarVeh.isEnabled = siHabilita
        spiModVeh.isEnabled = siHabilita
        btnColorVeh.isEnabled = siHabilita
        swEsParticular.isEnabled = siHabilita
        imvFotoVehi.isEnabled = siHabilita
    }

    override fun cargarSpiTipos(listaTipos: List<String>) {
        spiTipVeh.adapter = obtenerArrayAdapter(listaTipos)

        spiTipVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.obtenerMarcas(context,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarSpiMarcas(listaMarcas: List<String>) {
        spiMarVeh.adapter = obtenerArrayAdapter(listaMarcas)
        spiMarVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.obtenerModelos(context,position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun cargarSpiModelos(listaModelos: List<String>) {
        spiModVeh.adapter = obtenerArrayAdapter(listaModelos)
        spiModVeh.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mPresenter.asignarModelo(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(this,msj)
    }

    override fun finalizar() {
        finish()
    }

    private fun obtenerArrayAdapter(lista: List<String>): SpinnerAdapter? {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, lista)

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        return dataAdapter
    }

    fun mostrarColorPicker(view: View){
        val colorPicker = ColorPicker(this)

        val listaColores = ArrayList<String>()
        listaColores.add("#000000") //NEGRO
        listaColores.add("#FFFFFF") //BLANCO
        listaColores.add("#424242") //GRIS
        listaColores.add("#d50000") //ROJO
        listaColores.add("#0d47a1") //AZUL
        listaColores.add("#2e7d32") //VERDE
        listaColores.add("#f9a825") //AMARILLO
        listaColores.add("#ef6c00") //NARANJA

        colorPicker.setColors(listaColores)
            .setColumns(5)
            .setTitle(getString(R.string.title_color_picker))
            .setRoundColorButton(true)
            .setOnFastChooseColorListener(object: ColorPicker.OnFastChooseColorListener{
                override fun setOnFastChooseColorListener(position: Int, color: Int) {
                    asignarColor(listaColores[position])
                }

                override fun onCancel() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            .show()
    }

    private fun asignarColor(color: String) {
        colores = arrayOf(color)
        btnColorVeh.setBackgroundColor(Color.parseColor(color))
    }

    override fun agregarVehiculo(vehiculo: Vehiculo) {
        vehiculoActivity?.agregarVehiculo(vehiculo)
    }
}
