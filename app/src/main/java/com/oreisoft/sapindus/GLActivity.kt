package com.oreisoft.sapindus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.oreisoft.beengine.*

/**
 * Created by sunyuli on 2/28/2018 AD.
 */
class GLActivity : Activity(), BEView.BEViewDelegate {
    var beview: BEView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        beview = BEView(this)
        beview?.let {
            setContentView(it)
            it.delegate = this
            BEGameController.shared()?.renderer = it.renderer
        }
    }

    override fun onBERendererPrepared(renderer: BERenderer) {
        beview?.let {
            print("MainActivity - onBERendererPrepared")
            //it.hideSystemUI()
            it.renderer?.addModelNamed("maya-blender.obj")
        }
    }

    var zoomLock = false
    override fun beforeDrawFrame() {
        BEGameController.shared()?.let {
            //if (it.leftStickPressed) {
                it.renderer?.rotateModelByPanGesture(it.leftStickX, it.rightStickY)
            //}
            if (it.leftShouderPressed) {
                zoomLock = true
            }
            if (it.rightShouderPressed) {
                zoomLock = false
            }
            if (zoomLock == false) {
                var camDisScale = it.rightTriggerValue.toDouble() * 2.1
                BENativeRenderer.cameraDistanceScale(camDisScale, 0.4, 2.0)
            }
        }
    }

    override fun afterDrawFrame() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.killApp()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        BEGameController.shared()?.let {
            return it.handleKeyDown(keyCode, event)
        }
        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        BEGameController.shared()?.let {
            return it.handleKeyUp(keyCode, event)
        }
        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        BEGameController.shared()?.let {
            return it.handleGenericMotionEvent(event)
        }
        return true
    }
}