#!/bin/bash

# Kill existing processes.
sudo killall httpd &> /dev/null

# Start test.
apachectl -d php -f httpd.conf -e info -D FOREGROUND -c 'Listen 3313'