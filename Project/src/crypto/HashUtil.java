package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	// SHA-256 해시 생성
	public static byte[] hash(byte[] data) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(data);
		
		byte[] mdSHA256 = md.digest();
		return mdSHA256;
	}
	
	// byte[]를 hex 문자열로 변환
	public static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	// 두 개의 해시 결합
	public static byte[] combineHash(byte[] hash1, byte[] hash2) {
		byte[] combined = new byte[hash1.length + hash2.length];
		System.arraycopy(hash1, 0, combined, 0, hash1.length);
		System.arraycopy(hash2, 0, combined, hash1.length, hash2.length);
		return combined;
	}
}
