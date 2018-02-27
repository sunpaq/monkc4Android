package com.oreisoft.beengine

import android.content.Context
import android.content.res.AssetManager
import android.util.AttributeSet
import android.util.Log
import java.net.URI
import java.net.URL

/**
 * Created by sunyuli on 2/27/2018 AD.
 */
class BEResource {

    val TAG = "BEResource"

    var objModelNames: Array<String>? = null
    var texturesFolderName: String = "textures"
    var assetManager: AssetManager? = null

    companion object {
        private var instance: BEResource? = null

        fun shared(context: Context) : BEResource? {
            if (instance == null) {
                val am = context.applicationContext.assets
                instance = BEResource()
                instance?.assetManager = am
                BENativeRenderer.setAssetManager(am)
            }
            return instance
        }
    }

    fun prepare() {

    }

    fun detectObjModelNames() {

    }

    fun preloadJPGTextures() {
        preloadTextures("jpg")
    }

    fun preloadTGATextures() {
        preloadTextures("tga")
    }

    fun preloadTextures(extension: String) {
        assetManager?.let {
            val tnames = it.list(texturesFolderName)
            for (name in tnames) {
                if (name.endsWith(extension, true)) {
                    BENativeRenderer.cacheTextureNamed(name)
                    Log.v(TAG, "preload texture: " + name)
                }
            }
        }
    }

}