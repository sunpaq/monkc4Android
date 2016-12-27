package com.oreisoft.sapindus;

import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.GLES10;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sunyuli on 2016/12/11.
 */

public class BEEngineView extends GLSurfaceView {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native String nativeRun();
    public static native void init();
    public static native void openFile();
    public static native void resize(int width, int height);
    public static native void step();
    public static native void setAssetManager(Object man);
    public static native void onGestureScale(float scale);
    public static native void onGestureScroll(double x, double y);

    /*
    * Java Parts
    * */

    private static final String TAG = "BEEngineView";
    private static final boolean DEBUG = true;

    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mScrollDetector;
    private float mScaleFactor = 1.f;

    private Context sharedContext = null;

    public BEEngineView(Context context) {
        super(context);
        sharedContext = context;
        // Pick an EGLConfig with RGBA8 color, 24-bit depth, no stencil,
        // supporting OpenGL ES 3.0 or later backwards-compatible versions.
        setEGLContextClientVersion(3);
        setEGLConfigChooser(8, 8, 8, 8, 24, 0);
        setRenderer(new Renderer(context));
        //setRenderMode(RENDERMODE_CONTINUOUSLY);//RENDERMODE_CONTINUOUSLY

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mScrollDetector = new GestureDetector(context, new ScrollListener());
    }

    public BEEngineView(Context context, AttributeSet attrs) {
        this(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleDetector.onTouchEvent(e);
        mScrollDetector.onTouchEvent(e);
        onGestureScale(mScaleFactor);
        invalidate();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAssetManager(sharedContext.getApplicationContext().getAssets());
        //openFile();
    }

    private static class Renderer implements GLSurfaceView.Renderer {
        private Context context;

        public Renderer(Context context) {
            this.context = context;
        }

        public void onDrawFrame(GL10 gl) {
            step();
            gl.glFlush();
        }

        @Override
        public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eglConfig) {
            setAssetManager(context.getApplicationContext().getAssets());
            init();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.v(TAG, "resize to " + width + "x" + height);
            resize(width, height);
            openFile();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();
            return true;
        }
    }

    private class ScrollListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            onGestureScroll(distanceX, distanceY);
            return true;
        }
    }


}
