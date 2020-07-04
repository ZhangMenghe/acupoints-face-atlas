#ifndef ACUPOINTS_FACE_ATLAS_DLIB_FACE_LANDMARK_H
#define ACUPOINTS_FACE_ATLAS_DLIB_FACE_LANDMARK_H

#include "jni_main.h"

extern "C" {
DLIB_METHOD(jboolean, isFaceDetectorReady)(JNIEnv* env, jclass);
DLIB_METHOD(jboolean, isFaceLandmarksDetectorReady)(JNIEnv* env, jclass);
DLIB_METHOD(void, prepareFaceDetector)(JNIEnv* env, jclass);
DLIB_METHOD(void, prepareFaceLandmarksDetector)(JNIEnv* env, jclass, jstring);
DLIB_METHOD(jbyteArray, detectFaces)(JNIEnv* env, jclass, jobject);
DLIB_METHOD(jbyteArray, detectLandmarksFromFace)(JNIEnv* env, jclass, jobject, jint, jint, jint, jint);
DLIB_METHOD(jbyteArray, detectLandmarksFromFaces)(JNIEnv* env, jclass, jobject, jbyteArray);
DLIB_METHOD(jbyteArray, detectFacesAndLandmarks)(JNIEnv* env, jclass, jobject);
}


#endif //ACUPOINTS_FACE_ATLAS_DLIB_FACE_LANDMARK_H
