/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LichLibrary.config;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Base64;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author eater48
 */
public class ED {
    //en and dec for android and desktop
  private static String key2 = "lichlibrary";
  private static String key1 = "452864";

  public static byte[] hexStr2Bytes(String src) {
    int m = 0, n = 0;
    int l = src.length() / 2;
    byte[] ret = new byte[l];
    for (int i = 0; i < l; i++) {
      m = i * 2 + 1;//  w  w  w  . ja  v a  2 s  .c om
      n = m + 1;
      ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
    }
    return ret;
  }
  

  public String decryptionKey(String password) {
    byte[] keyByte1 = key1.getBytes();
    byte[] keyByte2 = key2.getBytes();
    // password = hexStr2Str(password);
    byte[] pwdByte = hexStr2Bytes(password);

    for (int i = 0; i < pwdByte.length; i++) {
      pwdByte[i] = (byte) (pwdByte[i] ^ keyByte2[i % keyByte2.length]);
    }

    byte[] lastByte = new byte[pwdByte.length - keyByte1.length];
    for (int i = 0; i < lastByte.length; i++) {
      lastByte[i] = pwdByte[i];
    }
    for (int i = 0; i < lastByte.length; i++) {
      lastByte[i] = (byte) (lastByte[i] ^ keyByte1[i % keyByte1.length]);
    }

    return new String(lastByte);
  }

  public static final String bytesToHexString(byte[] bArray) {
    StringBuffer sb = new StringBuffer(bArray.length);
    String sTemp;
    for (int i = 0; i < bArray.length; i++) {
      sTemp = Integer.toHexString(0xFF & bArray[i]);
      if (sTemp.length() < 2)
        sb.append(0);
      sb.append(sTemp.toUpperCase());
    }
    return sb.toString();
  }

  public String encryptionKey(String password) {
    byte[] keyByte1 = key1.getBytes();
    byte[] keyByte2 = key2.getBytes();
    byte[] pwdByte = password.getBytes();
    for (int i = 0; i < pwdByte.length; i++) {
      pwdByte[i] = (byte) (pwdByte[i] ^ keyByte1[i % keyByte1.length]);
    }
    byte[] countByte = new byte[pwdByte.length + keyByte1.length];
    for (int i = 0; i < countByte.length; i++) {
      if (i < pwdByte.length)
        countByte[i] = pwdByte[i];
      else
        countByte[i] = keyByte1[i - pwdByte.length];
    }
    for (int i = 0; i < countByte.length; i++) {
      countByte[i] = (byte) (countByte[i] ^ keyByte2[i % keyByte2.length]);
    }
    return bytesToHexString(countByte);
  }
//    public void test() {
//        String x = encryptionKey("admin");
//        String d = decryptionKey("39383C39345D574059444D");
//        System.out.println(x+"\n"+d);
//    }
//    public static void main(String[] args) {
//        ED ed = new ED();
//        ed.test();
//    }
}

