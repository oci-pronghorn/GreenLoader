// Dependencies.
var exec = require('child_process').exec;
var execSync = require('child_process').execSync;
var config = require('./load-config.json');

// Configuration.
var rates = [10, 1000];
var greenLoader = "java -jar green-loader/target/greenloader.jar ENDPOINT METHOD PAYLOAD results/OUTPUT WORKERS RATE 5"

// Create the results folder if it doesn't exist.
execSync("mkdir -p results");

// Index counter in our for loop.
var i = 0;

// Iterate over services.
config.services.forEach(function(service) {

    // Modify service name for outfiles.
    i++;
    service.name = i + "-" + service.name;

    // Start the service.
    proc = exec(service.start + " > results/" + service.name + ".log.txt 2>&1");
    console.log("Started " + service.name + " with PID " + proc.pid);

    // Wait for the service to start.
    console.log("Awaiting complete service startup.");
    var seconds = 10;
    var waitTill = new Date(new Date().getTime() + seconds * 1000);
    while (waitTill > new Date()) { }

    // Iterate over possible rates.
    rates.forEach(function(rate) {

        // Number of workers is determined by rate.
        var workers = 1;
        if (rate == 1000) {
            workers = 10;
        }

        // Output filename by service and rate.
        var output = service.name + "-rate" + String(rate);

        // Prepare command.
        var greenLoaderCommand = greenLoader.replace("OUTPUT", output)
                                            .replace("ENDPOINT", service.endpoint).replace("METHOD", service.method)
                                            .replace("PAYLOAD", service.payload).replace("WORKERS", workers)
                                            .replace("RATE", rate);

        // Execute Vegeta and wait for completion.
        console.log(greenLoaderCommand);
        execSync(greenLoaderCommand);

        // Wait for the service to cool down.
        console.log("Await cooldown.");
        var seconds = 1;
        var waitTill = new Date(new Date().getTime() + seconds * 1000);
        while (waitTill > new Date()) { }
        console.log("Cooldown complete.");
    });

    // Terminate process.
    process.kill(proc.pid);
    console.log("Terminated " + service.name);
})

// Terminate script.
process.exit()