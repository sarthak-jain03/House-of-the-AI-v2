@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0__%"=="" (SET __MVNW_ARG0__=%~dpnx0) ELSE (SET __MVNW_ARG0__=%__MVNW_ARG0__% %~dpnx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
@SET PSModulePath=

@FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $defaultUrl='https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar'; $mvnwProperties=Join-Path $scriptDir '.mvn\wrapper\maven-wrapper.properties'; if (Test-Path $mvnwProperties) { $props=Get-Content $mvnwProperties; foreach ($l in $props) { if ($l -match '^wrapperUrl=(.+)$') { $defaultUrl=$Matches[1]; break } } }; Write-Output \"MVNW_WRAPPER_URL=$defaultUrl\"}"`) DO @(
    @IF "%%A"=="MVNW_WRAPPER_URL" @SET "MVNW_WRAPPER_URL=%%B"
)

@SET PSModulePath=%__MVNW_PSMODULEP_SAVE%

@SET MVNW_WRAPPER_JAR=%~dp0.mvn\wrapper\maven-wrapper.jar

@REM Download maven-wrapper.jar if not present
@IF NOT EXIST "%MVNW_WRAPPER_JAR%" (
    powershell -noprofile -Command "[Net.ServicePointManager]::SecurityProtocol=[Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar' -OutFile '%MVNW_WRAPPER_JAR%'"
    @IF ERRORLEVEL 1 (
        @ECHO Failed to download maven-wrapper.jar
        @EXIT /B 1
    )
)

@REM End verify
@SET MAVEN_PROJECTBASEDIR=%~dp0

@REM Find java.exe
@SET JAVA_EXE=java.exe

@IF NOT "%JAVA_HOME%"=="" @SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

@FOR /F "usebackq tokens=*" %%G IN (`"%JAVA_EXE%" -classpath "%MVNW_WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*`) DO @(
    @IF "%%G"=="" (
        @SET __MVNW_CMD__=
    ) ELSE (
        @SET "__MVNW_CMD__=%%G"
    )
)

@IF "%__MVNW_CMD__%"=="" (
    "%JAVA_EXE%" ^
        %JVM_CONFIG_MAVEN_PROPS% ^
        %MAVEN_OPTS% ^
        -classpath "%MVNW_WRAPPER_JAR%" ^
        "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
        org.apache.maven.wrapper.MavenWrapperMain %*
) ELSE (
    "%__MVNW_CMD__%" %*
)

@IF ERRORLEVEL 1 (
    @IF NOT "%__MVNW_ERROR__%"=="" @ECHO %__MVNW_ERROR__%
    @EXIT /B 1
)
