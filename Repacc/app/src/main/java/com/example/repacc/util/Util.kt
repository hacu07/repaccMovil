package com.example.repacc.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.repacc.registro.view.RegistroActivity
import kotlin.reflect.KClass

class Util {

    //Elementos estaticos
    companion object{

        fun mostrarToast(context: Context, msj: String){
            Toast.makeText(context,msj,Toast.LENGTH_LONG).show()
        }
    }


}