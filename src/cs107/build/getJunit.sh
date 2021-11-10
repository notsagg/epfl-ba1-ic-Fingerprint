#!/bin/bash

# 0. ensure that the script is being run from the build directory
if [ $(dirname "$0") != "./build/deps/contrib"  ]; then
    echo "error: script is not being run from the build directory"
    exit 1
fi

# 1. download the archive file
echo "1/2 - downloading jUnit archive file"
wget https://github.com/downloads/junit-team/junit/junit-4.10.jar -P deps/junit

# 2. set junit .jar path
echo "2/2 - setting environment variables"
export JUNIT_HOME=$(pwd)/deps/junit
export CLASSPATH=$CLASSPATH:$JUNIT_HOME/junit-4.10.jar:.
