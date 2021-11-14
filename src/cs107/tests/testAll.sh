#!/bin/bash

# 1. ensure that the script is being run from the src/cs107/tests directory
if [ $(dirname "$0") != "."  ]; then
    echo "error: script is not being run from the src/cs107/tests directory"
    exit 1
fi

# 2. find the build directory
BUILD_DIR=$(find .. -type d -name build)

# 3. build the whole project
cd $BUILD_DIR
make test

# 4. find all test executables
TEST_EXECS=$(find $BUILD_DIR -type f -name "Test*.class" | rev | sed 's/\//./' | cut -d'.' -f2 | rev)

# 5. execute all Test*.class files in the build directory
for unitTest in $TEST_EXECS
do
    java $unitTest
done

# 6. cleanup
cd ..
