'use strict';

const crypto = require('crypto');
const ecies = require("eth-ecies");

/**
 * Encrypts data with the provided key.
 * @param {*} key The encryption key.
 * @param {*} data The data to encrypt.
 * @return {*} The encrypted data.
 */
exports.encrypt = function(key, data) {
    let cipher = crypto.createCipher('aes-256-cbc', key);
    let encrypted = cipher.update(data, 'utf-8', 'hex');
    encrypted += cipher.final('hex');
    return encrypted;
};

/**
 * Decrypts data with the provided key.
 * @param {*} key The decryption key.
 * @param {*} data The data to decrypt.
 * @return {*} The decrypted data.
 */
exports.decrypt = function(key, data) {
    let decrypted = undefined;
    try {
      let decipher = crypto.createDecipher('aes-256-cbc', key);
      decrypted = decipher.update(data, 'hex', 'utf-8');
      decrypted += decipher.final('utf-8');
    } catch (error) {
      decrypted = undefined;
    }
    return decrypted;
};

exports.hash = function(data){
    const hash = crypto.createHash('sha256');
    hash.update(data);
    return hash.digest('hex');
};

exports.generateKey = function() {
    // 256-bit key
    return crypto.randomBytes(32).toString('hex');
};

exports.asymEncrypt = function(publicKey, data) {
    let userPublicKey = new Buffer(publicKey, 'hex');
    let bufferData = new Buffer(data, 'utf8');

    let encryptedData = ecies.encrypt(userPublicKey, bufferData);

    return encryptedData.toString('base64')
};

exports.asymDecrypt = function(privateKey, encryptedData) {
    let userPrivateKey = new Buffer(privateKey, 'hex');
    let bufferEncryptedData = new Buffer(encryptedData, 'base64');

    let decryptedData = ecies.decrypt(userPrivateKey, bufferEncryptedData);
    
    return decryptedData.toString('utf8');
};
