#!/bin/bash

# 1. add the linuxuprising/java repository
sudo add-apt-repository ppa:linuxuprising/java -y

# 2. reach out for available updates
sudo apt update

# 3. install the latest version of Java 17
sudo apt-get install oracle-java17-installer oracle-java17-set-default -y
