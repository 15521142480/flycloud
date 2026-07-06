"use strict";
const utils_crypto_miniProgram = require("./miniProgram.js");
const rsaEncrypt = async (plainText, publicKey) => {
  return utils_crypto_miniProgram.rsaEncrypt(plainText, publicKey);
};
exports.rsaEncrypt = rsaEncrypt;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/utils/crypto/index.js.map
