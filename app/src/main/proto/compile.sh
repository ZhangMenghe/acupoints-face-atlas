#!/usr/bin/env sh

CURRENT_DIR=`pwd`
PROTOC="protoc"
PROTO_IN_DIR="${CURRENT_DIR}"
CPP_OUT_DIR="${CURRENT_DIR}"
JAVA_OUT_DIR="${CURRENT_DIR}"

# Compile the *.proto file.
echo "PROTO_IN_DIR=${PROTO_IN_DIR}"
echo "CPP_OUT_DIR=${CPP_OUT_DIR}/../cpp/proto"
#echo "JAVA_OUT_DIR=${JAVA_OUT_DIR}../java/acu/face/proto"
echo "Generating Java/CPP code from .proto ..."
for proto_file in `find . -name "*.proto"`
do
    echo "  Compile: ${proto_file}"
    ${PROTOC} --cpp_out=${CPP_OUT_DIR} ${proto_file}

#    ${PROTOC} --cpp_out=${CPP_OUT_DIR} --java_out=${JAVA_OUT_DIR} ${proto_file}
done
echo "Done!"

#build/tools/make-standalone-toolchain.sh \
#--arch=arm \
#--platform=android-29 \
#--toolchain=arm-linux-android-clang5.0 \
#--install-dir=$HOME/Android/arm-28-toolchain-clang \
#--stl=c++_shared