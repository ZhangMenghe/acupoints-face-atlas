#include "atlasManager.h"

atlasManager* atlasManager::_myPtr = nullptr;
atlasManager *atlasManager::instance() {
    if(!_myPtr) _myPtr = new atlasManager;
    return _myPtr;
}

atlasManager::atlasManager(){

}

void atlasManager::onViewCreated(){

}
void atlasManager::onViewChange(int rot, int width, int height){

}
void atlasManager::onDraw(){

}
void atlasManager::onReset(){

}