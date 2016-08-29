#!/bin/bash

if [[ $# -eq 0 ]] ; then
    echo 'No arguments given'
    echo 'Usage:  ./instrument.sh ./some_src_folder'
    exit 0
fi

echo "Compiling instrumentation"

javac InsertMethodLogs*.java Java8*.java

find $1 \*.java | while read filename
do

   # TODO reuse process to speed up
   echo "Instrumenting $filename"
   java InsertMethodLogs "$filename" > "$filename.bak"
   if [ $? -eq 0 ]
   then 
     mv "$filename.bak" "$filename"
   else 
     echo "Couldn't instrument $filename" >&2
   fi
done

cd -
