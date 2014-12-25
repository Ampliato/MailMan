package br.com.ampliato.mailman.api;

import javax.annotation.Nonnull;
import javax.mail.MessagingException;

/**
 * Interface that defines the email sending capability.
 */
public interface EmailSender {

	/**
	 * Send the email.
	 *
	 * @param email
	 * @throws MessagingException
	 */
	public void send(@Nonnull Email email) throws MessagingException;
}
