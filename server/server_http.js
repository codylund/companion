'use strict';

// Load environment from .env file
require('dotenv').config();

// required packages
const express = require('express');
const forceSsl = require('express-force-ssl');
const app = express();
const http = require('http');

app.use(forceSsl);

// Start server
http.createServer(app).listen(80);
