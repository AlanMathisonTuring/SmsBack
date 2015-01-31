package com.nutegg.smsback.domain;

import java.io.Serializable;


public class SmsInfo implements Serializable{
	private int id;
	private Long date ;
	private int type;
	private String content;
	private String address;
	
	@Override
	public String toString() {
		return "SmsInfo [短信发送时间:" + date + ", 短信内容:" + content + ", 收件人:" + address + "]";
	}

	public SmsInfo() {
		
	}
	
	public SmsInfo(int id, Long date, int type, String content, String address) {
		super();
		this.id = id;
		this.date = date;
		this.type = type;
		this.content = content;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
