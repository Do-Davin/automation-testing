#!/bin/bash
set -e

echo ""
echo "========================================================"
echo "  LAB01 - NestJS Login Integration Tests (Java/JUnit)"
echo "========================================================"
echo ""

# ── Download JUnit Jupiter JARs if missing ────────────────────
JUNIT_VER="5.10.2"
PLATFORM_VER="1.10.2"
STANDALONE="lib/junit-platform-console-standalone-${PLATFORM_VER}.jar"

mkdir -p lib out

download_if_missing() {
    local url=$1
    local dest="lib/$(basename $url)"
    if [ ! -f "$dest" ]; then
        echo "Downloading $(basename $url) ..."
        curl -sL "$url" -o "$dest"
    fi
}

BASE_MVN="https://repo1.maven.org/maven2"

download_if_missing "$BASE_MVN/org/junit/platform/junit-platform-console-standalone/${PLATFORM_VER}/junit-platform-console-standalone-${PLATFORM_VER}.jar"
download_if_missing "$BASE_MVN/org/junit/jupiter/junit-jupiter-api/${JUNIT_VER}/junit-jupiter-api-${JUNIT_VER}.jar"
download_if_missing "$BASE_MVN/org/junit/jupiter/junit-jupiter-engine/${JUNIT_VER}/junit-jupiter-engine-${JUNIT_VER}.jar"
download_if_missing "$BASE_MVN/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar"
download_if_missing "$BASE_MVN/org/junit/platform/junit-platform-commons/${PLATFORM_VER}/junit-platform-commons-${PLATFORM_VER}.jar"

echo ""
echo "Compiling ..."
javac -cp "$STANDALONE" -d out src/App.java test/AuthLoginTest.java
echo "Compiled successfully."

echo ""
echo "Running tests ..."
echo ""
java -jar "$STANDALONE" \
    --class-path out \
    --select-class AuthLoginTest \
    --details verbose
