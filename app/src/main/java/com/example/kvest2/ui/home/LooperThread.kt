package com.example.kvest2.ui.home

import android.os.Looper
import android.os.Message
import java.util.logging.Handler
import java.util.logging.LogRecord

class LooperThread:Thread() {
    var mHandler: Handler? = null

    override fun run() {
        Looper.prepare()
        mHandler = object : Handler() {
            fun handleMessage(msg: Message?) {
                // process incoming messages here
            }

            override fun publish(p0: LogRecord?) {
                TODO("Not yet implemented")
            }

            override fun flush() {
                TODO("Not yet implemented")
            }

            override fun close() {
                TODO("Not yet implemented")
            }
        }
        Looper.loop()
    }
}