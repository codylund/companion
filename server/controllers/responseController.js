'use strict';

/**
 * Generates an error response given the supplied error message.
 * @param {*} res The HTTP response object.
 * @param {*} messageText The error message.
 */
module.exports.error = (res, messageText) => {
    res.status(400).json({message: messageText});
};

/**
 * Generates a success response given the supplied success message.
 * @param {*} res The HTTP response object.
 * @param {*} messageText The success message.
 */
module.exports.success = (res, messageText) => {
    res.status(200).json({message: messageText});
};

/**
 * Generates a success JSON response given the supplied JSON object.
 * @param {*} res 
 * @param {*} messageText 
 */
module.exports.json = (res, jsonObject) => {
    res.status(200).json(jsonObject);
}

/**
 * Generates a response for a successful signin. Creates a new
 * JWT token using the given alias.
 * @param {*} res The response object.
 * @param {*} tokenValue A token containing the alias of user who signed in.
 */
module.exports.successfulSignIn = (res, tokenValue) => {
    res.status(200).json({token: tokenValue});
};

/**
 * TESTING ONLY: Generates a response for a successful signin test. 
 * Simply welcomes the user back.
 * @param {*} res The response object.
 * @param {*} alias The successfully signed in alias.
 */
module.exports.test_successfulSignIn = (res, alias) => {
    res.status(200).json({message: 'Welcome back, ' + alias + '!'});
};
