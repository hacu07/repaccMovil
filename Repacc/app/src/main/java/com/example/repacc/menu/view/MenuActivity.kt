package com.example.repacc.menu.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.repacc.R
import com.example.repacc.contacto.view.ContactoActivity
import com.example.repacc.perfil.view.PerfilActivity
import com.example.repacc.reporte.view.ReporteActivity
import com.example.repacc.reportes.view.ReportesActivity
import com.example.repacc.vehiculo.view.VehiculoActivity
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        setSupportActionBar(toolbarMenu)
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
        }
        return super.onOptionsItemSelected(item)
    }

    private fun irPerfil() {
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }

    fun irContactos(view: View) {
        val intent = Intent(this, ContactoActivity::class.java)
        startActivity(intent)
    }

    fun irVehiculos(view: View) {
        val intent = Intent(this, VehiculoActivity::class.java)
        startActivity(intent)
    }

    fun irReporte(view:View){
        val intent = Intent(this, ReporteActivity::class.java)
        startActivity(intent)
    }

    fun irReportes(view: View) {
        val intent = Intent(this, ReportesActivity::class.java)
        startActivity(intent)
    }

}
