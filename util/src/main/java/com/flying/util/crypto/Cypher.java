/*
  File: Cypher.java
  Originally written by Walker.

  Rivision History:
  Date         Who     Version  What
  2015.1.4     walker  0.1.0    Create this file.
 */
package com.flying.util.crypto;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Cypher {
  public static final char CYPHER_STRING_BEGIN_CHAR = '{';
  public static final char CYPHER_STRING_END_CHAR = '}';

  private static final byte[] ENCRYPTION_SALT = {0X5, 0X1, 0X1, 0X1, 0X5, 0X1, 0X1, 0X1};
  private static final int ENCRYPTION_ITERATION_COUNT = 8;
  private static final String HEXSTRING = "0123456789ABCDEF";

  private static final byte getIndex(char c) {
    return (byte) HEXSTRING.indexOf(c);
  }

  /**
   * Converts a byte to hex digit and writes to the supplied buffer
   *
   * @param b      byte to process
   * @param buffer supplied buffer
   */
  private static void byte2hex(byte b, StringBuffer buffer) {
    int high = ((b & 0xf0) >> 4);
    int low = (b & 0x0f);
    buffer.append(HEXSTRING.charAt(high));
    buffer.append(HEXSTRING.charAt(low));
  }

  /**
   * Converts two chars to byte and write to specified byte array.
   *
   * @param high  high digit  char
   * @param low   low digit char
   * @param block specified byte array.
   * @param index byte index.
   */
  private static void hex2byte(char high, char low, byte[] block, int index) {
    byte bhigh = getIndex(high);
    byte blow = getIndex(low);
    block[index] = (byte) ((bhigh << 4) | blow);
  }

  /**
   * Convert hexString to byte array
   *
   * @param cipher Hex string generate by toHexString method, The cipher string should
   *               be {[0-9]*[A-F]*}
   * @return byte array.
   */
  private static byte[] toByteArray(String cipher) {
    String hexString = cipher.substring(1, cipher.length() - 1);
    int len = hexString.length() / 2;
    byte[] block = new byte[len];
    for (int i = 0; i < len; i++) {
      hex2byte(hexString.charAt(i * 2), hexString.charAt(i * 2 + 1), block, i);
    }
    return block;
  }

  /**
   * Converts a byte array to hex string, it's output should be {[0-9]*[A-F]*}
   *
   * @param block byte array to be hexed
   * @return hex string that can output or print.
   */
  private static String toHexString(byte[] block) {
    StringBuffer buf = new StringBuffer();
    buf.append(CYPHER_STRING_BEGIN_CHAR);
    int len = block.length;
    for (int i = 0; i < len; i++) {
      byte2hex(block[i], buf);
    }
    buf.append(CYPHER_STRING_END_CHAR);
    return buf.toString();
  }

  public static String encrypt(String plain) throws CypherException {
    try {
      PBEKeySpec pbeKeySpec;
      PBEParameterSpec pbeParamSpec;
      SecretKeyFactory keyFac;
      // Create PBE parameter set
      pbeParamSpec = new PBEParameterSpec(ENCRYPTION_SALT, ENCRYPTION_ITERATION_COUNT);
      pbeKeySpec = new PBEKeySpec(HEXSTRING.toCharArray());
      keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
      // Create PBE Cipher
      Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
      // Initialize PBE Cipher with key and parameters
      pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
      // Our cleartext
      byte[] cleartext = plain.getBytes();
      // Encrypt the cleartext
      byte[] ciphertext = pbeCipher.doFinal(cleartext);
      return toHexString(ciphertext);
    }
    catch (Exception e) {
      throw new CypherException("Error when encrypt!", e);
    }
  }

  public static String decrypt(String cipher) throws CypherException {
    try {
      PBEKeySpec pbeKeySpec;
      PBEParameterSpec pbeParamSpec;
      SecretKeyFactory keyFactory;
      // Create PBE parameter set
      pbeParamSpec = new PBEParameterSpec(ENCRYPTION_SALT, ENCRYPTION_ITERATION_COUNT);
      pbeKeySpec = new PBEKeySpec(HEXSTRING.toCharArray());
      keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
      // Create PBE Cipher
      Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
      // Initialize PBE Cipher with key and parameters
      pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
      // Our cleartext
      byte[] ciphertext = toByteArray(cipher);
      // Encrypt the cleartext
      byte[] plaintext = pbeCipher.doFinal(ciphertext);
      return new String(plaintext);
    }
    catch (Exception e) {
      throw new CypherException("Error when encrypt!", e);
    }
  }
}
