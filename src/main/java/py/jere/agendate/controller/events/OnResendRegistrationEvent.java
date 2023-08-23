package py.jere.agendate.controller.events;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.security.auth.VerificationToken;

@SuppressWarnings("serial")
public class OnResendRegistrationEvent extends ApplicationEvent {
	private String appUrl;
	private Locale locale;
	private Usuario usuario;
	private VerificationToken newToken;

	public OnResendRegistrationEvent(Usuario usuario, VerificationToken newToken, Locale locale, String appUrl) {
		super(usuario);
		this.usuario = usuario;
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

	public Usuario getUser() {
		return usuario;
	}

	public void setUser(Usuario usuario) {
		this.usuario = usuario;
	}

	public VerificationToken getNewToken() {
		return newToken;
	}

	public void setNewToken(VerificationToken newToken) {
		this.newToken = newToken;
	}

}