#!/bin/bash

# 0. ensure that the script is being run from the build directory
if [ $(dirname "$0") != "."  ]; then
    echo "error: script is not being run from the build directory"
    exit 1
fi

# 1. download the archive file
echo "1/2 - downloading jUnit archive file"
wget https://repo1.maven.org/maven2/junit/junit/4.13.2/junit-4.13.2.jar -P deps/junit

# 2. set the junit .jar path
echo "2/2 - setting environment variables"
echo "export JUNIT_HOME=$(pwd)/deps/junit" >> ~/.bashrc
echo "export CLASSPATH=$CLASSPATH:$JUNIT_HOME/junit-4.13.2.jar:." >> ~/.bashrc

# 3. reload the .bashrc configuration file
source ~/.bashrc
