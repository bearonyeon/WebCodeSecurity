package main;

import java.security.NoSuchAlgorithmException;

import crypto.HashUtil;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println();
		System.out.println("===== HASH TEST =====");

		// 주문 정보
		String orderInfo =
		        "엽기떡볶이 2인분";

		// 결제 정보
		String paymentInfo =
		        "카드번호:1234-5678";

		// OIH 생성
		byte[] OIH =
		        HashUtil.hash(orderInfo.getBytes());

		System.out.println(
		        "OIH: "
		                + HashUtil.bytesToHex(OIH));

		// PIH 생성
		byte[] PIH =
		        HashUtil.hash(paymentInfo.getBytes());

		System.out.println(
		        "PIH: "
		                + HashUtil.bytesToHex(PIH));

		// OIH + PIH 결합
		byte[] combined =
		        HashUtil.combineHash(OIH, PIH);

		// POMD 생성
		byte[] POMD =
		        HashUtil.hash(combined);

		System.out.println(
		        "POMD: "
		                + HashUtil.bytesToHex(POMD));
	}

}
