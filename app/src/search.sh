#!/bin/bash

shopt -s nocasematch
filenum=1
for i in $(cat files.txt)
do
    for j in $(cat classes.txt)
    do
        string=$( echo "$i" | awk 'BEGIN{ FS = "[/.]"} ; {print $(NF-1)}' )
        if [[ $string = $j ]]; then
            >&2 echo "File number: $filenum"
            >&2 echo "File is $i"
            >&2 echo "String is $string"
            >&2 echo "Searching $i for $j"
            >&2 echo "Running: egrep \"${j}\(.*{\" ${i} | egrep -v \"(public|private|class) ${j}\""
            egrep "${j}\(.*{" ${i} /dev/null | egrep -v "(public|private|class) ${j}"
            break
        fi
    done
    ((filenum++))
done