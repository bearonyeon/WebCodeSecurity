package model;

import java.io.Serializable;

public class CardData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private PaymentInfo paymentInfo; // 결제 정보
	private byte[] orderInfoHash; // 주문 정보 해시
	private byte[] digitalSignature; // 이중서명
	
	public CardData(PaymentInfo paymentInfo, byte[] orderInfoHash, byte[] digitalSignature) {
		this.paymentInfo = paymentInfo;
		this.orderInfoHash = orderInfoHash;
		this.digitalSignature = digitalSignature;
	}

	public PaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public byte[] getOrderInfoHash() {
		return orderInfoHash;
	}

	public byte[] getDigitalSignature() {
		return digitalSignature;
	}

}
