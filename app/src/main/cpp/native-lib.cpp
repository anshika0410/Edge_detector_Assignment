#include <jni.h>
#include <android/bitmap.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <GLES2/gl2.h>

// Flag to track if the texture has been initialized
static bool isTextureInitialized = false;

extern "C" JNIEXPORT void JNICALL
Java_com_example_myapplication_MyGLRenderer_nativeProcessFrame(
        JNIEnv* env,
        jobject /* this */,
        jobject bitmap,
        jboolean use_canny,
        jint texture_id) {

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    void* pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);

    cv::Mat image(info.height, info.width, CV_8UC4, pixels);
    cv::Mat processedImage;

    if (use_canny) {
        cv::Mat gray, edges;
        cv::cvtColor(image, gray, cv::COLOR_RGBA2GRAY);
        cv::Canny(gray, edges, 50.0, 150.0);
        cv::cvtColor(edges, processedImage, cv::COLOR_GRAY2RGBA);
    } else {
        cv::Mat gray;
        cv::cvtColor(image, gray, cv::COLOR_RGBA2GRAY);
        cv::cvtColor(gray, processedImage, cv::COLOR_GRAY2RGBA);
    }

    // --- THE FIX IS HERE ---
    glBindTexture(GL_TEXTURE_2D, texture_id);

    if (!isTextureInitialized) {
        // If this is the first frame, define the texture's size and format.
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, processedImage.cols, processedImage.rows, 0, GL_RGBA, GL_UNSIGNED_BYTE, processedImage.ptr());
        isTextureInitialized = true;
    } else {
        // For all subsequent frames, just update the texture's content.
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, processedImage.cols, processedImage.rows, GL_RGBA, GL_UNSIGNED_BYTE, processedImage.ptr());
    }
    // --- END OF FIX ---

    AndroidBitmap_unlockPixels(env, bitmap);
}