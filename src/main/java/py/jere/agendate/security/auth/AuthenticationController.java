package py.jere.agendate.security.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import py.jere.agendate.controller.events.OnPasswordResetEvent;
import py.jere.agendate.controller.events.OnRegistrationCompleteEvent;
import py.jere.agendate.controller.events.OnResendRegistrationEvent;
import py.jere.agendate.security.token.Token;
import py.jere.agendate.security.user.User;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

	@Autowired
	private AuthenticationService service;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping("/{token}")
	public ResponseEntity<AuthenticationResponse> findToken(@PathVariable String token) throws Exception {
		return ResponseEntity.ok(service.findToken(token));
	}

	@PostMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		service.refreshToken(request, response);
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(RegisterRequest register, HttpServletRequest request)
			throws Exception {
		AuthenticationResponse response;
		try {
			response = service.register(register);
			eventPublisher.publishEvent(
					new OnRegistrationCompleteEvent(response.getUser(), request.getLocale(), request.getContextPath()));
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/register/confirm/{id}")
	public void registrationConfirm(WebRequest request, @PathVariable UUID id) throws Exception {
		service.registrationConfirm(id);
	}

	@GetMapping("/register/resend/{id}")
	public void registrationResend(WebRequest request, @PathVariable UUID id) {
		try {
			Token response = service.registrationResend(id);
			eventPublisher.publishEvent(
					new OnResendRegistrationEvent(response.getUser(), request.getLocale(), request.getContextPath()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) throws Exception {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/password/reset/{email}")
	public void resetPassword(HttpServletRequest request, @PathVariable("email") String email) {
		User user = service.resetPassword(email);
		eventPublisher.publishEvent(new OnPasswordResetEvent(user, request.getLocale(), request.getContextPath()));
	}

	@GetMapping("/password/change/{id}")
	public void changePassword(HttpServletRequest request, @PathVariable UUID id) throws Exception {
		service.changePassword(id);
	}

	@PostMapping("/password/save/{id}")
	public void savePassword(PasswordRequest request, @PathVariable UUID id) throws Exception {
		service.savePassword(id, request);
	}

	@PostMapping("/password/update/{id}")
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
	public void updatePassword(@PathVariable UUID id, PasswordRequest request) throws Exception {
		service.updatePassword(id, request);
	}

}
