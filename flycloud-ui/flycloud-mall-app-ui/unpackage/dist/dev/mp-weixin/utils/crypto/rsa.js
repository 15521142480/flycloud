"use strict";
const rsaEncrypt = async (plainText, publicKey) => {
  const keyData = base64ToArrayBuffer(publicKey);
  const cryptoKey = await window.crypto.subtle.importKey(
    "spki",
    keyData,
    {
      name: "RSA-OAEP",
      hash: "SHA-256"
    },
    false,
    ["encrypt"]
  );
  const data = new TextEncoder().encode(plainText);
  const encryptedData = await window.crypto.subtle.encrypt(
    {
      name: "RSA-OAEP"
    },
    cryptoKey,
    data
  );
  return arrayBufferToBase64(encryptedData);
};
const base64ToArrayBuffer = (base64) => {
  const binaryString = atob(base64);
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }
  return bytes.buffer;
};
const arrayBufferToBase64 = (buffer) => {
  const bytes = new Uint8Array(buffer);
  let binaryString = "";
  for (let i = 0; i < bytes.length; i++) {
    binaryString += String.fromCharCode(bytes[i]);
  }
  return btoa(binaryString);
};
exports.rsaEncrypt = rsaEncrypt;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/utils/crypto/rsa.js.map
