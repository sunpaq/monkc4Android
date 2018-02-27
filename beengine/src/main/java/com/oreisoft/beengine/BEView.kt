package com.oreisoft.beengine

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.*

import javax.microedition.khronos.opengles.GL10

/**
 * Created by sunyuli on 2016/12/11.
 */

class BEView(context: Context) : GLSurfaceView(context), BERenderer.BERendererDelegate {

    interface BEViewDelegate {
        fun onBERendererPrepared(renderer: BERenderer);
        fun beforeDrawFrame();
        fun afterDrawFrame();
    }

    private val TAG = "BEView"
    private val DEBUG = true

    private val mScaleDetector: ScaleGestureDetector
    private val mScrollDetector: GestureDetector
    private var mScaleFactor = 1f

    var delegate: BEViewDelegate? = null
    var renderer: BERenderer? = null

    init {
        if (context.applicationContext.assets != null) {
            BENativeRenderer.setAssetManager(context.applicationContext.assets)
        } else {
            error("BEView - context.applicationContext.assets is null can not setAssetManager")
        }

        // Pick an EGLConfig with RGBA8 color, 24-bit depth, no stencil,
        // supporting OpenGL ES 3.0 or later backwards-compatible versions.
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 24, 0)
        //setRenderMode(RENDERMODE_CONTINUOUSLY);//RENDERMODE_CONTINUOUSLY

        renderer = BERenderer(context)
        renderer?.delegate = this
        setRenderer(renderer)

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mScrollDetector = GestureDetector(context, ScrollListener())
    }

    constructor(context: Context, attrs: AttributeSet) : this(context) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        super.surfaceCreated(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, w: Int, h: Int) {
        super.surfaceChanged(holder, format, w, h)
        renderer?.resizeAllScene(Size(w,h))
        Log.v(TAG, "resize to " + w + "x" + h)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        delegate?.onBERendererPrepared(renderer!!)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(e)
        mScrollDetector.onTouchEvent(e)
        BENativeRenderer.onGestureScale(mScaleFactor)
        invalidate()
        return true
    }

    override fun willDrawFrame() {

    }

    override fun didDrawFrame() {

    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor = detector.scaleFactor
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            BENativeRenderer.onGestureScroll(distanceX.toDouble(), distanceY.toDouble())
            return true
        }
    }

    fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}
