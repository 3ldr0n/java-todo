#!/bin/bash

mvn package

mvn exec:java -D exec.mainClass=com.luulia.App -Dexec.args="$1 $2"
