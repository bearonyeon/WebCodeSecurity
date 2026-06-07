package crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import model.DigitalEnvelope;

public class EnvelopeUtil {
	// 전자봉투 생성
	public static DigitalEnvelope sealEnvelope(byte[] data, PublicKey publicKey) 
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// AES 키 생성
		SecretKey aesKey = AESUtil.generateKey();
		
		// 결제 정보 AES 암호화
		byte[] encryptedData = AESUtil.encrypt(data, aesKey);
		
		// AES 키를 RSA 공개 키로 암호화
		byte[] aesKeyBytes = aesKey.getEncoded();
		byte[] encryptedKey;
		
		try {
		    encryptedKey = RSAUtil.encrypt(aesKeyBytes, publicKey);
		} finally {
			// 메모리에 남아있는 AES 키 바이트 배열 제거
		    Arrays.fill(aesKeyBytes, (byte) 0);
		}
		
		// 전자봉투 생성 후 반환
		return new DigitalEnvelope(encryptedData, encryptedKey);
	}
	
	// 전자봉투 개봉
	public static byte[] openEnvelope(DigitalEnvelope envelope, PrivateKey privateKey) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// RSA 공개 키로 암호화된 AES 키를 RSA 개인 키로 복호화
		byte[] aesKeyBytes = RSAUtil.decrypt(envelope.getEncryptedKey(), privateKey);
		
		SecretKey aesKey;
		try {     
			// byte[] 형태의 키를 SecretKey 타입으로 복원
	        aesKey = new SecretKeySpec(aesKeyBytes, "AES");
	        // 데이터 복호화 후 반환
	        return AESUtil.decrypt(envelope.getEncryptedData(), aesKey);
		} finally {
			// 복호화 직후 메모리에 남은 평문 바이트 배열 제거
			Arrays.fill(aesKeyBytes, (byte) 0);
		}
	}
}
