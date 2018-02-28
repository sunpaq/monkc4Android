package com.oreisoft.sapindus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.oreisoft.beengine.BEResource
import org.jetbrains.anko.AnkoAsyncContext
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.ref.WeakReference

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.loading_status).visibility = View.VISIBLE
        findViewById(R.id.start_button).visibility = View.INVISIBLE
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val ctx = this
        doAsync {
            BEResource.Companion.shared(ctx)?.preloadTextures("jpg")
            uiThread {
                findViewById(R.id.loading_status).visibility = View.INVISIBLE
                findViewById(R.id.start_button).visibility = View.VISIBLE
            }
        }
    }

    fun onButtonClicked(view: View) {
        val intent = Intent(this, GLActivity::class.java)
        startActivity(intent)
    }

}

