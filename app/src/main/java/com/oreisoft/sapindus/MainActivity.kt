package com.oreisoft.sapindus

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.oreisoft.beengine.BEResource
import kotlinx.coroutines.experimental.async

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
        if (hasFocus) {
            BEResource.Companion.shared(this)?.preloadTextures("jpg")

            findViewById(R.id.loading_status).visibility = View.INVISIBLE
            findViewById(R.id.start_button).visibility = View.VISIBLE
        }
    }

    fun onButtonClicked(view: View) {
        startActivity(Intent(this, GLActivity::class.java))
    }

}
