// Dependencies.
var exec = require('child_process').exec;
var execSync = require('child_process').execSync;
var config = require('./load-config.json');

// Configuration.
var rates = [100, 5000];
var fortio="fortio load -t 100s -c WORKERS -qps RATE -data-dir results -json results/OUTPUT.json -labels NAME ENDPOINT"
var fortioPost="fortio load -t 100s -c WORKERS -qps RATE -data-dir results -json results/OUTPUT.json -labels NAME -payload 'PAYLOAD' ENDPOINT"

// Create the results folder if it doesn't exist.
execSync("mkdir -p results");

// Run load tests.
config.services.forEach(function(service) {

    // Start the service.
    var proc = exec(service.start + " > results/" + service.name + ".log.txt 2>&1");
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

        // Choose corect command (POST vs GET)
        var fortioCommand;
        if (service.payload) {
            fortioCommand = fortioPost.replace("PAYLOAD", service.payload);
        } else {
            fortioCommand = fortio;
        }

        // Prepare command.
        fortioCommand = fortioCommand.replace("OUTPUT", output)
                                     .replace("ENDPOINT", service.endpoint)
                                     .replace("WORKERS", workers)
                                     .replace("RATE", rate)
                                     .replace("NAME", service.name);

        // Execute Vegeta and wait for completion.
        console.log(fortioCommand);
        execSync(fortioCommand);
    });

    // Terminate process.
    process.kill(proc.pid);
    console.log("Terminated " + service.name);
    console.log("==============================================================");
});

process.exit()

// TODO: Implement automatic image capture of graphs below.
// Import dependencies.
var domtoimage = require('dom-to-image'); // dom-to-image
var save = require('save-file'); // save-file

// Start fortio reporting server.
var fortioProc = exec("fortio report", {cwd: __dirname + "/results"});

// Generate reports.
config.services.forEach(function(service) {

    // Iterate over possible rates.
    rates.forEach(function(rate) {

        // Report filename by service and rate.
        var report = service.name + "-rate" + String(rate) + ".json";

        // Fetch report page.
        var uri = "http://localhost:8080/browse?url=" + report;
        var document = 0;
        
    });

});

// Terminate script and subprocesses.
process.kill(fortioProc.pid);