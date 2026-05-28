package model;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String cardNumber;
	private String cardPassword;
	private int price;
	
	public PaymentInfo(String cardNumber, String cardPassword, int price) {
		this.cardNumber = cardNumber;
		this.cardPassword = cardPassword;
		this.price = price;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardPassword() {
		return cardPassword;
	}

	public int getPrice() {
		return price;
	}
	
	public String toString() {
		String payment = "카드번호: " + cardNumber + ", 결제금액: " + price;
		return payment;
	}
	
}
