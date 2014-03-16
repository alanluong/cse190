package com.example.bidit;

public class Message {
	User sender;
	User receiver;
	String content;
	String subject;
	
	public Message(User s, User r, String c, String subject){
		setSender(s);
		setReceiver(r);
		setContent(c);
		setSubject(subject);
	}
	


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
