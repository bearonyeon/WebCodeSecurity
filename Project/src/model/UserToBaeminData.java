package model;

import java.io.Serializable;

public class UserToBaeminData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private OrderInfo orderInfo; // 주문 정보
	private byte[] orderInfoHash; // 주문 정보 해시
	private byte[] paymentInfoHash; // 결제 정보 해시
	private byte[] digitalSignature; // 이중서명
	private DigitalEnvelope cardEnvelope; // 카드사용 전자봉투
	
	public UserToBaeminData (OrderInfo orderInfo, byte[] orderInfoHash, byte[] paymentInfoHash,
			byte[] digitalSignature, DigitalEnvelope cardEnvelope) {
		this.orderInfo = orderInfo;
		this.orderInfoHash = orderInfoHash;
		this.paymentInfoHash = paymentInfoHash;
		this.digitalSignature = digitalSignature;
		this.cardEnvelope = cardEnvelope;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public byte[] getOrderInfoHash() {
		return orderInfoHash;
	}

	public byte[] getPaymentInfoHash() {
		return paymentInfoHash;
	}

	public byte[] getDigitalSignature() {
		return digitalSignature;
	}

	public DigitalEnvelope getCardEnvelope() {
		return cardEnvelope;
	}
	
}
