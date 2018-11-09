const mongo = require('mongodb');
const fs = require('fs');
const MongoClient = mongo.MongoClient;
const mongoURL = "mongodb://localhost:27017/";
const Mustache = require('mustache');
const responseController = require('./responseController.js');

/* builds the main splash page */
module.exports.main = (req, res, next) => {
  fs.readFile('views/main.html', 'utf8', (err, template) => {
    if (err) {
      responseController.console.error(res, "Failed to serve page.");
      next();
      return;
    }

    // get all the path stuff
    MongoClient.connect(mongoURL, (err, db) => {
      if (err) {
        responseController.error(res, "Failed to access database.");
        next();
        return;
      }
      let dbo = db.db("companion");
      dbo.collection("path").find().toArray((err, pts) => {
        if (err) {
          responseController.error("Failed to access database.");
          next();
          return;
        }
        // compile all the coordinates in a json object
        path = JSON.parse('{"coords":[], "checkpoints":[]}');
        let index = 0;
        for (i in pts) {
          let pt = pts[i];
          if (!pt.lat || !pt.lng) {
            // We were missing a coordinate, so we discluded this entry...
            break;
          } else if (pt.text || pt.pic) {
            // It's a checkpoint!
            var pic;
            if (pt.pic)
              pic = pt.pic.split('/')[1];
            else
              pic = undefined;
            path.checkpoints.push({label: index, time: pt.time, lat: pt.lat, lng: pt.lng, text: pt.text, pic: pic});
            path.coords.push({lat: pt.lat, lng: pt.lng});
            index += 1;
          }
        }

        let now = new Date();
        let diffMs = now - new Date(pts[pts.length-1].time);
        let diffMins = Math.round(diffMs/1000/60);

        // What was the last ping location and what was the time?
        path.last_time = diffMins + " minutes ago";
        path.last_lat = pts[pts.length-1].lat;
        path.last_lng = pts[pts.length-1].lng;
        path.coords.push({lat: path.last_lat, lng: path.last_lng});

        // parse template and render in the coordinates
        Mustache.parse(template);
        let rendered = Mustache.render(template, path);

        // send response to the user
        res.send(rendered);
        next();
      });
    });
  });
};
