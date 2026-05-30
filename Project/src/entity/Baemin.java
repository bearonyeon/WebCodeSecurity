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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import crypto.EnvelopeUtil;
import model.BaeminToCardData;
import model.BaeminToStoreData;
import model.DigitalEnvelope;
import model.UserToBaeminData;

public class Baemin {
	private PrivateKey privateKey;
	
	public Baemin(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 사용자가 보낸 전자봉투 개봉
	public UserToBaeminData openEnvelope(DigitalEnvelope baeminEnvelope) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		byte[] decrypted = EnvelopeUtil.openEnvelope(baeminEnvelope, privateKey);
		UserToBaeminData baeminData = (UserToBaeminData) bytesToObject(decrypted);
		return baeminData;
	}
	
	// 가맹점 전달 데이터 생성
	public BaeminToStoreData generateStoreData(UserToBaeminData data) {
		BaeminToStoreData storeData = new BaeminToStoreData(data.getOrderInfo(), data.getPaymentInfoHash(), data.getDigitalSignature());
		return storeData;
	}
	
	// 가맹점 전달 전자봉투 생성
	public DigitalEnvelope createStoreEnvelope(BaeminToStoreData storeData, PublicKey storePublicKey) 
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] storeDataBytes = objectToBytes(storeData);
		DigitalEnvelope storeEnvelope = EnvelopeUtil.sealEnvelope(storeDataBytes, storePublicKey);
		return storeEnvelope;
	}
	
	// 카드사 전달 데이터 생성
	public BaeminToCardData generateCardData(UserToBaeminData data) {
		BaeminToCardData cardData = new BaeminToCardData(data.getCardEnvelope());
		return cardData;
	}
	
	// 카드사 전달 전자봉투 생성
	public DigitalEnvelope createCardEnvelope(BaeminToCardData cardData, PublicKey cardPublicKey) 
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] cardDataBytes = objectToBytes(cardData);
		DigitalEnvelope cardEnvelope = EnvelopeUtil.sealEnvelope(cardDataBytes, cardPublicKey);
		return cardEnvelope;
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
