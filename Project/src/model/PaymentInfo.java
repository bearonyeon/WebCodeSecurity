package model;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String cardNumber;
	private int price;
	
	public PaymentInfo(String cardNumber, int price) {
		this.cardNumber = cardNumber;
		this.price = price;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public int getPrice() {
		return price;
	}
	
	public String toString() {
		String payment = "카드번호: " + cardNumber + ", 결제금액: " + price;
		return payment;
	}
	
}
