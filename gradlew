#!/usr/bin/env sh
DIR="$( cd "$( dirname "$0" )" && pwd )"
java -Xmx64m -cp "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
