/*
 * Copyright 2013 by Dandelion Software & Research, Inc (DSR)
 * 
 * This application was written for immunization information system (IIS) community and has
 * been released by DSR under an Apache 2 License with the hope that this software will be used
 * to improve Public Health.  
 */
package org.openimmunizationsoftware.dqa.manager;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashManager
{

  public static String getMD5Hash(String text) throws NoSuchAlgorithmException
  {

    byte[] results;
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(text.getBytes());
    results = md.digest();
    // logger.debug(Support.viewAsHex(results));
    return viewAsHex(results);
  }

  public static String viewAsHex(String aString)
  {
    return viewAsHex(aString.getBytes());
  }

  public static String viewAsHex(byte[] bytes)
  {
    String sb = "";
    try
    {
      sb = getHexString(bytes);
    } catch (UnsupportedEncodingException e)
    {
      sb = "UnsupportedEncodingException thrown";
    }
    return sb;
  }

  static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7', (byte) '8',
      (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F' };

  public static String getHexString(byte[] raw) throws UnsupportedEncodingException
  {
    if (raw == null)
      return "";
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw)
    {
      int v = b & 0xFF;
      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }
    return new String(hex, "ASCII");
  }

  public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    int iterations = 1000;
    char[] chars = password.toCharArray();
    byte[] salt = getSalt().getBytes();

    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return iterations + ":" + toHex(salt) + ":" + toHex(hash);
  }

  private static String getSalt() throws NoSuchAlgorithmException
  {
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[16];
    sr.nextBytes(salt);
    return salt.toString();
  }

  private static String toHex(byte[] array) throws NoSuchAlgorithmException
  {
    BigInteger bi = new BigInteger(1, array);
    String hex = bi.toString(16);
    int paddingLength = (array.length * 2) - hex.length();
    if (paddingLength > 0)
    {
      return String.format("%0" + paddingLength + "d", 0) + hex;
    } else
    {
      return hex;
    }
  }
  
  public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException
  {
      String[] parts = storedPassword.split(":");
      int iterations = Integer.parseInt(parts[0]);
      byte[] salt = fromHex(parts[1]);
      byte[] hash = fromHex(parts[2]);
       
      PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] testHash = skf.generateSecret(spec).getEncoded();
       
      int diff = hash.length ^ testHash.length;
      for(int i = 0; i < hash.length && i < testHash.length; i++)
      {
          diff |= hash[i] ^ testHash[i];
      }
      return diff == 0;
  }
  private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
  {
      byte[] bytes = new byte[hex.length() / 2];
      for(int i = 0; i<bytes.length ;i++)
      {
          bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
      }
      return bytes;
  }
}
