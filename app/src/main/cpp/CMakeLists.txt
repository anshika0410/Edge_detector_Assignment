cmake_minimum_required(VERSION 3.18.1)

project("myapplication")

# IMPORTANT: CHANGE THIS PATH to where you downloaded the OpenCV Android SDK
# Use forward slashes `/` even on Windows.
set(OpenCV_DIR "C:/Users/stoff/Downloads/opencv-4.9.0-android-sdk/OpenCV-android-sdk/sdk/native/jni")

find_package(OpenCV REQUIRED)

add_library(myapplication SHARED native-lib.cpp)

# Find and link the Android logging and bitmap libraries
find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)
find_library(GLESv2-lib GLESv2)

target_link_libraries(
        myapplication
        ${log-lib}
        ${jnigraphics-lib}
        ${OpenCV_LIBS}
        ${GLESv2-lib} )