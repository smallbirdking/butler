#!/bin/bash

if [ "$1" == "start" ]; then
    echo "Starting MongoDB..."
    brew services start mongodb-community
elif [ "$1" == "stop" ]; then
    echo "Stopping MongoDB..."
    brew services stop mongodb-community
else
    echo "Usage: $0 {start|stop}"
    exit 1
fi