package com.oreisoft.sapindus

import android.app.Activity
import android.os.Bundle
import com.oreisoft.beengine.BEView
import com.oreisoft.beengine.BERenderer

class MainActivity : Activity(), BEView.BEViewDelegate {

    var beview: BEView? = null

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        beview = BEView(applicationContext)
        beview?.let {
            setContentView(it)
            it.delegate = this
        }
    }

    override fun onBERendererPrepared(renderer: BERenderer) {
        beview?.let {
            print("MainActivity - onBERendererPrepared")
            //it.hideSystemUI()
            it.renderer?.addModelNamed("2.obj")
        }
    }

    override fun beforeDrawFrame() {

    }

    override fun afterDrawFrame() {

    }
}
