#include "dlib_face_landmark.h"
#include <string>
using namespace acuf;
#include <android/bitmap.h>
#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing.h>
#include <dlib/image_io.h>
#include <proto/messages.pb.h>
using namespace acu;
namespace {
    dlib::shape_predictor sFaceLandmarksDetector;
    dlib::frontal_face_detector sFaceDetector;
    void convertBitmapToArray2d(JNIEnv* env,
                                jobject bitmap,
                                dlib::array2d<dlib::rgb_pixel>& out) {
        AndroidBitmapInfo bitmapInfo;
        void* pixels;
        int state;

        if (0 > (state = AndroidBitmap_getInfo(env, bitmap, &bitmapInfo))) {
            LOGI("L%d: AndroidBitmap_getInfo() failed! error=%d", __LINE__, state);
            LOGE("AndroidBitmap_getInfo() failed!");
            return;
        } else if (bitmapInfo.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
            LOGI("L%d: Bitmap format is not RGB_565!", __LINE__);
            LOGE("Bitmap format is not RGB_565!");
        }

        // Lock the bitmap for copying the pixels safely.
        if (0 > (state = AndroidBitmap_lockPixels(env, bitmap, &pixels))) {
            LOGI("L%d: AndroidBitmap_lockPixels() failed! error=%d", __LINE__, state);
            LOGE( "AndroidBitmap_lockPixels() failed!");
            return;
        }

        LOGI("L%d: info.width=%d, info.height=%d", __LINE__, bitmapInfo.width, bitmapInfo.height);
        out.set_size((long) bitmapInfo.height, (long) bitmapInfo.width);

        char* line = (char*) pixels;
        for (int h = 0; h < bitmapInfo.height; ++h) {
            for (int w = 0; w < bitmapInfo.width; ++w) {
                uint32_t* color = (uint32_t*) (line + 4 * w);

                out[h][w].red = (unsigned char) (0xFF & ((*color) >> 24));
                out[h][w].green = (unsigned char) (0xFF & ((*color) >> 16));
                out[h][w].blue = (unsigned char) (0xFF & ((*color) >> 8));
            }

            line = line + bitmapInfo.stride;
        }

        // Unlock the bitmap.
        AndroidBitmap_unlockPixels(env, bitmap);
    }

}
DLIB_METHOD(jboolean, isFaceDetectorReady)(JNIEnv* env, jclass){
    return (sFaceDetector.num_detectors() > 0)? JNI_TRUE:JNI_FALSE;
}
DLIB_METHOD(jboolean, isFaceLandmarksDetectorReady)(JNIEnv* env, jclass){
    return (sFaceLandmarksDetector.num_parts() > 0)? JNI_TRUE:JNI_FALSE;
}
DLIB_METHOD(void, prepareFaceDetector)(JNIEnv* env, jclass){
    sFaceDetector = dlib::get_frontal_face_detector();
}
DLIB_METHOD(void, prepareFaceLandmarksDetector)(JNIEnv* env, jclass, jstring detectorPath){
    const char *path = env->GetStringUTFChars(detectorPath, JNI_FALSE);
    dlib::deserialize(path) >> sFaceLandmarksDetector;
    env->ReleaseStringUTFChars(detectorPath, path);
    if (sFaceLandmarksDetector.num_parts() != 68)
        LOGE("===It's not a 68 landmarks detector!");
}

