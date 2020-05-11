#! /bin/bash

BUILD_DIR="build/"

java -cp $BUILD_DIR main.sdis.server.StartServer "$@"
