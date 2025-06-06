@echo off
FOR /L %%i IN (1,1,20) DO (
   echo "Running test iteration %%i"
   call gradlew.bat test --rerun-tasks
) 