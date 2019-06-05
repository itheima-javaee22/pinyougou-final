package domain;

import java.io.Serializable;

public class Excel implements Serializable{
    
	private String orderId;
	
	private String userId;
	
	private String receiver;
	
	private String reveiverMobile;
	
	private String payment;
	
	private String paymentType;
	
	private String sourceType;
	
	private String status;

	public Excel() {
	}
	public Excel(String orderId, String userId, String receiver, String reveiverMobile, String payment,
			String paymentType, String sourceType, String status) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.receiver = receiver;
		this.reveiverMobile = reveiverMobile;
		this.payment = payment;
		this.paymentType = paymentType;
		this.sourceType = sourceType;
		this.status = status;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReveiverMobile() {
		return reveiverMobile;
	}
	public void setReveiverMobile(String reveiverMobile) {
		this.reveiverMobile = reveiverMobile;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
