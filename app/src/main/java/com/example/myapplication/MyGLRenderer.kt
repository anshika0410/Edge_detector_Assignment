package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer(private val context: Context) : GLSurfaceView.Renderer {

    private lateinit var textureRenderer: TextureRenderer
    private var textureId: Int = 0
    private var frameAvailable = false
    private var currentBitmap: Bitmap? = null
    var isCannyFilter = true

    private external fun nativeProcessFrame(bitmap: Bitmap, useCanny: Boolean, textureId: Int)
    init {
        System.loadLibrary("myapplication")
    }

    fun onFrameAvailable(bitmap: Bitmap) {
        synchronized(this) {
            currentBitmap = bitmap
            frameAvailable = true
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        textureRenderer = TextureRenderer(context)
        textureId = createTexture()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        synchronized(this) {
            if (frameAvailable) {
                currentBitmap?.let {
                    nativeProcessFrame(it, isCannyFilter, textureId)
                }
                frameAvailable = false
            }
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        textureRenderer.draw()
    }

    private fun createTexture(): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        val textureId = textures[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        return textureId
    }
}