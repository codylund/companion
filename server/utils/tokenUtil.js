'use strict';

// Import modules
const jwt = require('jsonwebtoken');
const jwtSecret = '$uPP3r$3crE+t0k3m';

/**
 * Generates a JWT token using the provided alias.
 * @param {*} alias The provided alias.
 * @return {*} The auth token.
 */
module.exports.getNewTokenFromAlias = function(alias) {
    return jwt.sign({id: alias}, jwtSecret);
};

/**
 * Decodes the alias from the provided JWT token.
 * @param {*} token Token containing alias.
 * @param {*} callback Passes back the alias.
 */
module.exports.getAliasFromToken = function(token, callback) {
    // Verify the JWT token
    jwt.verify(token, jwtSecret, function(err, decode) {
        if (err) {
            // Bad token...
            callback(undefined);
        } else {
            // Good token!
            callback(decode.id);
        }
    });
};
