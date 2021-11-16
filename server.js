//Import the express and url modules
var express = require('express');
var url = require("url");

//Import database functions
const db = require('./database');

//Status codes defined in external file
require('./http_status.js');

//The express module is a function. When it is executed it returns an app object
var app = express();

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });

//Import the mysql module
var mysql = require('mysql');

//Create a connection object with the user details
var connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "Ifeelucrepin@1",
    database: "RentSmart",
    debug: false
});

//Set up the application to handle GET requests sent to the user path
app.get('/rooms', getAllRooms);
app.get('/rooms/*', getbyPostCode);

//Start the app listening on port 8080
app.listen(8080);

//Handles GET requests to our web service
function getAllRooms(request, response){
        db.getAllRooms(response);
}

function getbyPostCode(request, response) {
    //Parse the URL
    var urlObj = url.parse(request.url, true);

    //Extract object containing queries from URL object.
    var queries = urlObj.query;
    var numItems = 20;

    //     //Get the pagination properties if they have been set. Will be  undefined if not set.
    var offset = queries['offset'];

    //Split the path of the request into its components
    var pathArray = urlObj.pathname.split("/");

    //Get the last part of the path
    var pathEnd = pathArray[pathArray.length - 1];

    //Build query
    let sql = "SELECT * FROM rooms WHERE rooms.postcode = '"+pathEnd+"'";
    sql += "ORDER BY rooms.id LIMIT " + numItems + " OFFSET " + offset;
    db.getAllByPostcode(sql,response);

}

//Export Server for testing
module.exports = app;
