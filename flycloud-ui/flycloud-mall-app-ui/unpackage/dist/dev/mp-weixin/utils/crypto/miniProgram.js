"use strict";
const common_vendor = require("../../common/vendor.js");
const rsaEncrypt = async (plainText, publicKey) => {
  const rsaPublicKey = common_vendor.forge.pki.publicKeyFromPem(formatPublicKey(publicKey));
  const encryptedData = rsaPublicKey.encrypt(common_vendor.forge.util.encodeUtf8(plainText), "RSA-OAEP", {
    md: common_vendor.forge.md.sha256.create()
  });
  return common_vendor.forge.util.encode64(encryptedData);
};
const formatPublicKey = (publicKey) => {
  var _a;
  const formattedPublicKey = ((_a = publicKey.match(/.{1,64}/g)) == null ? void 0 : _a.join("\n")) ?? publicKey;
  return `-----BEGIN PUBLIC KEY-----
${formattedPublicKey}
-----END PUBLIC KEY-----`;
};
exports.rsaEncrypt = rsaEncrypt;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/utils/crypto/miniProgram.js.map
