#include "jni_interface.h"
#include <jni.h>
#include <string>
using namespace acuf;

#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing.h>
#include <dlib/image_io.h>
namespace {
    dlib::shape_predictor sFaceLandmarksDetector;
    dlib::frontal_face_detector sFaceDetector;
}
JNI_METHOD(jlong, JNIonCreate)(JNIEnv* env, jclass , jobject asset_manager){
//    dlib::deserialize(path) >> sFaceLandmarksDetector;

}