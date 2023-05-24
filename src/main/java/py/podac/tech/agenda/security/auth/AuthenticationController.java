package py.podac.tech.agenda.security.auth;

import java.util.Calendar;
import java.util.Locale;

import org.springframework.context.ApplicationEventPublisher;
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
import py.podac.tech.agenda.controller.events.OnRegistrationCompleteEvent;
import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

	private final AuthenticationService service;

	private final ApplicationEventPublisher eventPublisher;

	// TODO: validar que el correo no se repita
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registrarUsuario(HttpServletRequest request,
			@RequestBody @Valid Persona persona) throws Exception {
		AuthenticationResponse registered = service.register(persona);
		String appUrl = request.getContextPath();
		eventPublisher.publishEvent(
				new OnRegistrationCompleteEvent(registered.getPersona().getUser(), request.getLocale(), appUrl));
		return ResponseEntity.ok(registered);
	}

	@PostMapping(value = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AuthenticationResponse> ingresarUsuario(@RequestBody @Valid AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	// TODO: devolver el horario cuando se va a vencer el token
	@PostMapping("/validate")
	public ResponseEntity<AuthenticationResponse> validarToken(@RequestBody Token token) {
		// Valida si el token esta dentro de la validez
		return ResponseEntity.ok(service.validate(token.getToken()));
	}
	
	@GetMapping("/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	 
	    Locale locale = request.getLocale();
	    
	    VerificationToken verificationToken = service.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
	        model.addAttribute("message", message);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    }
	    
	    User user = verificationToken.getUser();
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale)
	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    } 
	    
	    user.setEnabled(true); 
	    service.saveRegisteredUser(user); 
	    return "redirect:/login.html?lang=" + request.getLocale().getLanguage(); 
	}

}
