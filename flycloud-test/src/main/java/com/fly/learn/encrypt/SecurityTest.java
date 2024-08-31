package com.fly.learn.encrypt;


import com.fly.learn.encrypt.encoder.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * 数据加密辅助类(默认编码UTF-8)
 *
 * @author tymon
 * @version 2016年9月10日
 */
public final class SecurityTest {


	public static void main(String[] args) {

		byte[] key = { 9, -1, 0, 5, 39, 8, 6, 19 };
		String user = decryptDes("sOGhhAUYuM7=", key);
		String password = decryptDes("BJPDvBnTtW1/idMOTmYl0xfBNr9qNpFK", key);

		System.out.println("user" + user);
		System.out.println("password" + password);
	}




	/**
	 * 密钥算法 <br>
	 * Java 6 只支持56bit密钥 <br>
	 * Bouncy Castle 支持64bit密钥
	 */
	public static final String KEY_ALGORITHM = "DES";

	/**
	 * 加密/解密算法 / 工作模式 / 填充方式
	 */
	public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5PADDING";

	/**
	 * 数据解密，算法（DES）
	 *
	 * @param cryptData 加密数据
	 * @return 解密后的数据
	 */
	public static final String decryptDes(String cryptData, byte[] key) {
		String decryptedData = null;
		try {
			// 把字符串解码为字节数组，并解密
			decryptedData = new String(decrypt(decryptBASE64(cryptData), key));
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
		return decryptedData;
	}

	/**
	 * BASE64解码
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static final byte[] decryptBASE64(String key) {
		try {
			return new BASE64Encoder().decode(key);
		} catch (Exception e) {
			throw new RuntimeException("解密错误，错误信息：", e);
		}
	}

	/**
	 * 解密
	 *
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 还原密钥
		Key k = toKey(key);
		// 实例化
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		// 初始化，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, k);
		// 执行操作
		return cipher.doFinal(data);
	}

	/**
	 * 转换密钥
	 *
	 * @param key 二进制密钥
	 * @return Key 密钥
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
		// 实例化DES密钥材料
		DESKeySpec dks = new DESKeySpec(key);
		// 实例化秘密密钥工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		// 生成秘密密钥
		SecretKey secretKey = keyFactory.generateSecret(dks);
		return secretKey;
	}




}
