package br.com.ampliato.mailman.smtp;

import br.com.ampliato.mailman.api.Email;
import br.com.ampliato.mailman.api.EmailSender;
import br.com.ampliato.mailman.api.EmailType;
import br.com.ampliato.mailman.api.Recipient;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import javax.annotation.Nonnull;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import java.util.List;

/**
 * A EmailSender that uses SMTP.
 *
 * The underlying implementation uses Apache Commons Email.
 */
public class SmtpEmailSender implements EmailSender {
	/** The address of the SMTP host. */
	private final String host;

	/** The port of the SMTP host. */
	private final int port;

	/** The username used for authentication. */
	private String username;

	/** The password used for authentication. */
	private String password;

	/**
	 * An authenticator. If an authenticator is set, the username and password are ignored.
	 */
	private Authenticator authenticator;

	/** Whether to enable STARTTLS. */
	private boolean starttlsEnabled;

	/** Whether to require STARTTLS. */
	private boolean starttlsRequired;

	/**
	 * Create a smtp email sender for the specified host.
	 *
	 * @param host
	 */
	public SmtpEmailSender(@Nonnull String host) {
		this(host, 587);
	}

	/**
	 * Create a smtp email sender for the specified host, using the specified port.
	 *
	 * @param host
	 * @param port
	 */
	public SmtpEmailSender(@Nonnull String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void send(@Nonnull Email email) throws MessagingException {
		try {
			org.apache.commons.mail.Email commonEmail = buildCommonsEmail(email);

			commonEmail.send();
		} catch (EmailException emailException) {
			throw new MessagingException(emailException.getMessage(), emailException);
		}
	}

	/**
	 * Build an email as specified by Apache Commons Email API from an Email object.
	 *
	 * @param email
	 * @return An org.apache.commons.mail.Email
	 * @throws EmailException
	 */
	protected org.apache.commons.mail.Email buildCommonsEmail(@Nonnull Email email) throws EmailException {
		org.apache.commons.mail.Email commonEmail;

		if (email.getType().equals(EmailType.PLAIN_TEXT)) {
			SimpleEmail simpleEmail = new SimpleEmail();
			simpleEmail.setMsg(email.getMessage());

			commonEmail = simpleEmail;
		} else {
			ImageHtmlEmail htmlEmail = new ImageHtmlEmail();
			htmlEmail.setHtmlMsg(email.getMessage());

			commonEmail = htmlEmail;
		}

		commonEmail.setFrom(email.getSenderAddress(), email.getSenderName());
		commonEmail.setSubject(email.getSubject());
		commonEmail.setMsg(email.getMessage());

		List<Recipient> recipients = email.getRecipients();
		if (recipients != null) {
			for (Recipient recipient : email.getRecipients()) {
				switch (recipient.getType()) {
					case BCC:
						commonEmail.addBcc(recipient.getAddress(), recipient.getName());
						break;
					case CC:
						commonEmail.addCc(recipient.getAddress(), recipient.getName());
						break;
					case TO:
						commonEmail.addTo(recipient.getAddress(), recipient.getName());
						break;
				}
			}
		}

		commonEmail.setHostName(this.host);
		commonEmail.setSmtpPort(this.port);
		commonEmail.setStartTLSEnabled(this.starttlsEnabled);
		commonEmail.setStartTLSRequired(this.starttlsRequired);

		if (this.authenticator == null) {
			commonEmail.setAuthentication(this.username, this.password);
		} else {
			commonEmail.setAuthenticator(this.authenticator);
		}

		return commonEmail;
	}

	/**
	 * Set the username and password used on a default authentication.
	 *
	 * If an authenticator is defined, this settings will be ignored.
	 *
	 * @param username
	 * @param password
	 */
	public void setAuthentication(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Authenticator getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	public boolean isStarttlsEnabled() {
		return starttlsEnabled;
	}

	public void setStarttlsEnabled(boolean starttlsEnabled) {
		this.starttlsEnabled = starttlsEnabled;
	}

	public boolean isStarttlsRequired() {
		return starttlsRequired;
	}

	public void setStarttlsRequired(boolean starttlsRequired) {
		this.starttlsRequired = starttlsRequired;
	}
}
