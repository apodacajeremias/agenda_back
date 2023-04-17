package py.podac.tech.agenda.security.auth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.security.token.Token;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

	private final AuthenticationService service;
	
	@PostMapping
	public ResponseEntity<AuthenticationResponse> verificar(@RequestBody Token token){
		System.err.println("Verificando -> "+ token.getToken());
		System.err.println(token);
		
		return ResponseEntity.ok(service.verify(token.getToken()));
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		System.err.println("Registrando -> " + request);
		return ResponseEntity.ok(service.register(request));
	}

	@PostMapping(value = "/authenticate", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		System.err.println("Autenticando -> " + request);
		return ResponseEntity.ok(service.authenticate(request));
	}

}
