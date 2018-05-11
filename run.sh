#!/bin/bash

mvn package

mvn exec:java -D exec.mainClass=com.todo.app.App -Dexec.args="$1 $2"
