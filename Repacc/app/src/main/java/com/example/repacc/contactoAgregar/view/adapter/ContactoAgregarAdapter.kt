package com.example.repacc.contactoAgregar.view.adapter

import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.repacc.R
import com.example.repacc.pojo.Usuario
import kotlinx.android.synthetic.main.item_solicitud_enviar.view.*

class ContactoAgregarAdapter(val listaUsuarios: ArrayList<Usuario>,
                             val context: Context,
                             val listener: ContactoAgregarClickListener) :
    RecyclerView.Adapter<ContactoAgregarAdapter.ContactoAgregarViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactoAgregarViewHolder {
        val inflater =  LayoutInflater.from(parent.context).inflate(
            R.layout.item_solicitud_enviar,parent,false)
        return ContactoAgregarViewHolder(inflater)
    }

    override fun onBindViewHolder(
        holder: ContactoAgregarViewHolder,
        position: Int
    ) {
        return holder.bind(listaUsuarios.get(position), context, listener)
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    class ContactoAgregarViewHolder(usuarioAgregar: View) :RecyclerView.ViewHolder(usuarioAgregar) {

        fun bind(itmUsuario: Usuario, context: Context, listener: ContactoAgregarClickListener) = with(itemView){
            itmSolEnv_nombre.text = itmUsuario.nombre
            itmSolEnv_usuario.text = itmUsuario.usuario

            if(itmUsuario.foto != null){
                Glide.with(context)
                    .load(itmUsuario.foto)
                    .error(R.drawable.user)
                    .centerCrop()
                    .into(itmSolEnv_foto)
            }

            itmSolEnv_btnConfirmar.setOnClickListener {
                listener.onContactoClickListener(itmUsuario)
            }
        }
    }
}