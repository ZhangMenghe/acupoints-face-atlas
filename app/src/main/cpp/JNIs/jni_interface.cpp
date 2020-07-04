#include "jni_interface.h"
#include <jni.h>
#include <string>
using namespace acuf;


namespace {
}
JNI_METHOD(jlong, JNIonCreate)(JNIEnv* env, jclass , jobject asset_manager){
//    new assetLoader(AAssetManager_fromJava(env, asset_manager));
//    manager = new Manager;
//    nativeAddr =  getNativeClassAddr(new vrController());
//    setupShaderContents();
//    return nativeAddr;
}
JNI_METHOD(void, JNIonPause)(JNIEnv* env, jclass){

}
JNI_METHOD(void, JNIonDestroy)(JNIEnv* env, jclass){

}
JNI_METHOD(void, JNIonResume)(JNIEnv* env, jclass, jobject, jobject){

}
JNI_METHOD(void, JNIdrawFrame)(JNIEnv*, jclass){

}
JNI_METHOD(void, JNIonGLSurfaceCreated)(JNIEnv * env, jclass){

}
JNI_METHOD(void, JNIonSurfaceChanged)(JNIEnv * env, jclass, jint, jint, jint){

}