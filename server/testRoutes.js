const authHandler = require('./controllers/authController.js');
const registrationHandler = require('./controllers/registrationController');
const testHandler = require('./controllers/testController.js');
const identityHandler = require('./controllers/blockchainController.js');
const settings = require('./settings');

/**
 * Prepares testing routes.
 * @param {*} app The Express JS app.
 */
module.exports = function(app) {
    // Only prepare theseroutes if not in release mode.
    if (!settings.releaseMode()) {
        // Tests validity of token
        app.route('/test_signin')
            .post(authHandler.loginRequired, testHandler.testSignIn);
    }
};
