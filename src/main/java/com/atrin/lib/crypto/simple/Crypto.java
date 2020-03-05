package com.atrin.lib.crypto.simple;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class Crypto{

	// public static encryptDES(String raw_text,String key){
	// 	SecretKeySpec sec_key = new SecretKeySpec(key.getBytes(), "DES");
	// 	IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
	// 	Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	// 	cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
	// 	byte[] text = raw_text.getBytes();
	// 	byte[] textEncrypted = cipher.doFinal(text);
	// 	System.out.println(textEncrypted);
	// }
}