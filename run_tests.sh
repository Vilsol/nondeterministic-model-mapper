#!/bin/bash

for i in {1..20}
do
   echo "Running test iteration $i"
   ./gradlew test --rerun-tasks
done 