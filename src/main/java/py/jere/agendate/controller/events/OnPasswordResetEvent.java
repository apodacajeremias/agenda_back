package py.jere.agendate.controller.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import py.jere.agendate.security.user.User;

@SuppressWarnings("serial")
public class OnPasswordResetEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private User user;

	public OnPasswordResetEvent(User user, Locale locale, String appUrl) {
		super(user);

		this.user = user;
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

}