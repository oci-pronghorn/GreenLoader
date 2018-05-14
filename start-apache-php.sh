#!/bin/bash
apachectl -d php -f httpd.conf -e info -D FOREGROUND -c 'Listen 3313'