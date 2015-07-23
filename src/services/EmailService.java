/**
 * 
 */
package services;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
	private static final int QUEUE_CAPACITY = 100;
	
	private final String userName;
	private final String password;
	
	private final BlockingQueue<Email> emailQueue;
	
	/** Creates a new EmailService */
	public EmailService(String userName, String password) {
		this.userName = userName;
		this.password = password;
		emailQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
		
		Thread thread = new Thread(() -> run());
		thread.start();
	}
	
	/** Internal method the continuously processes and sends emails */
	private void run() {
		while (true) {
			try {
				Email email = emailQueue.take();
				process(email);
				
				Thread.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Sends the given email */
	private void process(Email email) throws AddressException, MessagingException {
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
				InternetAddress.parse(email.to));
		if (email.sbj != null) {
			message.setSubject(email.sbj);
		}
		message.setText(email.msg);

		Transport.send(message);
	}
	
	/** Sends an email to the given person with the subject and message */
	void send(String toNetid, String subject, String content) throws InterruptedException {
		emailQueue.put(new Email(toNetid + "@cornell.edu", subject, content));
	}
	
	/** Sends an email to the given person with the subject and body file */
	void send(String toNetid, String subject, File bodyFile) {
		
	}
	
	/** Sends an email to the given person with the subject, template file, and parameters */
	void send(String toNetid, String subject, File templateFile, Map<String, Object> params) {
		
	}
	
	/** Stores information about an email */
	private class Email {
		private final String to;
		private final String sbj;
		private final String msg;
		
		/** Creates a new Email */
		Email(String to, String sbj, String msg) {
			this.to = to;
			this.sbj = sbj;
			this.msg = msg;
		}
	}
	
}
