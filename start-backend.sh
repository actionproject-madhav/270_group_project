#!/bin/bash

echo "Starting Rollins Tennis Archive Backend..."
echo ""

# Navigate to backend directory
cd backend

# Create output directory
mkdir -p out

# Compile Java files
echo "Compiling Java files..."
javac -cp "gson.jar:poi-5.2.5.jar:poi-ooxml-5.2.5.jar:poi-ooxml-lite-5.2.5.jar:xmlbeans-5.3.0.jar:commons-compress-1.24.0.jar:commons-collections4-4.4.jar:log4j-api-2.20.0.jar" src/main/java/com/rollins/tennis/*.java -d out/

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

# Run the server
echo "Starting server..."
java -cp "gson.jar:poi-5.2.5.jar:poi-ooxml-5.2.5.jar:poi-ooxml-lite-5.2.5.jar:xmlbeans-5.3.0.jar:commons-compress-1.24.0.jar:commons-collections4-4.4.jar:log4j-api-2.20.0.jar:out/" com.rollins.tennis.Server

