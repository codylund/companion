'use strict';

// Update to reflect current settings
const releaseMode = true;
const isSpoofing = true;

/**
 * Returns true if this instance of the server is to be 
 * released.
 * @returns {boolean}
 */
module.exports.releaseMode = function() {
    return releaseMode;
};

/** 
 * Returns true if this instance of the server is spoofing 
 * data.
 * @return {boolean}
 */
module.exports.isSpoofingData = function() {
    return isSpoofing;
};
