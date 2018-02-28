package com.oreisoft.beengine

import android.app.Activity
import android.content.Intent
import android.view.View

/**
 * Created by sunyuli on 2/28/2018 AD.
 */

fun View.hideSystemUI() {
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

fun Activity.killApp() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
    finish()
    System.gc()
    System.exit(0)
}

fun Activity.pushToBackground() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}