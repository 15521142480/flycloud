
// #ifdef H5
import { rsaEncrypt as platformRsaEncrypt } from './h5'
// #endif

// #ifdef MP-WEIXIN
import { rsaEncrypt as platformRsaEncrypt } from './miniProgram';
// #endif

/**
 * RSA 公钥加密
 */
export const rsaEncrypt = async (
  plainText: string,
  publicKey: string
): Promise<string> => {
  return platformRsaEncrypt(plainText, publicKey)
}