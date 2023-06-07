package py.podac.tech.agenda.security.auth;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.controller.events.OnPasswordResetEvent;
import py.podac.tech.agenda.controller.events.OnRegistrationCompleteEvent;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;
import py.podac.tech.agenda.security.user.reset.PasswordResetRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

	private final AuthenticationService service;

	private final IUserService userService;

	private final ApplicationEventPublisher eventPublisher;

	private final MessageSource messages;

	// TODO: validar que el correo no se repita
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registrarUsuario(HttpServletRequest request,
			@RequestBody @Valid User user) throws Exception {
		AuthenticationResponse response = service.register(user);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(response.getUser(), request.getLocale(), appUrl));
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AuthenticationResponse> ingresarUsuario(@RequestBody @Valid AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	// TODO: devolver el horario cuando se va a vencer el token
	@PostMapping("/validate")
	public ResponseEntity<AuthenticationResponse> validarToken(@RequestBody Token token) throws Exception {
		// Valida si el token esta dentro de la validez
		return ResponseEntity.ok(service.validate(token.getToken()));
	}

	@GetMapping("/registrationConfirm")
	public ResponseEntity<Boolean> confirmRegistration(WebRequest request, Model model,
			@RequestParam("token") String token) throws Exception {
		boolean response = false;
		Locale locale = request.getLocale();

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messages.getMessage("auth.message.invalidToken", null, locale);
			throw new Exception(message);
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String message = messages.getMessage("auth.message.expired", null, locale);
			throw new Exception(message);
		}

		user.setEnabled(true);
		userService.saveRegisteredUser(user);
		response = true;
		return ResponseEntity.ok(response);
	}

	@PostMapping("/user/resetPassword")
	public ResponseEntity<Boolean> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		boolean response = false;
		User user = userService.buscarPorEmail(userEmail); // SI NO ENCUENTRA ARROJA EXCEPCION

		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnPasswordResetEvent(user, request.getLocale(), appUrl));
		response = true;
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/changePassword")
	public ResponseEntity<Boolean> changePassword(Locale locale, Model model, @RequestParam("token") String token)
			throws Exception {
		boolean response = false;
		String result = userService.validatePasswordResetToken(token);
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}
		response = true;
		return ResponseEntity.ok(response);
	}

	@PostMapping("/user/savePassword")
	public ResponseEntity<Boolean> savePassword(final Locale locale, @Valid PasswordResetRequest passwordReset)
			throws Exception {
		String result = userService.validatePasswordResetToken(passwordReset.getToken());
		boolean response = false;
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}

		User user = userService.getUserByPasswordResetToken(passwordReset.getToken());
		if (user != null) {
			userService.changeUserPassword(user, passwordReset.getPassword());
			response = true;
		}
		return ResponseEntity.ok(response);
	}

}
