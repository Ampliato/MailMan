package br.com.ampliato.mailman.api;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an email.
 */
public final class Email {

	/** The type of this email. */
	private final EmailType type;

	/** The subject of this email. */
	private String subject;

	/** The message of this email. */
	private String message;

	/** The address of the sender of this email. */
	private String senderName;

	/** The name of the sender of this email. */
	private String senderAddress;

	/** The recipients of this email. */
	private List<Recipient> recipients;

	/**
	 * Create a new plain text email.
	 */
	public Email () {
		this(EmailType.PLAIN_TEXT);
	}

	/**
	 * Create a new email of the specified type.
	 *
	 * @param type
	 */
	public Email (@Nonnull EmailType type) {
		super();

		this.type = type;
	}

	/**
	 * Add a recipient to the recipients list.
	 * @param recipient
	 */
	public void addRecipient(@Nonnull Recipient recipient) {
		if (this.recipients == null) {
			this.recipients = new ArrayList<>();
		}

		this.recipients.add(recipient);
	}

	/**
	 * Add a recipient to the recipients list.
	 *
	 * @param type
	 * @param name
	 * @param address
	 */
	public void addRecipient(@Nonnull RecipientType type, String name, @Nonnull String address) {
		this.addRecipient(new Recipient(type, name, address));
	}

	/**
	 * Add a recipient to the recipients list.
	 *
 	 * @param type
	 * @param address
	 */
	public void addRecipient(@Nonnull RecipientType type, @Nonnull String address) {
		this.addRecipient(new Recipient(type, null, address));
	}

	/**
	 * Set the sender of this email.
	 *
	 * @param senderAddress
	 * @param senderName
	 */
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
