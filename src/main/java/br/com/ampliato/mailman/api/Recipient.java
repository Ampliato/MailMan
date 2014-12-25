package br.com.ampliato.mailman.api;

import javax.annotation.Nonnull;

/**
 * Representation of an email recipient.
 */
public class Recipient {

	/** The type of this recipient. */
	private RecipientType type;

	/** The address of this recipient. */
	private String address;

	/** The name of this recipient. */
	private String name;

	/**
	 * Create a new recipient.
	 *
	 * @param type
	 * @param name
	 * @param address
	 */
	public Recipient(@Nonnull RecipientType type, String name, @Nonnull String address) {
		this.type = type;
		this.name = name;
		this.address = address;
	}

	public RecipientType getType() {
		return type;
	}

	public void setType(RecipientType type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
