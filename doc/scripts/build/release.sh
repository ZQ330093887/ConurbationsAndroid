#!/usr/bin/env bash
if test ! -s "$1"
then
   echo No project specified
   exit
fi
echo "###### Building $1 ############"
ANDROID_HOME=/y/program/android-sdk ./gradlew clean $1:aR  -DGENERATE_CHANNEL=true -DSAVE_OUTPUT=true --info