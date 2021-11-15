#!/bin/bash

# This script strips away large files from the project to reach a strict maximum of a 20kb zip file

# 1. ensure that the script is being run from the src/cs107 directory
if [ $(dirname "$0") != "."  ]; then
    echo "error: script is not being run from the src/cs107 directory"
    exit 1
fi

# 2. cleanup from previous session
rm -r loo9Iupa-fingerprint.zip loo9Iupa-fingerprint epfl-ba1-ic-Fingerprint-master 2>/dev/null

# 3. make a shallow clone of the git repository
wget -q --show-progress https://github.com/notsagg/epfl-ba1-ic-Fingerprint/archive/refs/heads/master.zip -O loo9Iupa-fingerprint.zip
unzip loo9Iupa-fingerprint.zip 1>/dev/null
mv epfl-ba1-ic-Fingerprint-master loo9Iupa-fingerprint
rm loo9Iupa-fingerprint.zip

# 4. remove large files
cd loo9Iupa-fingerprint
rm -r resources # input fingerprints used for testing purposes
rm README.md # symbolic link to the actual README.md
rm -r src/cs107/docs # doxygen HTML documentation
rm src/cs107/doxygen.conf # doxygen configuration file
rm -r src/cs107/tests # unit-test executables
cd ..

# 5. initialize constants
LIMIT=20 # 20kb compress file limit

# 6. compress the project
zip -9 -r loo9Iupa-fingerprint.zip loo9Iupa-fingerprint 1>/dev/null

# 7. compare the size archived version of the project with LIMIT
size=$(du loo9Iupa-fingerprint.zip | sed 's/\t/:/g' | cut -d ':' -f1)

if [ $size -le $LIMIT ]; then
    echo " => Fingerprint archive file is ready for submission"
    exit 0
else
    remain=$(($size-$LIMIT))
    echo "$remain KB of data still remains to be stripped away"
    echo ""
    echo "The 20 largest files are: "
    find loo9Iupa-fingerprint -type f -exec du -hsx {} \; | sort -nr | head -n20
fi
