package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;

public class AESUtil {
	// 대칭 키 생성
	public static SecretKey generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128); // 256으로 해도 됨
		SecretKey secretKey = keyGen.generateKey();
		return secretKey;
	}
	
	// 대칭 키 암호화
	public static byte[] encrypt(byte[] data, SecretKey secretKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encrypted = cipher.doFinal(data);
		return encrypted;
	}
	
	// 대칭 키 복호화
	public static byte[] decrypt(byte[] encryptedData, SecretKey secretKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decrypted = cipher.doFinal(encryptedData);
		return decrypted;
	}
	
	public static void saveKey(String fname, SecretKey secretKey) throws FileNotFoundException, IOException {
		File directory = new File("keys");
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		File file = new File(directory, fname);
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(secretKey);
		}
	}
	
	public static SecretKey loadKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			SecretKey key = (SecretKey) ois.readObject();
			return key;
		}
	}
}
