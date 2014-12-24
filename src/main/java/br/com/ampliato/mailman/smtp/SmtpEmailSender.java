package br.com.ampliato.mailman.smtp;

import br.com.ampliato.mailman.api.Email;
import br.com.ampliato.mailman.api.EmailSender;
import br.com.ampliato.mailman.api.EmailType;
import br.com.ampliato.mailman.api.Recipient;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import java.util.List;

public class SmtpEmailSender implements EmailSender {
	private String hostName;
	private int smtpPort;
	private String username, password;
	private Authenticator authenticator;
	private boolean startTlsEnabled;
	private boolean startTlsRequired;

	public SmtpEmailSender(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public void send(Email email) throws MessagingException {
		try {
			org.apache.commons.mail.Email commonEmail = buildCommonEmail(email);

			commonEmail.send();
		} catch (EmailException emailException) {
			throw new MessagingException(emailException.getMessage(), emailException);
		}
	}

	protected org.apache.commons.mail.Email buildCommonEmail(Email email) throws EmailException {
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

		commonEmail.setHostName(this.hostName);
		commonEmail.setSmtpPort(this.smtpPort);
		commonEmail.setStartTLSEnabled(this.startTlsEnabled);
		commonEmail.setStartTLSRequired(this.startTlsRequired);

		if (this.authenticator == null) {
			commonEmail.setAuthentication(this.username, this.password);
		} else {
			commonEmail.setAuthenticator(this.authenticator);
		}

		return commonEmail;
	}

	public void setAuthentication(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
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

	public boolean isStartTlsEnabled() {
		return startTlsEnabled;
	}

	public void setStartTlsEnabled(boolean startTlsEnabled) {
		this.startTlsEnabled = startTlsEnabled;
	}

	public boolean isStartTlsRequired() {
		return startTlsRequired;
	}

	public void setStartTlsRequired(boolean startTlsRequired) {
		this.startTlsRequired = startTlsRequired;
	}
}
