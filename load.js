// Dependencies.
var exec = require('child_process').exec;
var execSync = require('child_process').execSync;
var config = require('./load-config.json');

// Configuration.
var rates = [20, 1000, 5000];

// Vegeta request string.
var vegetaHtml = "echo \"REQ\" | vegeta -profile=PROFILE attack -duration=5s -rate=RATE -workers=WORKERS | tee results.bin | vegeta report -reporter=plot -output=OUTPUT.html";
var vegetaTxt = "cat results.bin | vegeta report -reporter=txt -output=OUTPUT.txt"

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
        var profile = "cpu";
        var output = service.name + "-rate" + String(rate);

        // Prepare vegeta commands.
        var vegetaCommand = vegetaHtml.replace("REQ", service.req).replace("PROFILE", profile).replace("RATE", rate).replace("WORKERS", workers).replace("OUTPUT", output);
        var vegetaCommandTwo = vegetaTxt.replace("OUTPUT", output);

        // Execute Vegeta and wait for completion.
        console.log(vegetaCommand);
        execSync(vegetaCommand);
        console.log(vegetaCommandTwo);
        execSync(vegetaCommandTwo);

        // Clean up results and CPU profiling files.
        execSync("rm -rf results.bin");
        execSync("rm -rf cpu.pprof");
    });

    // Terminate process.
    proc.kill();
    console.log("Terminated " + service.name);
})
