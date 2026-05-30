package entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
import model.BaeminToCardData;
import model.CardData;
import model.DigitalEnvelope;
import model.PaymentInfo;

public class CardCompany {
	private PrivateKey privateKey;
	
	public CardCompany(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 배달의 민족에서 보낸 전자봉투 개봉
	public BaeminToCardData openEnvelope(DigitalEnvelope cardEnvelope) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		byte[] decrypted = EnvelopeUtil.openEnvelope(cardEnvelope, privateKey);
		BaeminToCardData cardData = (BaeminToCardData) bytesToObject(decrypted);
		return cardData;
	}
	
	// 결제 정보 검증
	public boolean verifyPayment(CardData cardData, PublicKey userPublicKey) 
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		// 결제 정보 해시 생성
		PaymentInfo paymentInfo = cardData.getPaymentInfo();
		byte[] paymentInfoBytes = objectToBytes(paymentInfo);
		byte[] paymentInfoHash = HashUtil.hash(paymentInfoBytes);
		
		// 주문 정보 해시, 결제 정보 해시 결합
		byte[] orderInfoHash = cardData.getOrderInfoHash();
		byte[] combined = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
		
		// 최종 해시 값 해시
		byte[] combinedHash = HashUtil.hash(combined);
		
		// 결제 정보 검증
		byte[] digitalSignature = cardData.getDigitalSignature();
		boolean rslt = SignatureUtil.digitalSignVerify(combinedHash, digitalSignature, userPublicKey);
		return rslt;
	}
	
	public Object bytesToObject(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		Object object = ois.readObject();
		return object;
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