DLIB_METHOD(jbyteArray, detectFaces)(JNIEnv* env, jclass, jobject bitmap){
    // Convert bitmap to dlib::array2d.
    dlib::array2d<dlib::rgb_pixel> img;
    convertBitmapToArray2d(env, bitmap, img);
    const long width = img.nc();
    const long height = img.nr();
    LOGI("===L%d: input image (w=%ld, h=%ld) is read", width, height);


    // Now tell the face detector to give us a list of bounding boxes
    // around all the faces in the image.
    std::vector<dlib::rectangle> dets = sFaceDetector(img);
    LOGI("===L%d: Number of faces detected: %d",dets.size());

    // To protobuf message.
    FaceList faces;
    for (unsigned long i = 0; i < dets.size(); ++i) {
        // Profiler.

        dlib::rectangle& det = dets.at(i);

        Face* face = faces.add_faces();
        RectF* bound = face->mutable_bound();

        bound->set_left((float) det.left() / width);
        bound->set_top((float) det.top() / height);
        bound->set_right((float) det.right() / width);
        bound->set_bottom((float) det.bottom() / height);

        LOGI("===L%d: Convert face #%lu to protobuf message",i);
    }

    // Prepare the return message.
    int outSize = faces.ByteSizeLong();
    jbyteArray out = env->NewByteArray(outSize);
    jbyte* buffer = new jbyte[outSize];

    faces.SerializeToArray(buffer, outSize);
    env->SetByteArrayRegion(out, 0, outSize, buffer);
    delete[] buffer;
    return out;
}
DLIB_METHOD(jbyteArray, detectLandmarksFromFace)(JNIEnv* env, jclass, jobject bitmap, jint left, jint top, jint right, jint bottom){
    // Convert bitmap to dlib::array2d.
    dlib::array2d<dlib::rgb_pixel> img;
    convertBitmapToArray2d(env, bitmap, img);

    const long width = img.nc();
    const long height = img.nr();

    // Detect landmarks.
    dlib::rectangle bound(left, top, right, bottom);
    dlib::full_object_detection shape = sFaceLandmarksDetector(img, bound);

    // Protobuf message.
    LandmarkList landmarks;
    // You get the idea, you can get all the face part locations if
    // you want them.  Here we just store them in shapes so we can
    // put them on the screen.
    for (unsigned long i = 0 ; i < shape.num_parts(); ++i) {
        dlib::point& pt = shape.part(i);

        Landmark* landmark = landmarks.add_landmarks();
        landmark->set_x((float) pt.x() / width);
        landmark->set_y((float) pt.y() / height);
    }

    // TODO: Make a JNI function to convert a message to byte[] living in
    // TODO: lib-protobuf project.
    // Prepare the return message.
    int outSize = landmarks.ByteSizeLong();
    jbyteArray out = env->NewByteArray(outSize);
    jbyte* buffer = new jbyte[outSize];

    landmarks.SerializeToArray(buffer, outSize);
    env->SetByteArrayRegion(out, 0, outSize, buffer);
    delete[] buffer;

    return out;
}
DLIB_METHOD(jbyteArray, detectLandmarksFromFaces)(JNIEnv* env, jclass, jobject bitmap, jbyteArray faceRects){
    dlib::array2d<dlib::rgb_pixel> img;
    convertBitmapToArray2d(env, bitmap, img);

    const long width = img.nc();
    const long height = img.nr();

    // Translate the input face-rects message into something we recognize here.
    jbyte* pFaceRects = env->GetByteArrayElements(faceRects, NULL);
    jsize pFaceRectsLen = env->GetArrayLength(faceRects);
    RectFList msgBounds;
    msgBounds.ParseFromArray(pFaceRects, pFaceRectsLen);
    env->ReleaseByteArrayElements(faceRects, pFaceRects, 0);
    std::vector<dlib::rectangle> bounds;
    for (int i = 0; i < msgBounds.rects().size(); ++i) {
        const RectF& msgBound = msgBounds.rects().Get(i);
        bounds.push_back(dlib::rectangle((long) msgBound.left(),
                                         (long) msgBound.top(),
                                         (long) msgBound.right(),
                                         (long) msgBound.bottom()));
    }

    // Detect landmarks and return protobuf message.
    FaceList faces;
    for (unsigned long j = 0; j < bounds.size(); ++j) {
        dlib::full_object_detection shape = sFaceLandmarksDetector(img, bounds[j]);

        // To protobuf message.
        Face* face = faces.add_faces();
        // Transfer face boundary.
        RectF* bound = face->mutable_bound();
        bound->set_left((float) bounds[j].left() / width);
        bound->set_top((float) bounds[j].top() / height);
        bound->set_right((float) bounds[j].right() / width);
        bound->set_bottom((float) bounds[j].bottom() / height);
        // Transfer face landmarks.
        for (u_long i = 0 ; i < shape.num_parts(); ++i) {
            dlib::point& pt = shape.part(i);

            Landmark* landmark = face->add_landmarks();
            landmark->set_x((float) pt.x() / width);
            landmark->set_y((float) pt.y() / height);
        }
    }

    // Prepare the return message.
    int outSize = faces.ByteSizeLong();
    jbyteArray out = env->NewByteArray(outSize);
    jbyte* buffer = new jbyte[outSize];

    faces.SerializeToArray(buffer, outSize);
    env->SetByteArrayRegion(out, 0, outSize, buffer);
    delete[] buffer;

    return out;
}
DLIB_METHOD(jbyteArray, detectFacesAndLandmarks)(JNIEnv* env, jclass, jobject bitmap){
    if(sFaceDetector.num_detectors() == 0 || sFaceLandmarksDetector.num_parts()==0) return nullptr;
    dlib::array2d<dlib::rgb_pixel> img;
    convertBitmapToArray2d(env, bitmap, img);

    const float width = (float) img.nc();
    const float height = (float) img.nr();

    // Now tell the face detector to give us a list of bounding boxes
    // around all the faces in the image.
    std::vector<dlib::rectangle> dets = sFaceDetector(img);
    // Protobuf message.
    FaceList faces;
    // Now we will go ask the shape_predictor to tell us the pose of
    // each face we detected.
    for (unsigned long j = 0; j < dets.size(); ++j) {
        dlib::full_object_detection shape = sFaceLandmarksDetector(img, dets[j]);

        // To protobuf message.
        Face* face = faces.add_faces();
        // Transfer face boundary.
        RectF* bound = face->mutable_bound();
        bound->set_left((float) dets[j].left() / width);
        bound->set_top((float) dets[j].top() / height);
        bound->set_right((float) dets[j].right() / width);
        bound->set_bottom((float) dets[j].bottom() / height);
        // Transfer face landmarks.
        for (u_long i = 0 ; i < shape.num_parts(); ++i) {
            dlib::point& pt = shape.part(i);

            Landmark* landmark = face->add_landmarks();
            landmark->set_x((float) pt.x() / width);
            landmark->set_y((float) pt.y() / height);
        }
    }

    // Prepare the return message.
    int outSize = faces.ByteSizeLong();
    jbyteArray out = env->NewByteArray(outSize);
    jbyte* buffer = new jbyte[outSize];

    faces.SerializeToArray(buffer, outSize);
    env->SetByteArrayRegion(out, 0, outSize, buffer);
    delete[] buffer;

    return out;
}