#!/bin/bash

# This script retrieves striped away resources to reach the 20kb target zip file

# 1. ensure that the script is being run from the src/cs107 directory
if [ $(dirname "$0") != "."  ]; then
    echo "error: script is not being run from the src/cs107 directory"
    exit 1
fi

# 2. make a shallow clone of the git repository
wget -q --show-progress https://github.com/notsagg/epfl-ba1-ic-Fingerprint/archive/refs/heads/master.zip -O loo9Iupa-fingerprint.zip
unzip loo9Iupa-fingerprint.zip 1>/dev/null
mv epfl-ba1-ic-Fingerprint-master loo9Iupa-fingerprint
rm loo9Iupa-fingerprint.zip

# 3. extract the directory of interest
cp -r loo9Iupa-fingerprint/resources ../../

# 4. cleanup
rm -r loo9Iupa-fingerprint
