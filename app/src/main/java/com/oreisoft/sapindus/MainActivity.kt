package com.oreisoft.sapindus

import android.os.Bundle
import com.oreisoft.beengine.BEEngineView
import com.oreisoft.beengine.BEGLActivity

class MainActivity : BEGLActivity() {

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        beview = BEEngineView(applicationContext)
        beview?.let {
            setContentView(it)
        }
    }

    override fun onBEEnginePrepared() {
        beview?.let {
            it.loadModel("Avent.obj")
            //it.hideSystemUI()
        }
    }

}
