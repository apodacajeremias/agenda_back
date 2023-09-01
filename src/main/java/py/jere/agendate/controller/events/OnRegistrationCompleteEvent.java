package py.jere.agendate.controller.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import py.jere.agendate.security.user.User;

@SuppressWarnings("serial")
public class OnRegistrationCompleteEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private User usuario;

	public OnRegistrationCompleteEvent(User usuario, Locale locale, String appUrl) {
		super(usuario);

		this.usuario = usuario;
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
		return usuario;
	}

	public void setUser(User usuario) {
		this.usuario = usuario;
	}

}