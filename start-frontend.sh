#!/bin/bash

echo "Starting Rollins Tennis Archive Frontend..."
echo ""

# Navigate to frontend directory
cd frontend

# Check if node_modules exists
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
fi

# Start the React app
npm start

