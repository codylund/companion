const sharp = require("sharp");

module.exports.resizeImage = function(req, res, next) {
  // check if we are saving an image
  if (!req.file) {
    req.body.attachment = undefined;
    next();
    return;
  }

  // get the image path
  let filepath = req.file.path;
  let resizedPath = 'resized/' + filepath.split('/')[1];

  // resize for the folks back home
  sharp(filepath)
    .resize(800)
    .toFile(resizedPath, function(err) {
      if (err) {
        console.error("Failed to resize checkpoint image " + filepath + ": " + err);
        req.body.attachment = filepath;
      } else {
        req.body.attachment = resizedPath;
      }
      next();
    });
}
