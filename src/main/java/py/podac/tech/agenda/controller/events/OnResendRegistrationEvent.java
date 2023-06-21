package py.podac.tech.agenda.controller.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;

@SuppressWarnings("serial")
public class OnResendRegistrationEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private User user;
	private VerificationToken newToken;

	public OnResendRegistrationEvent(User user, VerificationToken newToken, Locale locale, String appUrl) {
		super(user);
		this.user = user;
		this.newToken = newToken;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public VerificationToken getNewToken() {
		return newToken;
	}

	public void setNewToken(VerificationToken newToken) {
		this.newToken = newToken;
	}

}