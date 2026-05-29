package model;

import java.io.Serializable;

public class BaeminToStoreData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private OrderInfo orderInfo;
	private byte[] paymentInfoHash;
	private byte[] digitalSignature;
	
	public BaeminToStoreData(OrderInfo orderInfo, byte[] orderInfoHash, byte[] paymentInfoHash, byte[] digitalSignature) {
		this.orderInfo = orderInfo;
		this.paymentInfoHash = paymentInfoHash;
		this.digitalSignature = digitalSignature;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public byte[] getPaymentInfoHash() {
		return paymentInfoHash;
	}

	public byte[] getDigitalSignature() {
		return digitalSignature;
	}
	
}
