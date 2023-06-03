package py.podac.tech.agenda.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.jpa.PersonaServiceJPA;
import py.podac.tech.agenda.model.services.repositories.TokenRepository;
import py.podac.tech.agenda.security.config.JwtService;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.token.TokenType;
import py.podac.tech.agenda.security.user.Role;
import py.podac.tech.agenda.security.user.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final PersonaServiceJPA personaService;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
//	private final PasswordEncoder passwordEncoder;

	/**
	 * Se recibe un objeto Persona para guardar en la base de datos. La Persona debe
	 * contener obligatoriamente un User y un Colaborador, estas informaciones se
	 * usan para registrar los datos de la persona que actua como administrador o
	 * dueno del establecimiento
	 * 
	 * @param persona
	 * @return
	 * @throws Exception
	 */
	public AuthenticationResponse register(Persona persona) throws Exception {
		// Verificar si posee Colaborador
		if (persona.getColaborador() == null)
			throw new Exception("La Persona pretendida para administrador no posee datos de Colaborador");
		// Verificar si posee User
		if (persona.getUser() == null)
			throw new Exception("La Persona pretendida para administrador no posee datos de User");

		/*
		 * El campo ROLE que debe ser Role.ADMINISTRADOR
		 */
		persona.getUser().setRole(Role.ADMINISTRADOR);
		persona.getUser().setEnabled(false);
		// Reasignamos este User al campo User dentro de Persona y se guardamos
		var personaGuardada = personaService.registrar(persona);

		// Generamos el token de acceso
		var jwtToken = jwtService.generateToken(personaGuardada.getUser());
		saveUserToken(personaGuardada.getUser(), jwtToken);
		System.out.println("Registro exitoso");
		return AuthenticationResponse.builder().token(jwtToken).persona(personaGuardada).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var persona = this.personaService.buscarPorEmailDeUsuario(request.getEmail());
		var jwtToken = jwtService.generateToken(persona.getUser());
		revokeAllUserTokens(persona.getUser());
		saveUserToken(persona.getUser(), jwtToken);
		return AuthenticationResponse.builder().token(jwtToken).persona(persona).build();
	}

	public AuthenticationResponse validate(String token) {
		if (jwtService.isTokenExpired(token)) {
			return null;
		}
		final Token encontrado = this.tokenRepository.findByToken(token).orElse(null);
		if (token == null) {
			return null;
		}
		if (encontrado.isRevoked() || encontrado.isExpired()) {
			return null;
		}
		return AuthenticationResponse.builder().token(encontrado.getToken())
				.persona(this.personaService.buscarPorEmailDeUsuario(encontrado.getUser().getEmail())).build();
	}

	// Invalidar otros TOKEN
	private void saveUserToken(User user, String jwtToken) {
		revokeAllUserTokens(user);
		var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getID());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
}
