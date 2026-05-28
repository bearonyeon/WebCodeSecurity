package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import crypto.EnvelopeUtil;
import crypto.HashUtil;
import crypto.RSAUtil;
import crypto.SignatureUtil;
import model.DigitalEnvelope;

public class Main {

	public static void main(String[] args) 
			throws FileNotFoundException, ClassNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		System.out.println("===== HASH TEST =====");

		// 주문 정보
		String orderInfo = "엽기떡볶이 2인분";

		// 결제 정보
		String paymentInfo =	"카드번호:1234-5678";

		// OIH 생성
		byte[] OIH = HashUtil.hash(orderInfo.getBytes());

		System.out.println("OIH: " + HashUtil.bytesToHex(OIH));

		// PIH 생성
		byte[] PIH = HashUtil.hash(paymentInfo.getBytes());

		System.out.println("PIH: " + HashUtil.bytesToHex(PIH));

		// OIH + PIH 결합
		byte[] combined = HashUtil.combineHash(OIH, PIH);

		// POMD 생성
		byte[] POMD = HashUtil.hash(combined);

		System.out.println("POMD: " + HashUtil.bytesToHex(POMD));
		
		System.out.println("===== DIGITAL SIGNATURE TEST =====");

		// 사용자 개인키 로드
		PrivateKey userPrivateKey = RSAUtil.loadPrivateKey("user_private.key");

		// 사용자 공개키 로드
		PublicKey userPublicKey = RSAUtil.loadPublicKey("user_public.key");

		// POMD 전자서명 생성
		byte[] digitalSignature = SignatureUtil.digitalSign(POMD, userPrivateKey);

		System.out.println("전자서명 생성 완료");

		// 전자서명 검증
		boolean isVerified = SignatureUtil.digitalSignVerify(POMD, digitalSignature, userPublicKey);

		System.out.println("전자서명 검증 결과: " + isVerified);
		
		System.out.println();
		System.out.println("===== DIGITAL ENVELOPE TEST =====");

		// 전송할 데이터
		String secretMessage = "결제 정보: 1234-5678";

		// 전자봉투 생성
		DigitalEnvelope envelope = EnvelopeUtil.sealEnvelope(secretMessage.getBytes(), userPublicKey);

		System.out.println("전자봉투 생성 완료");

		// 전자봉투 개봉
		byte[] openedData = EnvelopeUtil.openEnvelope(envelope, userPrivateKey);

		String result = new String(openedData);

		System.out.println("전자봉투 복호화 결과: " + result);
	}
}
