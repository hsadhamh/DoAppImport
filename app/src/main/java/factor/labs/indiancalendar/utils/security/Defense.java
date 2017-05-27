package factor.labs.indiancalendar.utils.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Defense {
	private String mEncodeString = "shLab!17KctIanay";
	private Cipher mCipher = null; 
	private Key mAesKey = null; 
	
	private static Defense encode = null;
	
	public Defense() 
			throws NoSuchAlgorithmException, NoSuchPaddingException{
		mCipher = Cipher.getInstance("AES");
		mAesKey = new SecretKeySpec(mEncodeString.getBytes(), "AES");
	}
	
	public String encryptData(String sData) 
			throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		mCipher.init(Cipher.ENCRYPT_MODE, mAesKey);
		byte[] encrypted = mCipher.doFinal(sData.getBytes("UTF-8"));
		return encodeIntoBase64(encrypted);
	}
	
	
	public String decryptData(String encrypted) 
			throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException{
		mCipher.init(Cipher.DECRYPT_MODE, mAesKey);
		String decrypted = new String(mCipher.doFinal(decodeIntoBase64(encrypted)), "UTF-8");
		return decrypted;
	}
	
	private byte[] decodeIntoBase64(String  encryptedString) throws UnsupportedEncodingException{
		return Base64.decode(encryptedString.getBytes("UTF-8"), 0);
	}
	
	private String encodeIntoBase64(byte[] bData) throws UnsupportedEncodingException{
		byte[] finalBytes = Base64.encode(bData, 0);
		String encryptedString = new String(finalBytes, "UTF-8");
		return encryptedString;
	}
	
	public static Defense getInstance() 
			throws NoSuchAlgorithmException, NoSuchPaddingException{
		if(encode == null)
			encode = new Defense();
		return encode;
	}
}
