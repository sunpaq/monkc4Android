package com.oreisoft.beengine

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.*

import javax.microedition.khronos.opengles.GL10
import android.view.KeyEvent.KEYCODE_BUTTON_A
import android.view.KeyEvent.KEYCODE_DPAD_CENTER
import org.xml.sax.InputSource


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
    var controller: BEGameController? = null

    init {


        // Pick an EGLConfig with RGBA8 color, 24-bit depth, no stencil,
        // supporting OpenGL ES 3.0 or later backwards-compatible versions.
        setEGLContextClientVersion(3)
        setEGLConfigChooser(8, 8, 8, 8, 24, 0)
        //setRenderMode(RENDERMODE_CONTINUOUSLY);//RENDERMODE_CONTINUOUSLY

        renderer = BERenderer(context)
        renderer?.delegate = this
        controller = BEGameController()
        controller?.renderer = renderer
        setRenderer(renderer)

        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mScrollDetector = GestureDetector(context, ScrollListener())
    }

    constructor(context: Context, attrs: AttributeSet) : this(context) {}

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        delegate?.onBERendererPrepared(renderer!!)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mScaleDetector.onTouchEvent(e)
        mScrollDetector.onTouchEvent(e)
        invalidate()
        return true
    }

    override fun willDrawFrame() {
        delegate?.beforeDrawFrame()
    }

    override fun didDrawFrame() {
        delegate?.afterDrawFrame()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor = detector.scaleFactor
            BENativeRenderer.onGestureScale(mScaleFactor)
            return true
        }
    }

    private inner class ScrollListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val factor = -0.1
            BENativeRenderer.onGestureScroll(distanceX.toDouble() * factor,distanceY.toDouble() * factor)
            return true
        }
    }
}
