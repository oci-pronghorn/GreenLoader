<?php
// Read JSON POST.
$request = json_decode(file_get_contents("php://input"), true);

// Extract properties.
$name = $request["name"];
$age = $request["age"];
$happy = $request["happy"];

// Create JSON response.
$response = new stdClass();
$response->message = "Hello, " . $name . "!";
$response->age = $age * 2;
$response->happy = !$happy;
 
// Set header as json
header("Content-type: application/json");
 
// send response
echo json_encode($response);
?>