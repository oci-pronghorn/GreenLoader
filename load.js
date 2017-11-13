// Dependencies.
var exec = require('child_process').exec;
var execSync = require('child_process').execSync;
var config = require('./load-config.json');

// Configuration.
var rates = [10, 1000, 5000];

// CPU profiling command/
// This will print CPU and MEM space separated.
var profile = "while sleep 1; do ps -o '%cpu,%mem' -p PID | sed 1d >> results/OUTPUT.profile.txt; done";

// Vegeta request string.
var vegetaHtml = "echo \"REQ\" | vegeta attack -duration=5s -rate=RATE -workers=WORKERS | tee results.bin | vegeta report -reporter=plot -output=results/OUTPUT.html";
var vegetaTxt = "cat results.bin | vegeta report -reporter=text -output=results/OUTPUT.txt"

// Create the results folder if it doesn't exist.
execSync("mkdir -p results");

// Iterate over services.
config.services.forEach(function(service) {

    // Start the service.
    proc = exec(service.start);
    console.log("Started " + service.name + " with PID " + proc.pid);

    // Wait for the service to start.
    var seconds = 10;
    var waitTill = new Date(new Date().getTime() + seconds * 1000);
    while (waitTill > new Date()) { }

    // Iterate over possible rates.
    rates.forEach(function(rate) {

        // Number of workers is determined by rate.
        var workers = 1;
        if (rate == 1000) {
            workers = 10;
        } else if (rate == 5000) {
            workers = 50;
        }

        // Configure final variables.
        var output = service.name + "-rate" + String(rate);

        // Prepare commands.
        var profileCommand = profile.replace("PID", proc.pid).replace("OUTPUT", output);
        var vegetaCommand = vegetaHtml.replace("REQ", service.req).replace("RATE", rate).replace("WORKERS", workers).replace("OUTPUT", output);
        var vegetaCommandTwo = vegetaTxt.replace("OUTPUT", output);

        // Begin CPU profiling.
        console.log(profileCommand);
        profileProc = exec(profileCommand);

        // Execute Vegeta and wait for completion.
        console.log(vegetaCommand);
        execSync(vegetaCommand);

        // Stop CPU profiling.
        profileProc.kill();

        // Create Vegeta text reports and wait for completion.
        console.log(vegetaCommandTwo);
        execSync(vegetaCommandTwo);

        // Clean up results and CPU profiling files.
        execSync("rm -rf results.bin");

        // Wait for the service to cool down.
        var seconds = 2;
        var waitTill = new Date(new Date().getTime() + seconds * 1000);
        while (waitTill > new Date()) { }
    });

    // Terminate process.
    proc.kill();
    console.log("Terminated " + service.name);
})
