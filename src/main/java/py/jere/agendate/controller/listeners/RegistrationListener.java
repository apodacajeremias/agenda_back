package py.jere.agendate.controller.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import py.jere.agendate.controller.events.OnRegistrationCompleteEvent;
import py.jere.agendate.security.config.JwtService;
import py.jere.agendate.security.token.ITokenService;
import py.jere.agendate.security.token.Token;
import py.jere.agendate.security.token.TokenType;
import py.jere.agendate.security.user.User;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private ITokenService service;

	@Autowired
	private JwtService jwt;

	@Autowired
	private MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		try {
			this.confirmRegistration(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) throws Exception {
		User user = event.getUser();
		String token = jwt.generateRefreshToken(user);
		Token guardar = Token.builder().user(user).token(token).type(TokenType.VERIFICATION).build();
		service.registrar(guardar);

		String recipientAddress = user.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/register/confirm/" + guardar.getId();
		String message = messages.getMessage("message.regSucc", null, event.getLocale());

		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
		mailSender.send(email);
	}
	
}