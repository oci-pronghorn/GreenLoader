#!/bin/bash

# Remove old logs.
rm -rf php/logs/error_log

# Start test.
apachectl -d php -f httpd.conf -e info -D FOREGROUND -c 'Listen 3314' -D Logging