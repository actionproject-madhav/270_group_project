#!/bin/bash

echo "Starting Rollins Tennis Archive Backend..."
echo ""

# Navigate to backend directory
cd backend

# Create output directory
mkdir -p out

# Compile Java files
echo "Compiling Java files..."
javac -cp gson.jar src/main/java/com/rollins/tennis/*.java -d out/

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

# Run the server
echo "Starting server..."
java -cp gson.jar:out/ com.rollins.tennis.Server

