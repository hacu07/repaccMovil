package com.example.repacc.reporteDetalle.view.fragments.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import com.example.repacc.R
import com.example.repacc.pojo.Entidad
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.view.fragments.EntidadFragmentPresenter
import com.example.repacc.reporteDetalle.view.fragments.EntidadFragmentPresenterClass
import com.example.repacc.reporteDetalle.view.fragments.callback.InfoAtendidoCallback
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.fragment_clinica.*


class ClinicaFragment(val mReporte: Reporte, val callback: InfoAtendidoCallback) :
    DialogFragment(),
    DialogInterface.OnShowListener,
    ClinicaFragmentView{

    //guarda datos ingresados
    private var positiveButton: Button? = null
    private var mPresenter: EntidadFragmentPresenter? = null
    private var clinica_nombres: Spinner? =null
    private var clinica_descrip: EditText? =null
    private var clinica_progreso: ProgressBar? =null

    //Datos de la UI
    private var unidadMedica: Entidad? = null
    private var descripTraslado: String? = null

    init {
        mPresenter = EntidadFragmentPresenterClass(this)
    }

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        //Lo que se va a mostrar como dialogo
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            .setTitle(R.string.seleccionar_clinica)
            .setPositiveButton(R.string.aceptar, null)
            .setNeutralButton(R.string.cancelar, null)

        val view: View =
            activity?.layoutInflater!!.inflate(R.layout.fragment_clinica, null)
        builder.setView(view) // Se agrega la vista al dialogo

        this.clinica_nombres = view?.findViewById<Spinner>(R.id.clinica_nombres)
        this.clinica_descrip = view?.findViewById<EditText>(R.id.clinica_descrip)
        this.clinica_progreso = view?.findViewById<ProgressBar>(R.id.clinica_progreso)

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener(this)

        return dialog
    }

    // Se activa una vez se muestra el dialogo
    override fun onShow(dialogInterface: DialogInterface?) {

        //Obtiene el dialogo actual
        val dialog = dialog as AlertDialog?

        //Agrega los btn locales al dialogo y asigna su listener
        if (dialog != null) {
            positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
            val negativeButton: Button = dialog.getButton(Dialog.BUTTON_NEGATIVE)
            positiveButton!!.setOnClickListener(View.OnClickListener {
                callback.atendidoCallback(
                    unidadMedica = unidadMedica,
                    descripTraslado = clinica_descrip?.text.toString().trim()
                )
                dismiss()
            })
            negativeButton.setOnClickListener(View.OnClickListener {
                //quita el dialogo
                callback.atendidoCallback(null,null)
                dismiss()
            })
        }
        mPresenter?.onShow()
    }

    override fun onResume() {
        mPresenter?.buscarClinicasMunicipio(context!!,mReporte.municipioReg!!._id!!)
        super.onResume()
    }

    override fun onDestroy() {
        mPresenter?.onDestroy()
        super.onDestroy()
    }

    /***********************
     *  View interface
     */
    override fun habilitarElementos(habilita: Boolean) {
        clinica_descrip?.isEnabled = habilita
        this.clinica_nombres?.isEnabled = habilita
    }

    override fun mostrarProgreso(muestra: Boolean) {
        clinica_progreso?.visibility = if (muestra) View.VISIBLE else View.GONE
    }

    override fun ocultarSpinnerEntidades() {
        clinica_progreso?.visibility = View.GONE
    }

    override fun cargarEntidades(
        entidades: ArrayList<Entidad>?,
        nombresEntidades: MutableList<String>
    ) {
        clinica_nombres?.adapter = Util.obtenerArrayAdapter(nombresEntidades,context!!)
        clinica_nombres?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.i("REPACC",nombresEntidades[position])
                unidadMedica = entidades?.get(position)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    override fun mostrarMsj(msj: String) {
        Util.mostrarToast(context!!,msj)
    }
}
