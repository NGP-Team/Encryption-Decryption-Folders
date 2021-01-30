/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finallabsecuirty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;



public class FinalLabSecuirty {

     private static String ALGORITHM;
    private static String TRANSFORMATION;

    public static void encrypt(String Algorithm, String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, Algorithm, key, inputFile, outputFile);
    }

    public static void decrypt(String Algorithm, String key, File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, Algorithm, key, inputFile, outputFile);
    }

    private static void doCrypto(int cipherMode, String Algorithm, String key, File inputFile,
            File outputFile) throws CryptoException {

        if (Algorithm.equalsIgnoreCase("AES")) {
            ALGORITHM = "AES";
            TRANSFORMATION = "AES";
        } else if (Algorithm.equalsIgnoreCase("DES")) {
            ALGORITHM = "DES";
            TRANSFORMATION = "DES/ECB/PKCS5Padding";
        }

        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
}

class CryptoException extends Exception {

    public CryptoException() {
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
   
    
}
