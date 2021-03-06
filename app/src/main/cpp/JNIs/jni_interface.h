#ifndef ACUPOINTS_FACE_ATLAS_JNI_INTERFACE_H
#define ACUPOINTS_FACE_ATLAS_JNI_INTERFACE_H
#include "jni_main.h"
extern "C" {
    JNI_METHOD(jlong, JNIonCreate)(JNIEnv* env, jclass , jobject asset_manager);
    JNI_METHOD(void, JNIonPause)(JNIEnv* env, jclass);
    JNI_METHOD(void, JNIonDestroy)(JNIEnv* env, jclass);
    JNI_METHOD(void, JNIonResume)(JNIEnv* env, jclass, jobject, jobject);
    JNI_METHOD(void, JNIdrawFrame)(JNIEnv*, jclass);
    JNI_METHOD(void, JNIonGLSurfaceCreated)(JNIEnv * env, jclass);
    JNI_METHOD(void, JNIonSurfaceChanged)(JNIEnv * env, jclass, jint, jint, jint);
}
#endif
