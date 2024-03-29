package org.barrabash.powernotifier;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class MailSender extends javax.mail.Authenticator {
	private String _user; 
	private String _pass; 

	private String _host;
	private String _port, _sport;
	
	private String[] _to; 
	private String _from; 

	private String _subject; 
	private String _body; 

	private Multipart _multipart; 
//	private Session _session;

	public MailSender() {
//		Properties props = new Properties();   
//		props.setProperty("mail.transport.protocol", "smtp");   
//		props.setProperty("mail.host", "smtp.googlemail.com");   
//		props.put("mail.smtp.auth", "true");   
//		props.put("mail.smtp.port", "465");   
//		props.put("mail.smtp.socketFactory.port", "465");   
//		props.put("mail.smtp.socketFactory.class",   
//				"javax.net.ssl.SSLSocketFactory");   
//		props.put("mail.smtp.socketFactory.fallback", "false");   
//		props.setProperty("mail.smtp.quitwait", "false");   

//		_session = Session.getDefaultInstance(props, this);   
	    	   
		_host = "smtp.googlemail.com";
		_port = _sport = "465";
		
		_user = ""; // username 
		_pass = ""; // password 
		_from = ""; // email sent from 
		_subject = ""; // email subject 
		_body = ""; // email body 

		_multipart = new MimeMultipart(); 

		// There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added. 
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822"); 
		CommandMap.setDefaultCommandMap(mc); 
	}
	
	public MailSender(String user, String pass) { 
		this(); 

		_user = user; 
		_pass = pass; 
	}
	
	private Properties _setProperties() { 
	    Properties props = new Properties(); 
	 
	    props.put("mail.smtp.host", _host); 
	    props.put("mail.smtp.auth", "true"); 
	    props.put("mail.smtp.port", _port); 
	    props.put("mail.smtp.socketFactory.port", _sport); 
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
	    props.put("mail.smtp.socketFactory.fallback", "false"); 
	 
	    return props; 
	  } 

	public boolean send() throws AddressException, MessagingException {
		if(!_user.equals("") && !_pass.equals("") && _to.length > 0 && !_from.equals("") && !_subject.equals("") && !_body.equals("")) {
			Properties props = _setProperties();
			Session session  = Session.getInstance(props, this);
			MimeMessage msg  = new MimeMessage(session); 

			msg.setFrom(new InternetAddress(_from)); 

			InternetAddress[] addressTo = new InternetAddress[_to.length]; 
			for (int i = 0; i < _to.length; i++) { 
				addressTo[i] = new InternetAddress(_to[i]); 
			} 
			msg.setRecipients(MimeMessage.RecipientType.TO, addressTo); 

			msg.setSubject(_subject); 
			msg.setSentDate(new Date()); 

			// setup message body 
			BodyPart messageBodyPart = new MimeBodyPart(); 
			messageBodyPart.setText(_body); 
			_multipart.addBodyPart(messageBodyPart); 

			// Put parts in message 
			msg.setContent(_multipart); 

			// send email 
			Transport.send(msg); 

			return true; 
		} else { 
			return false; 
		} 
	}
	
	@Override 
	public PasswordAuthentication getPasswordAuthentication() { 
		return new PasswordAuthentication(_user, _pass); 
	}
	
	public void setTo(String[] to) {
		_to = to;
	}
	
	public void setSubject(String subject) {
		_subject = subject;
	}
	
	public void setFrom(String from) {
		_from = from;
	}
	
	public void setBody(String emailBody) {
		_body = emailBody;
	}
}
