package com.example.repacc.reporteDetalle.view.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.example.repacc.R
import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Reporte
import com.example.repacc.pojo.Servicio
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import kotlinx.android.synthetic.main.item_serviciosrepdet.view.*

class ServiciosAdapter(
    val serviciosSolicitados: ArrayList<Servicio>,
    val context: Context,
    val listaEstados: List<String>?,
    val listener: OnStateSelected,
    val estados: ArrayList<Estado>? = null,
    var mReporte: Reporte
): RecyclerView.Adapter<ServiciosAdapter.ServiciosViewHolder>() {

    class ServiciosViewHolder(servicioItemView: View): RecyclerView.ViewHolder(servicioItemView) {

        var primeraVez = true

        fun bind(
            servicio: Servicio,
            context: Context,
            listaEstados: List<String>?,
            listener: OnStateSelected,
            estados: ArrayList<Estado>? = null,
            mReporte: Reporte
        ) = with(itemView){
            itmSerRep_servicio.setText(servicio.tipo.nombre)

            // Si es un agente y presta un servicio solicitado en el reporte
            if (
                Constantes.config?.agente != null &&
                Constantes.config?.agente?.servicio == servicio.tipo.codigo &&
                listaEstados != null
            ){
                itmSerRep_estado.visibility = View.GONE
                itmSerRep_spiner.adapter = Util.obtenerArrayAdapter(listaEstados,context)
                itmSerRep_spiner.setSelection(listaEstados.indexOf(servicio.estado.nombre))
                itmSerRep_spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                        if (primeraVez){
                            primeraVez = false
                        }else{
                            // Si ya validaron el reporte
                            if (!mReporte.esFalAlarm && mReporte.agenteFalAlarm != null){
                                listener.onClickStateServiceListener(
                                    servicio = servicio,
                                    position = position,
                                    estado = estados?.get(position)
                                )
                            }else{
                                Util.mostrarToast(context,context.getString(R.string.valide_reporte))
                            }
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
            }else{
                itmSerRep_estado.setText(servicio.estado.nombre)
                itmSerRep_spiner.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiciosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_serviciosrepdet,parent,false)
        return ServiciosViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ServiciosViewHolder, position: Int) {
        return holder.bind(serviciosSolicitados[position], context, listaEstados, listener, estados, mReporte)
    }

    override fun getItemCount(): Int {
        return serviciosSolicitados.size
    }

    fun setReporte(mReporte: Reporte) {
        this.mReporte = mReporte
    }

}