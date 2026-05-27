package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {
	// 비대칭 키 생성
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024); // 2048로 수정해도 됨(시간 오래 걸림)
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return keyPair;
	}
	
	// 공개 키 암호화
	public static byte[] encrypt(byte[] data, PublicKey publicKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encrypted = cipher.doFinal(data);
		return encrypted;
	}
	
	// 개인 키 복호화
	public static byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) 
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decrypted = cipher.doFinal(encryptedData);
		return decrypted;
	}
	
	// 공개 키 저장
	public static void savePublicKey(String fname, PublicKey publicKey) throws FileNotFoundException, IOException {
		File directory = new File("keys");
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		File file = new File(directory, fname);
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(publicKey);
		}
	}
	
	// 개인 키 저장
	public static void savePrivateKey(String fname, PrivateKey privateKey) throws FileNotFoundException, IOException {
		File directory = new File("keys");
		
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		File file = new File(directory, fname);
		
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
			oos.writeObject(privateKey);
		}
	}
	
	// 공개 키 불러오기
	public static PublicKey loadPublicKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			PublicKey publicKey = (PublicKey) ois.readObject();
			return publicKey;
		}
	}
	
	// 개인 키 불러오기
	public static PrivateKey loadPrivateKey(String fname) throws FileNotFoundException, IOException, ClassNotFoundException {
		File directory = new File("keys");
		File file = new File(directory, fname);
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			PrivateKey privateKey = (PrivateKey) ois.readObject();
			return privateKey;
		}
	}
}
