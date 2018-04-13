// requiring the HTTP interfaces in node 
var http = require('http'); 

// create an http server to handle requests and response 
http.createServer(function (req, res) { 

    var jsonString = '';

    req.on('data', function (data) {
        jsonString += data;
    });

    req.on('end', function () {
        var json = JSON.parse(jsonString);
        res.setHeader('Content-Type', 'application/json'); 
        var response = {"message": 'Hello, ' + json.name + '!', "happy": !json.happy, "age": json.age * 2};
        res.end(JSON.stringify(response)); 
    });

// use port 8080 
}).listen(3301); 

console.log('Server running on port 3301.');