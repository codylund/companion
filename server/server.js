'use strict';

// Load environment from .env file
require('dotenv').config();

// required packages
const express = require('express');
const app = express();
const port = process.env.PORT || 8080;
const routes = require('./routes.js');
const middleware = require('./middleware.js');
const tokenUtil = require('./utils/tokenUtil.js');
const fs = require('fs');
const https = require('https');
const http = require('http');

// Prepare pre-route middleware
middleware.preRouting(app);

// Prepare API routes
routes(app);

// Prepare post-route middleware
middleware.postRouting(app);

console.log(tokenUtil.getNewTokenFromAlias('cody'));

// Set-up HTTPS
const key = fs.readFileSync('./encryption/private.key');
const cert = fs.readFileSync('./encryption/codylund_com.crt');
const ca = fs.readFileSync('./encryption/codylund_com.ca-bundle');
const options = {
  key: key,
  cert: cert,
  ca: ca
};

// Start server
https.createServer(options, app).listen(443);
//http.createServer(app).listen(80);
