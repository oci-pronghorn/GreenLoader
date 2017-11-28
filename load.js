// Dependencies.
var exec = require('child_process').exec;
var execSync = require('child_process').execSync;
var config = require('./load-config.json');

// Configuration.
var rates = [10, 1000];

// CPU profiling command/
// This will print CPU and MEM space separated.
var profile = "while sleep 1; do ps -o '%cpu,%mem' -p PID | sed 1d >> results/OUTPUT.profile.txt; done";

// Vegeta request string.
var vegetaWarmup = "echo \"REQ\" | vegeta attack -duration=10s -rate=100 -workers=1";
var vegetaHtml = "echo \"REQ\" | vegeta attack -duration=5s -rate=RATE -workers=WORKERS | tee results.bin | vegeta report -reporter=plot -output=results/OUTPUT.html";
var vegetaTxt = "cat results.bin | vegeta report -reporter=text -output=results/OUTPUT.txt";
var vegetaCsv = "cat results.bin | vegeta dump -dumper=csv -output=results/OUTPUT.csv";

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

    // Warm up the service.
    var vegetaWarmupCommand = vegetaWarmup.replace("REQ", service.req);
    console.log("Warming up service: " + vegetaWarmupCommand);
    execSync(vegetaWarmupCommand);

    // Iterate over possible rates.
    rates.forEach(function(rate) {

        // Number of workers is determined by rate.
        var workers = 1;
        if (rate == 1000) {
            workers = 10;
        }

        // Configure final variables.
        var output = service.name + "-rate" + String(rate);

        // Prepare commands.
        var profileCommand = profile.replace("PID", proc.pid).replace("OUTPUT", output);
        var vegetaCommand = vegetaHtml.replace("REQ", service.req).replace("RATE", rate).replace("WORKERS", workers).replace("OUTPUT", output);
        var vegetaCommandTwo = vegetaTxt.replace("OUTPUT", output);
        var vegetaCommandThree = vegetaCsv.replace("OUTPUT", output);

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
        console.log(vegetaCommandThree);
        execSync(vegetaCommandThree);

        // Clean up results and CPU profiling files.
        execSync("rm -rf results.bin");

        // Wait for the service to cool down.
        console.log("Await cooldown.");
        var seconds = 10;
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