//Server code that we are testing
let server = require ('../server')

//Import database functions
const db = require('../database');

//Set up Chai library 
let chai = require('chai');
let should = chai.should();
let assert = chai.assert;
let expect = chai.expect;

//Set up Chai for testing web service
let chaiHttp = require ('chai-http');
chai.use(chaiHttp);

//Import the mysql module and create a connection pool with the user details
const mysql = require('mysql');
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "Ifeelucrepin@1",
    database: "RentSmart",
    debug: false
});

//Wrapper for all database tests
describe('Database', () => {

    //Mocha test for getAllRooms method in database module.
    describe('#getAllRooms', () => {
        it('should return all of the rooms in the database', (done) => {
            //Mock response object for test
            let response= {};

            /* When there is an error response.staus(ERROR_CODE).json(ERROR_MESSAGE) is called
               Mock object should fail test in this situation. */
            response.status = (errorCode) => {
                return {
                    json: (errorMessage) => {
                        console.log("Error code: " + errorCode + "; Error message: " + errorMessage);
                        assert.fail("Error code: " + errorCode + "; Error message: " + errorMessage);
                        done();
                    }
                }
            };

            //Add send function to mock object
            response.send = (result) => {
                //Convert result to JavaScript object
                let resObj = JSON.parse(result);

                //Check that an array of Rooms is returned
                resObj.should.be.a('array');

                //Check that appropriate properties are returned
                if(resObj.length > 1){
                    resObj[0].should.have.property('description');
                    resObj[0].should.have.property('rent');
                    resObj[0].should.have.property('postcode');
                    resObj[0].should.have.property('image_url');
                    resObj[0].should.have.property('website_url');
                    resObj[0].should.have.property('seller_id');
                }

                //End of test
                done();
            }

            //Call function that we are testing
            db.getAllRooms(response);
        });
    });

    //Mocha test for addRoom method in database module.
    describe('#addRoom', () => {
        it('should add a room to the database', (done) => {
            //Mock response object for test
            let response= {};

            /* When there is an error response.staus(ERROR_CODE).json(ERROR_MESSAGE) is called
               Mock object should fail test in this situation. */
            response.status = (errorCode) => {
                return {
                    json: (errorMessage) => {
                        console.log("Error code: " + errorCode + "; Error message: " + errorMessage);
                        assert.fail("Error code: " + errorCode + "; Error message: " + errorMessage);
                        done();
                    }
                }
            };

            //Add send function to mock object. This checks whether function is behaving correctly
            response.send = () => {
                //Check that room has been added to database
                let sql = "SELECT description FROM rooms WHERE description='" + roomDescription + "'";
                connectionPool.query(sql, (err, result) => {
                    if (err){//Check for errors
                        assert.fail(err);//Fail test if this does not work.
                        done();//End test
                    }
                    else{
                        //Check that room has been added
                        expect(result.length).to.equal(1);

                        //Clean up database
                        sql = "DELETE FROM rooms WHERE description='" + roomDescription + "'";
                        connectionPool.query(sql, (err, result) => {
                            if (err){//Check for errors
                                assert.fail(err);//Fail test if this does not work.
                                done();//End test
                            }
                            else{
                                done();//End test
                            }
                        });
                    }
                });
            };

            //Create random room details
            let roomDescription = Math.random().toString(36).substring(2, 15);
            let roomRent = 100;
            let roomPostCode = "NW";
            let roomImage_url = "test imageURL";
            let roomWebsite_url = "test webURL";
            let roomSellerId = 1;

            //Call function to add room to database
            db.addRoom(roomDescription, roomRent, roomPostCode, roomImage_url, roomWebsite_url, roomSellerId, response);
        });
    });
});

//Wrapper for all web service tests
describe('Web Service', () => {

    //Test of GET request sent to /Rooms
    describe('/GET rooms', () => {
        it('should GET all the rooms', (done) => {
            chai.request(server)
                .get('/rooms')
                .end((err, response) => {
                    //Check the status code
                    response.should.have.status(200);

                    //Convert returned JSON to JavaScript object
                    let resObj = JSON.parse(response.text);

                    //Check that an array of Rooms is returned
                    resObj.should.be.a('array');

                    //Check that appropriate properties are returned
                    if(resObj.length > 1){
                        resObj[0].should.have.property('description');
                        resObj[0].should.have.property('rent');
                        resObj[0].should.have.property('postcode');
                        resObj[0].should.have.property('image_url');
                        resObj[0].should.have.property('website_url');
                        resObj[0].should.have.property('seller_id');
                    }

                    //End test
                    done();
                });
        });
    });
});

