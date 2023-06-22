package py.podac.tech.agenda.security.auth;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import py.podac.tech.agenda.controller.events.OnResendRegistrationEvent;
import py.podac.tech.agenda.model.exceptions.InvalidOldPasswordException;
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
	public ResponseEntity<AuthenticationResponse> register(WebRequest request, @RequestBody User user)
			throws Exception {
		System.out.println(user);
		AuthenticationResponse response = service.registrar(user);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnRegistrationCompleteEvent(response.getUser(), request.getLocale(), appUrl));
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/validate")
	public ResponseEntity<AuthenticationResponse> validate(@RequestBody Token token) throws Exception {
		return ResponseEntity.ok(service.validate(token.getToken()));
	}

	@PostMapping("/registrationConfirm")
	public ResponseEntity<Boolean> registrationConfirm(WebRequest request, @RequestParam("token") String token)
			throws Exception {
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
		userService.activarCuenta(user.getID());
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/resetPassword")
	public ResponseEntity<Boolean> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
		User user = userService.buscarPorEmail(userEmail);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(new OnPasswordResetEvent(user, request.getLocale(), appUrl));
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/user/changePassword")
	public ResponseEntity<Boolean> changePassword(Locale locale, @RequestParam("token") String token) throws Exception {
		String result = userService.validatePasswordResetToken(token);
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/savePassword")
	public ResponseEntity<Boolean> savePassword(final Locale locale, @Valid PasswordResetRequest passwordReset)
			throws Exception {
		String result = userService.validatePasswordResetToken(passwordReset.getToken());
		if (result != null) {
			throw new Exception(messages.getMessage(result, null, locale));
		}

		User user = userService.getUserByPasswordResetToken(passwordReset.getToken());
		if (user == null) {
			throw new Exception("Usuario que modifica contrasena no se ha encontrado");
		}

		userService.changeUserPassword(user, passwordReset.getPassword());
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/user/resendRegistrationToken")
	public ResponseEntity<Boolean> resendRegistrationToken(HttpServletRequest request,
			@RequestParam("token") String existingToken) {
		VerificationToken newToken = userService.generateNewVerificationToken(existingToken);

		User user = userService.getUser(newToken.getToken());
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

		eventPublisher.publishEvent(new OnResendRegistrationEvent(user, newToken, request.getLocale(), appUrl));

		return ResponseEntity.ok(Boolean.TRUE);
	}

	@PostMapping("/user/updatePassword")
	@PreAuthorize("hasRole('READ_PRIVILEGE')")
	public ResponseEntity<Boolean> changeUserPassword(Locale locale, @RequestParam("password") String password,
			@RequestParam("oldpassword") String oldPassword) throws Exception {
		User user = userService.buscarPorEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		if (!userService.checkIfValidOldPassword(user, oldPassword)) {
			throw new InvalidOldPasswordException();
		}
		userService.changeUserPassword(user, password);
		return ResponseEntity.ok(Boolean.TRUE);
	}

}
