package py.jere.agendate.controller;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import py.jere.agendate.controller.events.OnPasswordResetEvent;
import py.jere.agendate.controller.events.OnRegistrationCompleteEvent;
import py.jere.agendate.controller.events.OnResendRegistrationEvent;
import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.model.exceptions.InvalidOldPasswordException;
import py.jere.agendate.model.services.interfaces.IUsuarioService;
import py.jere.agendate.model.services.jpa.AuthenticationServiceJPA;
import py.jere.agendate.security.auth.AuthenticationRequest;
import py.jere.agendate.security.auth.AuthenticationResponse;
import py.jere.agendate.security.auth.PasswordResetRequest;
import py.jere.agendate.security.auth.VerificationToken;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

	@Autowired
	private AuthenticationServiceJPA service;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private MessageSource messages;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(WebRequest request, Usuario usuario) throws Exception {
		System.out.println(usuario);
		AuthenticationResponse response = service.registrar(usuario);
		String appUrl = request.getContextPath();
		eventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(response.getUsuario(), request.getLocale(), appUrl));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> login(AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@GetMapping("/validate/{token}")
	public ResponseEntity<AuthenticationResponse> validate(@PathVariable String token) throws Exception {
		return service.validate(token);
	}

	@PostMapping("/registrationConfirm")
	public ResponseEntity<Boolean> registrationConfirm(WebRequest request, @RequestParam("token") String token)
			throws Exception {
		Locale locale = request.getLocale();
		VerificationToken verificationToken = usuarioService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messages.getMessage("auth.message.invalidToken", null, locale);
			throw new Exception(message);
		}
		Usuario usuario = verificationToken.getUsuario();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String message = messages.getMessage("auth.message.expired", null, locale);
			throw new Exception(message);
		}
		usuarioService.activarCuenta(usuario.getID());
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/resetPassword")
	public ResponseEntity<Boolean> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		Usuario usuario = usuarioService.buscarPorEmail(userEmail);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnPasswordResetEvent(usuario, request.getLocale(), appUrl));
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/user/changePassword")
	public ResponseEntity<Boolean> changePassword(Locale locale, @RequestParam("token") String token) throws Exception {
		String result = usuarioService.validatePasswordResetToken(token);
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/savePassword")
	public ResponseEntity<Boolean> savePassword(final Locale locale, PasswordResetRequest passwordReset)
			throws Exception {
		String result = usuarioService.validatePasswordResetToken(passwordReset.getToken());
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}

		Usuario usuario = usuarioService.getUsuarioByPasswordResetToken(passwordReset.getToken());
		if (usuario == null) {
			throw new Exception("Usuario que modifica contrasena no se ha encontrado");
		}

		usuarioService.changeUsuarioPassword(usuario, passwordReset.getPassword());
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/user/resendRegistrationToken")
	public ResponseEntity<Boolean> resendRegistrationToken(HttpServletRequest request,
			@RequestParam("token") String existingToken) {
		VerificationToken newToken = usuarioService.generateNewVerificationToken(existingToken);

		Usuario usuario = usuarioService.getUsuario(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		eventPublisher.publishEvent(new OnResendRegistrationEvent(usuario, newToken, request.getLocale(), appUrl));

		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/updatePassword")
	@PreAuthorize("hasRole('READ_PRIVILEGE')")
	public ResponseEntity<Boolean> changeUserPassword(Locale locale, @RequestParam("password") String password,
			@RequestParam("oldpassword") String oldPassword) throws Exception {
		Usuario usuario = usuarioService
				.buscarPorEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!usuarioService.checkIfValidOldPassword(usuario, oldPassword)) {
			throw new InvalidOldPasswordException();
		}
		usuarioService.changeUsuarioPassword(usuario, password);
		return ResponseEntity.ok(Boolean.TRUE);
	}

}
