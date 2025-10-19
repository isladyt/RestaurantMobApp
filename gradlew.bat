@ECHO OFF
SET DIR=%~dp0
java -Xmx64m -cp "%DIR%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
