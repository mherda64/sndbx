#!/bin/bash

cd usercode

mkdir in
mkdir out
touch ./out/compiler_output.txt

echo "Compiling $1.java"
javac ./in/$1.java > ./out/compiler_output.txt

touch ./out/program_output.txt

echo "Running $1"
java --class-path=./in/ $1 > ./out/program_output.txt
# java --class-path=./in/ $1
