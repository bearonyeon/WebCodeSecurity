package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import model.DigitalEnvelope;

public class EnvelopeUtil {
	// 전자봉투 생성
	public static DigitalEnvelope sealEnvelope(byte[] data, PublicKey publicKey) 
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// AES 키 생성
		SecretKey aesKey = AESUtil.generateKey();
		
		// 결제 정보 AES 암호화
		byte[] encryptedData = AESUtil.encrypt(data, aesKey);
		
		// AES 키를 공개 키로 암호화
		byte[] aesKeyBytes = aesKey.getEncoded();
		byte[] encryptedKey = RSAUtil.encrypt(aesKeyBytes, publicKey);
		
		// 전자봉투 생성
		DigitalEnvelope digitalEnvelope = new DigitalEnvelope(encryptedData, encryptedKey);
		return digitalEnvelope;
	}
	
	// 전자봉투 개봉
	public static byte[] openEnvelope(DigitalEnvelope envelope, PrivateKey privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 공개 키로 암호화된 AES 키를 개인 키로 복호화
		byte[] aesKeyBytes = RSAUtil.decrypt(envelope.getEncryptedKey(), privateKey);
		
		// byte[] 형태의 키를 SecretKey 타입으로 복원
		SecretKey aesKey = new SecretKeySpec(aesKeyBytes, "AES");
		
		// 데이터 복호화
		byte[] data = AESUtil.decrypt(envelope.getEncryptedData(), aesKey);
		return data;
	}
}
