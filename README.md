# **Real-Time Edge Detection Viewer for Android**

This project is a solution for the Software Engineering Intern (R\&D) technical assessment. It's an Android application that captures a live camera feed, processes each frame in real-time using native C++ with OpenCV, and renders the output efficiently using OpenGL ES 2.0. The app features both Grayscale and Canny Edge Detection filters that can be toggled by the user.

Additionally, a minimal web viewer built with TypeScript is included to demonstrate the ability to display processed output in a web environment.

## **Features Implemented**

### **Android Application**

* **Real-Time Camera Capture:** Utilizes Android's CameraX library for a high-performance, lifecycle-aware camera stream.  
* **Native C++ Processing:** All image processing is offloaded to a native C++ layer via JNI for maximum performance.  
* **OpenCV Integration:** Leverages the powerful OpenCV library for image filtering.  
* **Dual Filter System:**  
  * Canny Edge Detection  
  * Grayscale Conversion  
* **Interactive UI:** A simple button allows the user to toggle between the Canny and Grayscale filters in real-time.  
* **High-Performance Rendering:** Renders the processed video stream using OpenGL ES 2.0, uploading frames directly from C++ to GPU textures for efficiency.  
* **Correct Orientation:** Automatically handles device rotation to ensure the processed video is always displayed correctly.

### **Web Viewer**

* **TypeScript Project:** A clean, separate web project built with TypeScript.  
* **Static Frame Display:** Displays a static, pre-processed sample frame from the Android application.  
* **DOM Manipulation:** Demonstrates basic DOM manipulation to display the image and simulated frame statistics.

## **Working Demo**

Here are screenshots of the application in action.

Canny Edge Detection Filter:  
![WhatsApp Image 2025-10-09 at 01 14 00_c9e9e882](https://github.com/user-attachments/assets/1b4cb7d1-9619-4696-bfe3-d93abfb62c28)

Grayscale Filter:  
![WhatsApp Image 2025-10-09 at 01 14 00_d136c797](https://github.com/user-attachments/assets/634dbda5-c7c8-4697-8303-71f40f5e4ca5)


## **Architecture Overview**

The application follows a modular architecture designed for performance by separating concerns.

1. **Camera (Kotlin):** The MainActivity uses Android's CameraX ImageAnalysis use case to capture frames on a background thread.  
2. **Frame Preparation (Kotlin):** For each frame, the app gets the current device rotation and creates a correctly rotated Bitmap.  
3. **JNI Bridge:** The rotated Bitmap is passed to the MyGLRenderer class, which then calls a native C++ function (nativeProcessFrame) via the Java Native Interface (JNI). It also passes the active filter choice (Canny/Grayscale) and the ID of an OpenGL texture.  
4. **Processing (C++ & OpenCV):** The C++ layer receives the bitmap, wraps its pixel data in an OpenCV Mat, and applies the selected filter.  
5. **Rendering (C++ & OpenGL ES):** After processing, the C++ code uploads the pixel data from the final Mat directly into the pre-generated OpenGL texture using glTexSubImage2D.  
6. **Display (Kotlin & OpenGL ES):** The GLSurfaceView's render loop then draws a simple quad to the screen, textured with the updated image, using custom GLSL shaders.

This pipeline ensures that heavy processing occurs on a background thread and in native code, while the GPU handles all the rendering, keeping the main UI thread free and responsive.

## **Tech Stack**

* **Languages:** Kotlin, C++, GLSL, TypeScript, HTML  
* **Android:** Android SDK, CameraX, Android NDK  
* **Graphics:** OpenGL ES 2.0  
* **Image Processing:** OpenCV 4.9.0  
* **Web:** Node.js, npm, TypeScript

## **Setup and Installation**

To build and run this project, follow these steps:

1. **Clone the Repository:**  
   git clone https://github.com/anshika0410/Edge_detector_Assignment

2. **Download OpenCV for Android:**  
   * Download the **OpenCV Android SDK (version 4.9.0 is recommended)** from the official [OpenCV releases page](https://github.com/opencv/opencv/releases).  
   * Unzip the file to a known location on your computer.  
3. **Configure the OpenCV Path:**  
   * Open the project in Android Studio.  
   * Navigate to the file: app/src/main/cpp/CMakeLists.txt.  
   * **Crucial Step:** You must update the OpenCV\_DIR variable to point to the sdk/native/jni folder inside the OpenCV SDK you just downloaded. Use forward slashes / for the path.

\# Example path on Windows:  
set(OpenCV\_DIR "C:/Users/YourUser/Downloads/opencv-4.9.0-android-sdk/sdk/native/jni")

4. **Build and Run:**  
   * Android Studio will prompt you to sync the Gradle project. Click "Sync Now".  
   * Connect an Android device with USB debugging enabled.  
   * Click the "Run 'app'" button in Android Studio.

## **Running the Web Viewer**

1. **Navigate to the web Directory:**  
   cd web

2. **Install Dependencies:**  
   npm install

3. **Compile TypeScript:**  
   npx tsc

4. **View in Browser:**  
   * Open the index.html file in your web browser to see the static viewer.
