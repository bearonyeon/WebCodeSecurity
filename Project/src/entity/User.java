package entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
import crypto.SignatureUtil;
import model.CardData;
import model.DigitalEnvelope;
import model.OrderInfo;
import model.PaymentInfo;
import model.UserToBaeminData;

public class User {
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	public User(PublicKey publicKey, PrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	// 사용자가 배달의 민족으로 보낼 데이터 생성
	public UserToBaeminData createData(OrderInfo orderInfo, PaymentInfo paymentInfo, PublicKey cardPublicKey) 
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		// 주문 정보 해시 생성
		byte[] orderInfoBytes = objectToBytes(orderInfo);
		byte[] orderInfoHash = HashUtil.hash(orderInfoBytes);
		
		// 결제 정보 해시 생성
		byte[] paymentInfoBytes = objectToBytes(paymentInfo);
		byte[] paymentInfoHash = HashUtil.hash(paymentInfoBytes);
		
		// 주문, 결제 정보 해시 값 결합
		byte[] combinedHash = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
		
		// 최종 해시 값 (주문, 결제 정보 해시 값 결합에 해시)
		byte[] pomd = HashUtil.hash(combinedHash);
		
		// 이중서명 생성
		byte[] digitalSignature = SignatureUtil.digitalSign(pomd, privateKey);
		
		// 카드사 전달 데이터 생성
		CardData cardData = new CardData(paymentInfo, orderInfoHash, digitalSignature);
		byte[] cardDataBytes = objectToBytes(cardData);
		
		// 카드사용 전자봉투 생성
		DigitalEnvelope cardEnvelope = EnvelopeUtil.sealEnvelope(cardDataBytes, cardPublicKey);
		
		// 배달의 민족 전달 데이터 생성
		UserToBaeminData data = new UserToBaeminData(orderInfo, orderInfoHash, paymentInfoHash, digitalSignature, cardEnvelope);
		return data;
	}
	
	// 객체 직렬화
	public byte[] objectToBytes(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
			
		oos.writeObject(obj);
		oos.flush();
			
		byte[] bytes = bos.toByteArray();
		return bytes;
	}
}
