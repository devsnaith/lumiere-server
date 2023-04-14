#!/bin/bash

which javac &> /dev/null
if [ ! $? -eq 0 ]; then
  echo "Need javac to compile the project"
  exit 1
fi

if [ ! -d "./build" ]; then
  echo "Creating ./build Folder"
  mkdir ./build
  if [ $? -eq 0 ]; then
    echo "./build has been created"
  fi
fi

echo "Generating source..."
SOURCE=$(find ./src/ | grep ".java")
echo $SOURCE | tr " " "\n"
echo "Start compiling..."
javac -verbose $SOURCE -d ./build
echo "Generating jar file..."
jar cvfe lumiere-server.jar org/eu/lumiere/Lumiere -C build .

if [ -f ./lumiere-server.jar ]; then
    echo "lumiere-server jar hes been created"
fi

