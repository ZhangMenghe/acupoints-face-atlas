#ifndef ACUPOINTS_FACE_ATLAS_JNI_INTERFACE_H
#define ACUPOINTS_FACE_ATLAS_JNI_INTERFACE_H
#include "jni_main.h"
extern "C" {
    JNI_METHOD(jlong, JNIonCreate)(JNIEnv* env, jclass , jobject asset_manager);
}
#endif
