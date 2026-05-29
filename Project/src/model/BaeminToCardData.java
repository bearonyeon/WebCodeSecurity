package model;

import java.io.Serializable;

public class BaeminToCardData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private DigitalEnvelope cardEnvelope; // 카드사용 전자봉투
	private byte[] orderInfoHash; // 주문 정보 해시
	private byte[] digitalSignature; // 이중서명
	
	public BaeminToCardData(DigitalEnvelope cardEnvelope, byte[] orderInfoHash, byte[] digitalSignature) {
		super();
		this.cardEnvelope = cardEnvelope;
		this.orderInfoHash = orderInfoHash;
		this.digitalSignature = digitalSignature;
	}

	public DigitalEnvelope getCardEnvelope() {
		return cardEnvelope;
	}

	public byte[] getOrderInfoHash() {
		return orderInfoHash;
	}

	public byte[] getDigitalSignature() {
		return digitalSignature;
	}
	
}
