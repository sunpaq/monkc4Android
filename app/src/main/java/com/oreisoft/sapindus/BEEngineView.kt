package com.oreisoft.sapindus

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector

import javax.microedition.khronos.opengles.GL10

/**
 * Created by sunyuli on 2016/12/11.
 */

class BEEngineView(context: Context) : GLSurfaceView(context) {

    private val mScaleDetector: ScaleGestureDetector
    private val mScrollDetector: GestureDetector
    private var mScaleFactor = 1f

    private var sharedContext: Context? = null

    init {
        sharedContext = context
        // Pick an EGLConfig with RGBA8 color, 24-bit depth, no stencil,
        // supporting OpenGL ES 3.0 or later backwards-compatible versions.
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 24, 0)
        setRenderer(Renderer(context))
        //setRenderMode(RENDERMODE_CONTINUOUSLY);//RENDERMODE_CONTINUOUSLY

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mScrollDetector = GestureDetector(context, ScrollListener())
    }

    constructor(context: Context, attrs: AttributeSet) : this(context) {}

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(e)
        mScrollDetector.onTouchEvent(e)
        BERenderer.onGestureScale(mScaleFactor)
        invalidate()
        return true
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        BERenderer.setAssetManager(sharedContext!!.applicationContext.assets)
        //openFile();
    }

    private class Renderer(private val context: Context) : GLSurfaceView.Renderer {

        override fun onDrawFrame(gl: GL10) {
            BERenderer.render()
            gl.glFlush()
        }

        override fun onSurfaceCreated(gl10: GL10, eglConfig: javax.microedition.khronos.egl.EGLConfig) {
            BERenderer.setAssetManager(context.applicationContext.assets)
            BERenderer.init()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            Log.v(TAG, "resize to " + width + "x" + height)
            BERenderer.resize(width, height)
            BERenderer.openFile()
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor = detector.scaleFactor
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            BERenderer.onGestureScroll(distanceX.toDouble(), distanceY.toDouble())
            return true
        }
    }

    companion object {


        /*
    * Java Parts
    * */

        private val TAG = "BEEngineView"
        private val DEBUG = true
    }


}
