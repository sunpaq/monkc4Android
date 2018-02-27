package com.oreisoft.sapindus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.oreisoft.beengine.BERenderer
import com.oreisoft.beengine.BEResource
import com.oreisoft.beengine.BEView

/**
 * Created by sunyuli on 2/28/2018 AD.
 */
class GLActivity : Activity(), BEView.BEViewDelegate {
    var beview: BEView? = null

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        beview = BEView(this)
        beview?.let {
            setContentView(it)
            it.delegate = this
        }
    }

    override fun onBERendererPrepared(renderer: BERenderer) {
        beview?.let {
            print("MainActivity - onBERendererPrepared")
            //it.hideSystemUI()
            it.renderer?.addModelNamed("maya-blender.obj")
        }
    }

    override fun beforeDrawFrame() {

    }

    override fun afterDrawFrame() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
        System.gc()
        System.exit(0)
    }
}