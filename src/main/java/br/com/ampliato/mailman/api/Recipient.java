package br.com.ampliato.mailman.api;

public class Recipient {

	private RecipientType type;
	private String address;
	private String name;

	public Recipient(RecipientType type, String name, String address) {
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
