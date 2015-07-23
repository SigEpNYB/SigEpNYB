/**
 * 
 */
package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
	private static final String EMAIL_TYPES_FILE = "conf/email-types.conf";
	private static final String EMAILS_DIRECTORY = "conf/emails/";
	private static final int QUEUE_CAPACITY = 100;
	
	private final String userName;
	private final String password;
	
	private final Map<EmailType, String> emailSubjects;
	private final Map<EmailType, String> emailBodies;
	
	private final BlockingQueue<Email> emailQueue;
	
	/** Creates a new EmailService */
	public EmailService(String userName, String password) throws IOException {
		this.userName = userName;
		this.password = password;
		
		emailSubjects = new HashMap<>();
		emailBodies = new HashMap<>();
		loadEmailTypes();
		
		emailQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
		
		Thread thread = new Thread(() -> run());
		thread.start();
	}
	
	/** Loads the email types from files */
	private void loadEmailTypes() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(EMAIL_TYPES_FILE));
		
		String line;
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split(",");
			int index = 0;
			
			EmailType type = EmailType.valueOf(tokens[index++]);
			if (tokens.length == 3) {
				emailSubjects.put(type, tokens[index++]);
			}
			emailBodies.put(type, readFile(new File(EMAILS_DIRECTORY + tokens[index++])));
		}
		
		reader.close();
	}
	
	/** Reads the contents of a file into a String */
	private String readFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		
		reader.close();
		
		return sb.toString();
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
	void send(String toNetid, EmailType type) throws InterruptedException {
		String subject = emailSubjects.get(type);
		String body = emailBodies.get(type);
		emailQueue.put(new Email(toNetid + "@cornell.edu", subject, body));
	}
	
	/** The different types of emails */
	public enum EmailType {
		ACCOUNT_REQUESTED,
		ACCOUNT_ACCEPTED,
		ACCOUNT_REJECTED
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
