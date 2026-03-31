$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$gradleUserHome = Join-Path $projectRoot ".gradle-temp"

$env:GRADLE_USER_HOME = $gradleUserHome
$env:GRADLE_OPTS = "-Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=7897 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=7897"

& "D:\DevelopTools\Gradle\gradle-8.0\bin\gradle.bat" :customview:assemble --console=plain --no-daemon
