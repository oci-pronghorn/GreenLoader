var bodyParser = require('body-parser');
var express = require('express');
var app = express();
var fs = require('fs');
var path = require('path');
var morgan = require('morgan');
var cluster = require('cluster');

// If master, spawn workers.
if (cluster.isMaster) {

    // Count the machine's CPUs
    var cpuCount = require('os').cpus().length;

    // Create a worker for each CPU
    for (var i = 0; i < cpuCount; i++) {
        cluster.fork();
    }

// Otherwise, do work!
} else {

    // Configuration
    var PORT = process.argv[2];
    var ENABLE_LOGGING = (process.argv[3] === "true");
    
    // Number of alternate endpoints.
    var ALTERNATE_ENDPOINTS_COUNT = 19;
    
    // Enable logging.
    if (ENABLE_LOGGING) {
    
        // Add some tokens.	
        morgan.token("reqHeaders", function (req, res) {	
            return JSON.stringify(req.headers);	
        });	
        
        morgan.token("reqBody", function (req, res) {	
            return JSON.stringify(req.body);
        });
    
        // Setup log file.
        var accessLogStream = fs.createWriteStream(path.join(__dirname, 'node-log.txt'), {flags: 'a'})
    
        // Enable Morgan.
        app.use(morgan('ReqHeaders :reqHeaders ReqBody :reqBody', {stream: accessLogStream}));
    }
    
    // Enable JSON body parsing.
    app.use(bodyParser.json());
    app.use(bodyParser.urlencoded({ extended: true }));
    
    // Respond to POST request.
    app.post('/hello', function (req, res) {
        var json = req.body;
        res.setHeader('Content-Type', 'application/json');
        var response = {"message": 'Hello, ' + json.name + '!', "happy": !json.happy, "age": json.age * 2};
        res.send(JSON.stringify(response));
    });
    
    // Respond to POST request on other endpoints.
    for (var i = 0; i < ALTERNATE_ENDPOINTS_COUNT; i++) {
        app.post('/hello' + i, function (req, res) {
            res.send("You said hello to an alternate POST endpoint!");
        });
    }
    
    // Begin listening.
    app.listen(PORT, function() {
        console.log("Now listening on " + PORT + " with logging " + (ENABLE_LOGGING ? "ENABLED" : "DISABLED"));
    });
}