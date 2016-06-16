#!/usr/bin/env bash
chmod +x ./gradlew
echo "running ./gradlew setupDecompWorkspace --refresh-dependencies"
./gradlew setupDecompWorkspace --refresh-dependencies

echo "running ./gradlew setupDevWorkspace" 
./gradlew setupDevWorkspace 
#echo "running ./gradlew setupCIWorkspace"
#./gradlew setupCIWorkspace

echo "Done. import the project into Intellij Idea as a gradle project"
echo "then run \"./gradlew gIR\" and refresh the project"

