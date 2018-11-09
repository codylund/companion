/**
 * Prepares middleware for provided app instance.
 * @param {*} app An ExpressJS app.
 */
module.exports.preRouting = function(app) {
    const bodyParser = require('body-parser');
    const cors = require('cors');
    const expressValidator = require('express-validator');
    const express = require('express');

    // Using body parsing package
    app.use(bodyParser.urlencoded({extended: true}));
    app.use(bodyParser.json());

    // Enable CORS
    app.use(cors());

    // Use express-validator to validate requests
    app.use(expressValidator());

    app.use(express.static('public'));
    app.use(express.static('scripts'));
    app.use(express.static('resized'));
    app.use(express.static('views'));
};

/**
 * Prepares post-route middleware primarily for fielding invalid requests.
 * @param {*} app An ExpressJS app.
 */
module.exports.postRouting = function(app) {
    // Middleware to handle all invalid routes (TODO : find better way)
    /* app.use(function(req, res, next) {
        if (!res.headersSent) {
            res.status(401).json({message: 'Invalid request.'});
        }
    }); */

    // error handling
    app.use(function (err, req, res, next) {
      console.error(err.stack)
      res.status(500).send('Something broke!')
    });
};
