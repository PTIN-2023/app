package com.example.appptin;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import android.util.Base64;

public class EncryptionUtils {
    private static final String SECRET_KEY = "jwt1234-piramide-quadrada-gos-salvatge-iceberg-piña-Meren";

    public static String encryptData(String data) {
        try {
            // Converteix la clau secreta en una instància de SecretKey
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

            // Crea un objecte Cipher amb l'algorisme AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // Genera un vector d'inicialització aleatori
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[cipher.getBlockSize()];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Inicialitza el Cipher amb la clau secreta i el vector d'inicialització
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encripta les dades
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Combina el vector d'inicialització i les dades encriptades en una sola estructura
            byte[] combinedBytes = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combinedBytes, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combinedBytes, iv.length, encryptedBytes.length);

            // Retorna les dades encriptades com una cadena de text Base64
            return Base64.encodeToString(combinedBytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}