cmake_minimum_required(VERSION 3.4.1)
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexceptions -std=c++11")

add_library(native-lib

             SHARED

             JNIs/jni_interface.cpp )


#message(FATAL_ERROR ${DLIB_DIR})
add_library(dlib SHARED IMPORTED)

set_target_properties(
        dlib PROPERTIES IMPORTED_LOCATION
        ${DLIB_DIR}/lib/${ANDROID_ABI}/libdlib.so
        INTERFACE_INCLUDE_DIRECTORIES ${DLIB_DIR}/include
)

target_link_libraries(  native-lib
                        dlib
                        log)




#cmake_minimum_required(VERSION 3.4.1)
##set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fexceptions -std=c++11")
#
#add_library(native-lib
#        SHARED
#        JNIs/jni_interface.cpp
#        proto/messages.pb.cc)
#
##add protobuf
#add_library(protobuf SHARED IMPORTED)
#set_target_properties(
#        protobuf PROPERTIES IMPORTED_LOCATION
#        ${THIRD_PARTY_DIR}/protobuf/lib/${ANDROID_ABI}/libprotobuf-lite-3.2.0.so
#        INTERFACE_INCLUDE_DIRECTORIES ${THIRD_PARTY_DIR}/protobuf/include
#)
#
##add dlib
#add_library(dlib SHARED IMPORTED)
#set_target_properties(
#        dlib PROPERTIES IMPORTED_LOCATION
#        ${THIRD_PARTY_DIR}/dlib/lib/${ANDROID_ABI}/libdlib.so
#        INTERFACE_INCLUDE_DIRECTORIES ${THIRD_PARTY_DIR}/dlib/include
#)
#include_directories(${CMAKE_CURRENT_SOURCE_DIR})
#target_link_libraries(  native-lib
#        dlib
#        protobuf
#        android
#        jnigraphics
#        log)