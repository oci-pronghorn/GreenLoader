#!/bin/bash

# Remove old logs.
rm -rf php/logs/error_log

# Kill existing processes.
apachectl -d php -f httpd.conf -k graceful-stop

# Start test.
apachectl -d php -f httpd.conf -e info -D FOREGROUND -c 'Listen 3314' -D Logging

# Dodododod
echo "Hi!"
echo "Hihihihihihihhi"