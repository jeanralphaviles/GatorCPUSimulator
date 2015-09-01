#!/bin/bash -e

# Compile *.java files
find . -name '*.java' | xargs javac
# Link them into a jar file
find . -name '*.class' | xargs jar cvfm GCPUSim.jar manifest.txt
