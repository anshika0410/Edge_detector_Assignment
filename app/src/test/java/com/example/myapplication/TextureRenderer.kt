package com.example.myapplication

import android.content.Context
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TextureRenderer(private val context: Context) {

    // ... (all the properties like vertexShaderCode, program, etc., remain the same) ...
    private val vertexShaderCode = readShaderFromRawResource(R.raw.vertex_shader)
    private val fragmentShaderCode = readShaderFromRawResource(R.raw.fragment_shader)
    private var program: Int = 0
    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var texCoordBuffer: FloatBuffer
    private var positionHandle: Int = 0
    private var texCoordHandle: Int = 0
    private val vertices = floatArrayOf(-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f)
    private val textureCoords = floatArrayOf(0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f)


    init {
        setupBuffers()
        setupProgram()
    }

    fun draw() { // The textureId parameter is removed
        GLES20.glUseProgram(program)

        // The texture is already bound by our renderer, so we just need to draw it.

        vertexBuffer.position(0)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glEnableVertexAttribArray(positionHandle)

        texCoordBuffer.position(0)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    // ... (The rest of the file: setupBuffers, setupProgram, loadShader, readShaderFromRawResource remains exactly the same) ...
    private fun setupBuffers() {
        var buffer = ByteBuffer.allocateDirect(vertices.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        vertexBuffer = buffer.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)

        buffer = ByteBuffer.allocateDirect(textureCoords.size * 4)
        buffer.order(ByteOrder.nativeOrder())
        texCoordBuffer = buffer.asFloatBuffer()
        texCoordBuffer.put(textureCoords)
        texCoordBuffer.position(0)
    }

    private fun setupProgram() {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        positionHandle = GLES20.glGetAttribLocation(program, "a_Position")
        texCoordHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate")
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    private fun readShaderFromRawResource(resourceId: Int): String {
        val inputStream = context.resources.openRawResource(resourceId)
        val bufferedReader = inputStream.bufferedReader()
        return bufferedReader.use { it.readText() }
    }
}