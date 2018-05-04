// requiring the HTTP interfaces in node
var http = require('http');
var fs = require('fs');
var path = require('path');
var morgan = require('morgan');

// Add some tokens.
morgan.token("reqHeaders", function (req, res) {
    return JSON.stringify(req.headers);
});

morgan.token("reqBody", function (req, res) {
    return req.body;
});

//morgan.token("resHeaders", function (req, res) {
//    return JSON.stringify(res.getHeaders());
//});
//
//morgan.token("resBody", function (req, res) {
//    return res.body;
//});

// Create logger.
var accessLogStream = fs.createWriteStream(path.join(__dirname, 'node-log.txt'), {flags: 'a'})
var logger = morgan('ReqHeaders :reqHeaders ReqBody :reqBody ResHeaders', {stream: accessLogStream});

// create an http server to handle requests and response
http.createServer(function (req, res) {

    logger(req, res, function(err) {
       req.body = '';

       req.on('data', function (data) {
           req.body += data;
       });

       req.on('end', function () {
           var json = JSON.parse(req.body);
           res.setHeader('Content-Type', 'application/json');
           var response = {"message": 'Hello, ' + json.name + '!', "happy": !json.happy, "age": json.age * 2};
           res.body = JSON.stringify(response);
           res.end(res.body);
       });
    });
}).listen(3308);

console.log('Server running on port 3301.');