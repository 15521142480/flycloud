
/**
 * RSA 公钥加密
 *
 * RSA-OAEP + SHA-256
 */
export const rsaEncrypt = async (
  plainText: string,
  publicKey: string
): Promise<string> => {
  const keyData = base64ToArrayBuffer(publicKey)

  const cryptoKey = await window.crypto.subtle.importKey(
    'spki',
    keyData,
    {
      name: 'RSA-OAEP',
      hash: 'SHA-256'
    },
    false,
    ['encrypt']
  )

  const data = new TextEncoder().encode(plainText)

  const encryptedData = await window.crypto.subtle.encrypt(
    {
      name: 'RSA-OAEP'
    },
    cryptoKey,
    data
  )

  return arrayBufferToBase64(encryptedData)
}

/**
 * Base64 转 ArrayBuffer
 */
const base64ToArrayBuffer = (base64: string): ArrayBuffer => {
  const binaryString = atob(base64)
  const bytes = new Uint8Array(binaryString.length)

  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i)
  }

  return bytes.buffer
}

/**
 * ArrayBuffer 转 Base64
 */
const arrayBufferToBase64 = (buffer: ArrayBuffer): string => {
  const bytes = new Uint8Array(buffer)

  let binaryString = ''

  for (let i = 0; i < bytes.length; i++) {
    binaryString += String.fromCharCode(bytes[i])
  }

  return btoa(binaryString)
}
