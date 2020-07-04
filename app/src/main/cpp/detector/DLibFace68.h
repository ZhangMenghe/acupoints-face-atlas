
#ifndef ACUPOINTS_FACE_ATLAS_DLIBFACE68_H
#define ACUPOINTS_FACE_ATLAS_DLIBFACE68_H


#include <proto/messages.pb.h>

class DLibFace68 {
public:
    DLibFace68(acu::Face raw_face){
        mBound = raw_face.bound();
        for(int i=0; i<raw_face.landmarks_size();i++) mLandmarks.push_back(raw_face.landmarks(i));
    }
    DLibFace68(std::vector<acu::Landmark> face_landmarks){

    }

    //getter
    acu::RectF getBound(){return mBound;}
    std::vector<acu::Landmark> getAllLandmarks(){return mLandmarks;}

private:
    enum FACE_LANDMARK{
        CHIN_START = 0,
        CHIN_END = 16,
        EYEBROW_L_START = 17,
        EYEBROW_L_END = 21,
        EYEBROW_R_START = 22,
        EYEBROW_R_END = 26,

        NOSE_START = 28,
        NOSE_END = 35,

        EYE_L_START = 36,
        EYE_L_END = 41,
        EYE_R_START = 42,
        EYE_R_END = 47,

        LIPS_OUTER_START = 48,
        LIPS_OUTER_END = 59,
        LIPS_INNER_START = 60,
        LIPS_INNER_END = 67
    };
    acu::RectF mBound;
    std::vector<acu::Landmark> mLandmarks;

};


#endif //ACUPOINTS_FACE_ATLAS_DLIBFACE68_H
