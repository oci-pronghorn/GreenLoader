#!/bin/bash

# Kill existing processes.
apachectl -d php -f httpd.conf -k graceful-stop

# Start test.
apachectl -d php -f httpd.conf -e info -D FOREGROUND -c 'Listen 3313'