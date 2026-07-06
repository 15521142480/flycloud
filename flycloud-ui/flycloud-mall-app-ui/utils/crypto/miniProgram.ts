import { importPublicKey, sha256 } from 'rsa-oaep-encryption';

/**
 * RSA 公钥加密
 *
 * RSA-OAEP + SHA-256
 */
export const rsaEncrypt = async (plainText: string, publicKey: string): Promise<string> => {
  const rsaPublicKey = importPublicKey(formatPublicKey(publicKey));

  const encryptedData = rsaPublicKey.encrypt(plainText, sha256.create());

  return arrayBufferToBase64(encryptedData);
};

/**
 * Base64 公钥转 PEM
 */
const formatPublicKey = (publicKey: string): string => {
  const formattedPublicKey = publicKey.match(/.{1,64}/g)?.join('\n') ?? publicKey;

  return `-----BEGIN PUBLIC KEY-----
${formattedPublicKey}
-----END PUBLIC KEY-----`;
};

/**
 * ArrayBuffer 转 Base64
 */
const arrayBufferToBase64 = (buffer: ArrayBuffer): string => {
  const base64Chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/';

  const bytes = new Uint8Array(buffer);

  let result = '';

  for (let i = 0; i < bytes.length; i += 3) {
    const byte1 = bytes[i];
    const byte2 = i + 1 < bytes.length ? bytes[i + 1] : 0;
    const byte3 = i + 2 < bytes.length ? bytes[i + 2] : 0;

    const value = (byte1 << 16) | (byte2 << 8) | byte3;

    result += base64Chars[(value >> 18) & 63];
    result += base64Chars[(value >> 12) & 63];
    result += i + 1 < bytes.length ? base64Chars[(value >> 6) & 63] : '=';
    result += i + 2 < bytes.length ? base64Chars[value & 63] : '=';
  }

  return result;
};
