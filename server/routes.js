// Import modules.
const authHandler = require('./controllers/authController.js');
const viewHandler = require('./controllers/viewController.js');
const checkpointController = require('./controllers/checkpointController.js');
const imageUtil = require('./utils/imageUtil.js')
const multer  = require('multer');
const upload = multer({ dest: 'uploads/' });

/**
 * Applies routes to the Express JS app.
 * @param {*} app The Express JS app.
 */
module.exports = function(app) {

  // Main splash page
  app.get(
      '/whereiscody',
      // Sign the user in!
      viewHandler.main);

  // Just for me
  app.post(
      '/add',
      authHandler.authenticateRequest,
      upload.single('attachment'),
      imageUtil.resizeImage,
      checkpointController.add
  );
};
