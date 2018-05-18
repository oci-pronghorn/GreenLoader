#!/bin/bash

# Filters for only lines we like!
egrep -e '\QBeginning load test for:\E' -e '[0-9]+\s\Qtotal calls per second against server\E' -e '^$' -e 'Total:[0-9]+' -e '[0-9]+\s(?:µs|ms)\s(?:[0-9]|\.)+\spercentile' -e '[0-9]+\s(?:µs|ms)\smax' $1