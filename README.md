# Micro Service Batch Load Tester
This project was created for [OCI](http://objectcomputing.com) in order to streamline load testing of various web services.

## Running the tests

1. Pull this repo.
2. `bash build.sh`
3. `bash run.sh`

## How It Works
This project uses the Green Lightning parallel client load tester to run large amounts of load against services. Configuration is done via JSON files which are passed to the load tester via command-line.