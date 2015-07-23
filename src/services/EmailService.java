/**
 * 
 */
package services;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Sends emails
 */
class EmailService {
	private final String userName;
	private final String password;
	
	/** Creates a new EmailService */
	public EmailService(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	/** Sends an email to the given person with the subject and message 
	 * @throws MessagingException 
	 * @throws AddressException */
	void send(String toNetid, String subject, String content) throws AddressException, MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(userName + "@gmail.com"));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(toNetid + "@cornell.edu"));
		if (subject != null) {
			message.setSubject(subject);
		}
		message.setText(content);

		Transport.send(message);
	}
	
	/** Sends an email to the given person with the subject and body file */
	void send(String toNetid, String subject, File bodyFile) {
		
	}
	
	/** Sends an email to the given person with the subject, template file, and parameters */
	void send(String toNetid, String subject, File templateFile, Map<String, Object> params) {
		
	}
	
}
