#ifndef ACUPOINTS_FACE_ATLAS_JNI_MAIN_H
#define ACUPOINTS_FACE_ATLAS_JNI_MAIN_H

#include <jni.h>
#include <android/log.h>

#define JNI_METHOD(returnType, funcName)\
    JNIEXPORT returnType JNICALL        \
        Java_acu_face_JNIInterface_##funcName
#define DLIB_METHOD(returnType, funcName)\
    JNIEXPORT returnType JNICALL        \
        Java_acu_face_Detection_DLibLandmarks68Detector_##funcName
#define TAG "ANDROID-HELPER"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)

namespace acuf{
    inline static JavaVM *g_vm = nullptr;
    inline jlong nativeAddr;

//    inline jlong getNativeClassAddr(nEntrance * native_controller){
//        return reinterpret_cast<intptr_t>(native_controller);
//    }
//    inline nEntrance * nativeApp(jlong ptr){
//        return reinterpret_cast<nEntrance *>(ptr);
//    }
//    inline std::string jstring2string(JNIEnv *env, jstring jStr){
//        const char *cstr = env->GetStringUTFChars(jStr, NULL);
//        std::string str = std::string(cstr);
//        env->ReleaseStringUTFChars(jStr, cstr);
//        return str;
//    }
};
#endif //ACUPOINTS_FACE_ATLAS_JNI_MAIN_H
