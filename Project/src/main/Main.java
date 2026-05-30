package main;

import java.security.PrivateKey;
import java.security.PublicKey;

import crypto.RSAUtil;
import entity.Baemin;
import entity.User;
import model.DigitalEnvelope;
import model.OrderInfo;
import model.PaymentInfo;
import model.UserToBaeminData;

public class Main {

	public static void main(String[] args) {
		try {
			PublicKey userPublicKey = RSAUtil.loadPublicKey("user_public.key");
			PrivateKey userPrivateKey = RSAUtil.loadPrivateKey("user_private.key");
			
			PublicKey baeminPublicKey = RSAUtil.loadPublicKey("baemin_public.key");
			PrivateKey baeminPrivateKey = RSAUtil.loadPrivateKey("baemin_private.key");
			
			PublicKey cardPublicKey = RSAUtil.loadPublicKey("card_public.key");
			PrivateKey cardPrivateKey = RSAUtil.loadPrivateKey("card_private.key");
			
			// 사용자 객체 생성
			User user = new User(userPublicKey, userPrivateKey);
			// 배민 객체 생성
			Baemin baemin = new Baemin(baeminPrivateKey);
			
			// 주문 정보 생성
			OrderInfo orderInfo = new OrderInfo("엽기떡볶이 순한 맛", 1, 14000);
			// 결제 정보 생성
			PaymentInfo paymentInfo = new PaymentInfo("1234-5678-1234-5678", "1234", 14000);
			
			// 사용자 -> 배민
			DigitalEnvelope baeminEnvelope = user.createEnvelope(orderInfo, paymentInfo, cardPublicKey, baeminPublicKey);
			System.out.println("사용자가 배민에게 전자봉투 전송 완료");
			
			// 배민에서 전자봉투 복호화
			UserToBaeminData baeminData = baemin.openEnvelope(baeminEnvelope);
			System.out.println("배민 전자봉투 복호화 완료");
			
			// 결과 출력
			 System.out.println();
	         System.out.println("===== 주문 정보 =====");
	         System.out.println("메뉴: " + baeminData.getOrderInfo().getMenu());
	         System.out.println("수량: " + baeminData.getOrderInfo().getQuantity());
	         System.out.println("가격 : " + baeminData.getOrderInfo().getPrice());
	         System.out.println();
	         System.out.println("주문 정보 해시 길이 : " + baeminData.getOrderInfoHash().length);
	         System.out.println("결제 정보 해시 길이 : " + baeminData.getPaymentInfoHash().length);
	         System.out.println("전자서명 길이 : " + baeminData.getDigitalSignature().length);
	         System.out.println();
	         System.out.println("카드사용 전자봉투 존재 여부 : " + (baeminData.getCardEnvelope() != null));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
