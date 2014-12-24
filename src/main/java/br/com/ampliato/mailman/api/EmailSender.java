package br.com.ampliato.mailman.api;

import javax.mail.MessagingException;

public interface EmailSender {

	public void send(Email email) throws MessagingException;
}
