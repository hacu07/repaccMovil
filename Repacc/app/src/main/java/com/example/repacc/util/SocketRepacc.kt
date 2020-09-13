package com.example.repacc.util

import android.app.Activity
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

// Socket a usar a nivel global en la app
object SocketRepacc {
    var mSocket: Socket? = null

    fun init(){
        mSocket = IO.socket(Util.URL_SOCKET)
    }

    fun connectListener(listener: Emitter.Listener) {
        mSocket?.on(Util.NEW_SOCKET_CONNECTION, listener)
    }

    fun notificationListeners(listener: Emitter.Listener, socketId: String){
        mSocket?.on(socketId, listener)
    }
}