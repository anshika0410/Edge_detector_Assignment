#include <jni.h>
#include <string>
#include <android/log.h>
#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <android/bitmap.h>

extern "C" JNIEXPORT void JNICALL
Java_com_example_myapplication_MainActivity_processFrame(
        JNIEnv* env,
        jobject /* this */,
        jobject bitmap,
        jboolean use_canny) { // New boolean parameter

    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    void* pixels;
    AndroidBitmap_lockPixels(env, bitmap, &pixels);

    cv::Mat image(info.height, info.width, CV_8UC4, pixels);

    if (use_canny) {
        // Canny Edge Detection Path
        cv::Mat gray;
        cv::Mat edges;
        cv::cvtColor(image, gray, cv::COLOR_RGBA2GRAY);
        cv::Canny(gray, edges, 50.0, 150.0);
        cv::cvtColor(edges, image, cv::COLOR_GRAY2RGBA);
    } else {
        // Grayscale Path
        cv::cvtColor(image, image, cv::COLOR_RGBA2GRAY);
        cv::cvtColor(image, image, cv::COLOR_GRAY2RGBA);
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}