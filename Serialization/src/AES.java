

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;





public class AES {
	
	private static final StringBuffer myKey=new StringBuffer("PC4152016");
	// init vector for randomness
	static byte[] iv = { (byte) 0xcb, (byte) 0x53, (byte) 0x03, (byte) 0x0f,
			(byte) 0xe0, (byte) 0x79, (byte) 0x9d, (byte) 0xdc, (byte) 0x80,
			(byte) 0xa9, (byte) 0x83, (byte) 0xf1, (byte) 0x03, (byte) 0xb6,
			(byte) 0x59, (byte) 0x83 };

	// ========================================= CRYPTO STUFF
	public static byte[] md5sum(final byte[] buffer) {

		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA-224");
			md5.update(buffer);
			return md5.digest();
		} catch (NoSuchAlgorithmException e) {
		
		}
		return null;
	}

	public static byte[] decrypt(final byte[] cipherText, byte[] key) {
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			SecretKey secretKey = new SecretKeySpec(key, "AES");
			Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aes.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
			byte[] plainText = aes.doFinal(cipherText);
			return plainText;
		} catch (Exception e) {
			
		}
		return null;
	}

	public static byte[] encrypt(final byte[] plainText, byte[] key) {
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			SecretKey secretKey = new SecretKeySpec(key, "AES");
			Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aes.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
			byte[] cipherText = aes.doFinal(plainText);
			return cipherText;
		} catch (Exception e) {
			
		}
		return null;
	}

	// =============================================== HELPERS
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append((char) bytes[i]);
		}
		return stringBuffer.toString();
	}

	public static String encryptString(String plain, StringBuffer password)
			throws Exception {
		byte[] tmp = null;
		try {
			tmp = plain.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
			throw new Exception("Error in encrypting String", uee);
		}
	
		byte[] key = getKeyFromPassword(password);
		byte[] ct = encrypt(tmp, key);
		return byteArrayToHexString(ct);
	}

	public static String decryptString(String encrypted, StringBuffer password)
			throws Exception {
		byte[] tmp = hexStringToByteArray(encrypted);
		byte[] key = getKeyFromPassword(password);
		byte[] pt = decrypt(tmp, key);
		return bytes2String(pt);
	}

	// converts key to machine readable form
	public static byte[] getKeyFromPassword(StringBuffer s) {
		return md5sum(s.toString().getBytes());
	}
	public static String encode(String plain) throws Exception{
		return encryptString(plain,myKey);
	}
	public static String decode(String encrypted) throws Exception{
		return decryptString(encrypted,myKey) ;
	}
	/*public static void main(String args[]){
		try {
			System.out.println("Encode 'rules'"+encode("rules"));
			System.out.println("decode 'rules'"+decode("F95430B60488BB87B4FB1AA4E0F968A0"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}