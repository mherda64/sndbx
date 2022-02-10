#!/bin/bash

cd usercode

mkdir in
mkdir out

echo "Compiling $1.java"
javac ./in/$1.java

echo "Running $1"
touch ./out/output.txt
java --class-path=./in/ $1 > ./out/output.txt
