package com.fly.common.utils.crypto;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 加解密工具类
 *
 * @author flyCloud
 */
@UtilityClass
public class RsaUtils {


    /**
     * RSA 密钥长度
     */
    private static final int KEY_SIZE = 2048;

    /**
     * RSA 算法
     */
    private static final String RSA_ALGORITHM = "RSA";

    /**
     * RSA OAEP SHA-256
     */
    private static final String RSA_TRANSFORMATION =
            "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";



    /**
     * OAEP 参数
     */
    private static final OAEPParameterSpec OAEP_PARAMETER_SPEC =
            new OAEPParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT
            );



    /**
     * RSA 公钥加密
     *
     * @param plainText 明文
     * @param publicKey 公钥
     * @return Base64 密文
     */
    public static String encrypt(String plainText, String publicKey) {
        try {

            PublicKey key = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key, OAEP_PARAMETER_SPEC
            );
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 公钥加密失败", e);
        }
    }

    /**
     * RSA 私钥解密
     *
     * @param encryptedText Base64 密文
     * @param privateKey    私钥
     * @return 明文
     */
    public static String decrypt(String encryptedText, String privateKey) {
        try {

            PrivateKey key = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key, OAEP_PARAMETER_SPEC);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 私钥解密失败", e);
        }
    }

    /**
     * 获取 RSA 公钥
     */
    private static PublicKey getPublicKey(String publicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey(publicKey));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 公钥解析失败", e);
        }
    }

    /**
     * 获取 RSA 私钥
     */
    private static PrivateKey getPrivateKey(String privateKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(cleanKey(privateKey));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new IllegalArgumentException("RSA 私钥解析失败", e);
        }
    }

    /**
     * 清理 PEM 密钥格式
     */
    private static String cleanKey(String key) {
        return key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
    }



    /**
     * 生成 RSA 公钥、私钥
     */
    public static void main(String[] args) throws Exception {

        // fly_cloud_login_password
        // fly_cloud_payment
        // fly_cloud_open_api
        String keyId = "fly_cloud_pay_password";

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(
                keyPair.getPublic().getEncoded()
        );
        String privateKey = Base64.getEncoder().encodeToString(
                keyPair.getPrivate().getEncoded()
        );

        System.out.println("========================================");
        System.out.println("密钥标识：" + keyId);
        System.out.println("========================================");

        System.out.println();
        System.out.println("公钥：");
        System.out.println(publicKey);

        System.out.println();
        System.out.println("私钥：");
        System.out.println(privateKey);

        System.out.println();
        System.out.println("========================================");

        // 自测
        String text = "flyCloud-123456";

        String encryptedText = encrypt(text, publicKey);
        String decryptedText = decrypt(encryptedText, privateKey);

        System.out.println("原文：" + text);
        System.out.println("密文：" + encryptedText);
        System.out.println("解密：" + decryptedText);

        System.out.println("========================================");
    }
}