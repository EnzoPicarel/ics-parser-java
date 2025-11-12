#!/bin/bash

echo "==========CLEANING=========="
./gradlew clean

echo "==========BUILDING=========="
./gradlew build

echo "=========EXECUTION=========="

if [ $# -eq 0 ]
then
    java -cp build/classes/java/main eirb.pg203.Client src/test/resources/i2.ics
    #-cp path find classes
else
    java -cp build/classes/java/main eirb.pg203.Client "$1"
fi
