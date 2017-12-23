package com.oreisoft.sapindus

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    internal var mView: BEEngineView? = null

    // This snippet hides the system bars.
    private fun hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)



        mView = BEEngineView(applicationContext)
        setContentView(mView)

        //setContentView(R.layout.activity_main);
        //mView = (BEEngineView) findViewById(R.id.gl_container);
    }

    override fun onNewIntent(i: Intent) {
        super.onNewIntent(i)
    }

    override fun onPause() {
        //workaround for save GL context when pause/resume
        mView!!.visibility = View.GONE
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        //hideSystemUI();
        mView!!.onResume()
    }

    //workaround for save GL context when pause/resume
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && mView!!.visibility == View.GONE) {
            mView!!.visibility = View.VISIBLE
        }
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mView!!.onTouchEvent(e)
        return true
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
