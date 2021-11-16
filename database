//Import the mysql module and create a connection pool with user details
const mysql = require('mysql');
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "Ifeelucrepin@1",
    database: "RentSmart",
    debug: false
});

//Gets all rooms
exports.getAllRooms = (response) => {
    //Build query
    let sql = "SELECT * FROM rooms";

    //Execute query 
    connectionPool.query(sql, (err, result) => {
        if (err){//Check for errors
            let errMsg = "{Error: " + err + "}";
            console.error(errMsg);
            response.status(400).json(errMsg);
        }
        else{//Return results in JSON format 
            //console.log(JSON.stringify(result));
            response.send(JSON.stringify(result))
        }
    });
};

//Gets all rooms
exports.getAllByPostcode = (sql,response) => {

    //Execute query 
    connectionPool.query(sql, (err, result) => {
        if (err){//Check for errors
            let errMsg = "{Error: " + err + "}";
            console.error(errMsg);
            response.status(400).json(errMsg);
        }
        else{//Return results in JSON format 
            //console.log(JSON.stringify(result));
            response.send(JSON.stringify(result))
        }
    });
};


//Adds a new customer to database 
exports.addRoom = (description, rent, postcode, image_url, website_url, seller_id, response) => {
    //Build query
    let sql = "INSERT INTO rooms (description, rent, postcode, image_url, website_url, seller_id) " +
    "       VALUES ('" + description + "'," + rent +",'" + postcode + "','" + image_url + "','" + website_url + "'," + seller_id +")";
    
    //Execute query
    connectionPool.query(sql, (err, result) => {
        if (err){//Check for errors
            let errMsg = "{Error: " + err + "}";
            console.error(errMsg);
            response.status(400).json(errMsg);
        }
        else{//Send back result
            response.send("{result: 'Room added successfully'}");
        }
    });
}

