package entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import crypto.EnvelopeUtil;
import model.DigitalEnvelope;
import model.UserToBaeminData;

public class Baemin {
	private PrivateKey privateKey;
	
	public Baemin(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}
	
	// 사용자가 보낸 전자봉투 복호화
	public UserToBaeminData openEnvelope(DigitalEnvelope baeminEnvelope) 
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, IOException {
		byte[] decrypted = EnvelopeUtil.openEnvelope(baeminEnvelope, privateKey);
		UserToBaeminData data = (UserToBaeminData) bytesToObject(decrypted);
		return data;
	}
	
	// 가맹점 전달 데이터 생성
	// 카드사 전달 데이터 생성
	
	public Object bytesToObject(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		Object object = ois.readObject();
		return object;
	}
}
