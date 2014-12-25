package br.com.ampliato.mailman.smtp;

import br.com.ampliato.mailman.api.Email;
import br.com.ampliato.mailman.api.EmailType;
import br.com.ampliato.mailman.api.Recipient;
import br.com.ampliato.mailman.api.RecipientType;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.junit.Before;
import org.junit.Test;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.util.List;

import static org.junit.Assert.*;

public class SmtpEmailSenderTest {
	private final String SENDER_ADDRESS = "sender@test.host.com";
	private final String SENDER_NAME = "Sender Name";
	private final String RECIPIENT_ADDRESS = "recipient@test.host.com";
	private final String RECIPIENT_NAME = "Recipient Name";

	private SmtpEmailSender emailSender;

	@Before
	public void setUp () {
		emailSender = new SmtpEmailSender("test.host.com", 587);
	}

	@Test
	public void testBuildSimpleEmail() throws Exception {
		Email email = getSimpleEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
		assertTrue(commonsEmail instanceof SimpleEmail);

		assertEquals(email.getSubject(), commonsEmail.getSubject());

		commonsEmail.buildMimeMessage();
		assertNotNull(commonsEmail.getMimeMessage());
		assertEquals(email.getMessage(), commonsEmail.getMimeMessage().getContent());

		assertNotNull(commonsEmail.getFromAddress());
		assertEquals(SENDER_ADDRESS, commonsEmail.getFromAddress().getAddress());
		assertEquals(SENDER_NAME, commonsEmail.getFromAddress().getPersonal());

		assertContainsRecipients(email.getRecipients(), commonsEmail);
	}

	@Test
	public void testBuildHtmlEmail() throws Exception {
		Email email = getHtmlEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
		assertTrue(commonsEmail instanceof ImageHtmlEmail);

		assertEquals(email.getSubject(), commonsEmail.getSubject());

		commonsEmail.buildMimeMessage();
		assertNotNull(commonsEmail.getMimeMessage());
		assertTrue(commonsEmail.getMimeMessage().getContent() instanceof MimeMultipart);

		assertNotNull(commonsEmail.getFromAddress());
		assertEquals(SENDER_ADDRESS, commonsEmail.getFromAddress().getAddress());
		assertEquals(SENDER_NAME, commonsEmail.getFromAddress().getPersonal());

		assertContainsRecipients(email.getRecipients(), commonsEmail);
	}

	@Test
	public void testBuildCcEmail () throws Exception {
		Email email = getSimpleEmail();
		email.addRecipient(RecipientType.CC, RECIPIENT_NAME, RECIPIENT_ADDRESS);

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);

		assertContainsRecipients(email.getRecipients(), commonsEmail);
	}

	@Test
	public void testBuildBccEmail () throws Exception {
		Email email = getSimpleEmail();
		email.addRecipient(RecipientType.BCC, RECIPIENT_NAME, RECIPIENT_ADDRESS);

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);

		assertContainsRecipients(email.getRecipients(), commonsEmail);
	}

	// TODO(diego): How do I get the Commons Email authenticator? This only tests that nothing fails miserably.
	@Test
	public void testBuilEmailWithSimpleAuthentication () throws Exception {
		final String SENDER_PASSWORD = "sender_password";
		emailSender.setAuthentication(SENDER_ADDRESS, SENDER_PASSWORD);

		Email email = getSimpleEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
	}

	@Test
	public void testBuilEmailWithAuthenticator () throws Exception {
		final String SENDER_PASSWORD = "sender_password";
		emailSender.setAuthenticator(new DefaultAuthenticator(SENDER_ADDRESS, SENDER_PASSWORD));

		Email email = getSimpleEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
	}

	@Test
	public void testBuilEmailWithStartTlsEnabled () throws Exception {
		emailSender.setStarttlsEnabled(true);

		Email email = getSimpleEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
		assertTrue(commonsEmail.getMailSession().getProperty("mail.smtp.starttls.enable").equals("true"));
	}

	@Test
	public void testBuilEmailWithStartTlsRequired () throws Exception {
		emailSender.setStarttlsRequired(true);

		Email email = getSimpleEmail();

		org.apache.commons.mail.Email commonsEmail = emailSender.buildCommonsEmail(email);
		assertNotNull(commonsEmail);
		assertTrue(commonsEmail.getMailSession().getProperty("mail.smtp.starttls.required").equals("true"));
	}

	private void assertContainsRecipients (List<Recipient> expectedRecipients, org.apache.commons.mail.Email email) {
		int sizeTo = 0, sizeCc = 0, sizeBcc = 0;
		boolean found;

		for (Recipient recipient : expectedRecipients) {
			found = false;

			List<InternetAddress> addresses;
			switch (recipient.getType()) {
				case TO:
					sizeTo++;
					addresses = email.getToAddresses();
					break;
				case CC:
					sizeCc++;
					addresses = email.getCcAddresses();
					break;
				case BCC:
				default:
					sizeBcc++;
					addresses = email.getBccAddresses();
					break;
			}

			for (InternetAddress address : addresses) {
				if (address.getAddress().equals(recipient.getAddress()) &&
						address.getPersonal().equals(recipient.getName())) {
					found = true;
					break;
				}
			}

			assertTrue(found);
		}

		assertNotNull(email.getToAddresses());
		assertNotNull(email.getCcAddresses());
		assertNotNull(email.getBccAddresses());

		assertEquals(sizeTo, email.getToAddresses().size());
		assertEquals(sizeCc, email.getCcAddresses().size());
		assertEquals(sizeBcc, email.getBccAddresses().size());
	}

	private Email getSimpleEmail () {
		Email email = new Email();
		email.setFrom(SENDER_ADDRESS, SENDER_NAME);
		email.setMessage("Test message");

		email.addRecipient(RecipientType.TO, RECIPIENT_NAME, RECIPIENT_ADDRESS);

		return email;
	}

	private Email getHtmlEmail () {
		Email email = new Email(EmailType.HTML);
		email.setFrom(SENDER_ADDRESS, SENDER_NAME);
		email.setMessage("<div>Test Message</div>");

		email.addRecipient(RecipientType.TO, RECIPIENT_NAME, RECIPIENT_ADDRESS);

		return email;
	}
}