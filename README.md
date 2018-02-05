# Micro Service Batch Load Tester
This project was created for [OCI](http://objectcomputing.com) in order to streamline load testing of various web services.

## How It Works
This project uses Node.js and [Fortio](https://github.com/istio/fortio) to create benchmarks for a variety of microservice frameworks.

To use this project, you will need the following:

- A Unix (Linux or Mac OS) operating system.
- Node.js
- A JDK
- [Fortio](https://github.com/istio/fortio)

## Configuration
Configuration is done entirely via the `load-config.json` file. This file has the following structure:

    {
        "services": [
            {
                "name": "Service-Name (No spaces or special characters)",
                "start": "Shell command to execute to start the service (relative to where the load.js file is started)",
                "endpoint": "Service URL to hit with a request.",
                "method": "GET or POST",
                "payload": "null if GET, or payload data if POST"
            }
        ]
    }

You can view the default `load-config.json` in this project for more examples.

## Running
Once your `load-config.json` is built, simply execute `node load.js` and wait patiently for all of your results to be created. By default, they will be generated in the `results/` folder of this project.

## Contributing
This project was created for an internal requirement to test services; it is not neat, it is not pretty, and it is only barely reusable. Any contributions you are interested in making are more than appreciated!