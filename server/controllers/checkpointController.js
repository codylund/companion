const mongo = require('mongodb');
const MongoClient = mongo.MongoClient;
const mongoURL = "mongodb://localhost:27017/";
const responseController = require('./responseController.js');

module.exports.add = (req, res, next) => {
  const time = req.body.time;
  const lat = req.body.lat;
  const lng = req.body.lng;
  const text = req.body.text;
  const attachment = req.body.attachment;

  if (!text && !attachment) {
    // Just an ordinary ping... Add it to the DB
    ping(time, lat, lng, (err) => {
      if (err) {
        responseController.error(res, "Ping failed!");
        next();
      } else {
        responseController.success(res, "Successful ping!");
        next();
      }
    });
  } else {
    // We have image or text; make this a checkpoint
    addCheckpoint(time, lat, lng, text, attachment, (err) => {
      if (err) {
        responseController.error(res, "Checkpoint failed!");
        next();
      } else {
        responseController.success(res, "Added checkpoint!");
        next();
      }
    });
  }
};

function ping(time, latitude, longitude, callback) {
  // get all the path stuff
  MongoClient.connect(mongoURL, (err, db) => {
    if (err) {
      callback(err);
      return;
    }

    let p = {time: time, lat: latitude, lng: longitude};

    let dbo = db.db("companion");
    dbo.collection("path").insertOne(p, (err, res) => {
      if (err) {
        callback(err);
      } else {
        console.log("Ping from (" + latitude + ", " + longitude  + ") at " + time + ".");
        db.close();
        callback(undefined);
      }
    });
  });
}

function addCheckpoint(time, latitude, longitude, desc, attachment, callback) {
  MongoClient.connect(mongoURL, (err, db) => {
    if (err) {
      callback(err);
      return;
    }

    // Make the object we will store in the DB
    var p;
    if (desc && attachment)
      p = {time: time, lat: latitude, lng: longitude, text: desc, pic: attachment};
    else if (desc)
      p = {time: time, lat: latitude, lng: longitude, text: desc};
    else if (attachment)
      p = {time: time, lat: latitude, lng: longitude, pic: attachment};
    else
      p = {time: time, lat: latitude, lng: longitude};

    let dbo = db.db("companion");
    dbo.collection("path").insertOne(p, (err, res) => {
      if (err) {
        callback(err);
      } else {
        console.log("New checkpoint from (" + latitude + ", " + longitude + ") at " + time + ".");
        db.close();
        callback(undefined);
      }
    });
  });
}
