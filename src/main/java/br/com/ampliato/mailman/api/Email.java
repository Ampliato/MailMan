package br.com.ampliato.mailman.api;

import java.util.ArrayList;
import java.util.List;

public class Email {

	private final EmailType type;
	private String subject;
	private String message;

	private String senderAddress;
	private String senderName;
	private List<Recipient> recipients;

	public Email () {
		this(EmailType.PLAIN_TEXT);
	}

	public Email (EmailType type) {
		super();

		this.type = type;
	}

	public void addRecipient(Recipient recipient) {
		if (this.recipients == null) {
			this.recipients = new ArrayList<>();
		}

		this.recipients.add(recipient);
	}

	public void addRecipient(RecipientType type, String name, String address) {
		this.addRecipient(new Recipient(type, name, address));
	}

	public void addRecipient(RecipientType type, String address) {
		this.addRecipient(new Recipient(type, null, address));
	}

	public void setFrom(String senderAddress, String senderName) {
		this.setSenderAddress(senderAddress);
		this.setSenderName(senderName);
	}

	public EmailType getType() {
		return type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}
}
