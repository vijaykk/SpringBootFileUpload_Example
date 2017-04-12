package com.vijay.filecatalog.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Class to send Emails from application.
 * @author Vijay Koppineedi.
 *
 */
@Service
@Configuration
@PropertySource("classpath:application.properties")
public class EmailSender {

	private JavaMailSender javaMailSender;

	@Value("${mail.email.receiver.email}")
	private String receiverEmailId;

	@Autowired
	public EmailSender(JavaMailSender javaMailSender) {
		super();
		this.javaMailSender = javaMailSender;
	}

	public void sendMail(String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		System.out.println("send Email" + receiverEmailId);
		message.setTo(receiverEmailId);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
	}

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public String getReceiverEmailId() {
		return receiverEmailId;
	}

	public void setReceiverEmailId(String receiverEmailId) {
		this.receiverEmailId = receiverEmailId;
	}

}
