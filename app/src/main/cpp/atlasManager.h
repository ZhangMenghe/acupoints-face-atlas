#ifndef ACUPOINTS_FACE_ATLAS_ATLASMANAGER_H
#define ACUPOINTS_FACE_ATLAS_ATLASMANAGER_H

class atlasManager {
public:
    static atlasManager * instance();
    atlasManager();

    void onViewCreated();
    void onViewChange(int rot, int width, int height);
    void onDraw();
    void onReset();
//    void onDestroy();
//    void onPause();
//    void onResume(void* env, void* context, void* activity);
private:
    static atlasManager* _myPtr;

};


#endif //ACUPOINTS_FACE_ATLAS_ATLASMANAGER_H
