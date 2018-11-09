'use strict';

// passphrase
const passphrase = 'i like big butts and i cannot lie';

// Import modules
const responseHandler = require('./responseController.js');
const settings = require('../settings.js');
const tokenUtil = require('../utils/tokenUtil.js');

/**
 * Verify the request is authentic.
 * @param {*} req   The request object.
 * @param {*} res   The response object.
 * @param {*} next  The next function to call.
 */
module.exports.authenticateRequest = function(req, res, next) {
    // Retrieve token from header.
    let token = getToken(req.headers);

    // Check for non-existant token.
    if (!token) {
        req.user = undefined;
        next();
        return;
    }

    // Retrieve the alias from the token.
    tokenUtil.getAliasFromToken(token, function(result) {
        req.user = res[0];
        next();
        return;
    });
};

/**
 * Gets the token from the provided header.
 * @param {*} headers
 * @return {String}
 */
function getToken(headers) {
    if (verifyHeader(headers)) {
        return headers.authorization.split(' ')[1];
    } else {
        return undefined;
    }
}

/**
 * Validates the provided header by checking for Bearer type.
 * @param {*} headers
 * @return {Boolean}
 */
function verifyHeader(headers) {
    return headers
        && headers.authorization
        && headers.authorization.split(' ')[0] === 'Bearer';
}
