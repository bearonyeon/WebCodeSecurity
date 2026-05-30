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
import model.BaeminToStoreData;
import model.DigitalEnvelope;
import model.OrderInfo;

public class Store {
	private PrivateKey privateKey;
	
	public Store (PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 배달의 민족이 보낸 전자봉투 개봉
	public BaeminToStoreData openEnvelope(DigitalEnvelope storeEnvelope) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		byte[] decrypted = EnvelopeUtil.openEnvelope(storeEnvelope, privateKey);
		BaeminToStoreData storeData = (BaeminToStoreData) bytesToObject(decrypted);
		return storeData;
	}
	
	// 주문 정보 검증
	public boolean verifyOrder(BaeminToStoreData storeData, PublicKey userPublicKey) 
			throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		// 주문 정보 해시 생성
		OrderInfo orderInfo = storeData.getOrderInfo();
		byte[] orderInfoBytes = objectToBytes(orderInfo);
		byte[] orderInfoHash = HashUtil.hash(orderInfoBytes);
		
		// 주문 정보 해시, 결제 정보 해시 결합
		byte[] paymentInfoHash = storeData.getPaymentInfoHash();
		byte[] combined = HashUtil.combineHash(orderInfoHash, paymentInfoHash);
		
		// 최종 해시 값 해시
		byte[] combinedHash = HashUtil.hash(combined);
		
		// 이중서명 검증
		byte[] digitalSignature = storeData.getDigitalSignature();
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
