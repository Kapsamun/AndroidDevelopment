package com.mirea.kapsamunelenanikolaevna.cryptoloader;

import android.util.Log;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESUtils {
    private static final String AES_ALGORITHM = "AES";

    // Генерация ключа AES
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(128); // 128 бит для AES
        return keyGenerator.generateKey();
    }

    // Шифрование текста
    public static byte[] encryptMsg(String message, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                 | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("Ошибка шифрования: " + e.getMessage(), e);
        }
    }

    // Дешифрование байтового массива
    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Ошибка дешифрования: " + e.getMessage(), e);
        }
    }

    // Восстановление ключа из байтового массива
    public static SecretKey restoreKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, AES_ALGORITHM);
    }
}