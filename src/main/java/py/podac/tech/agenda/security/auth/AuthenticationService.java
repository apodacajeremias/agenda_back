package py.podac.tech.agenda.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.jpa.PersonaServiceJPA;
import py.podac.tech.agenda.security.config.JwtService;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.token.TokenRepository;
import py.podac.tech.agenda.security.token.TokenType;
import py.podac.tech.agenda.security.user.Role;
import py.podac.tech.agenda.security.user.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PersonaServiceJPA personaService;

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
			throw new Exception(
					"La informacion de Persona pretendida para administrador no posee datos de Colaborador");
		// Verificar si posee User
		if (persona.getUser() == null)
			throw new Exception("La informacion de Persona pretendida para administrador no posee datos de User");

		/*
		 * En este paso, el usuario solamente fornece dos datos que son relevantes
		 * ahora: email y password. Los demas datos del User son asignados por defecto,
		 * el campo ROLE que debe ser Role.ADMINISTRADOR
		 */
		var user = User.builder().changePassword(false).email(persona.getUser().getEmail())
				.password(passwordEncoder.encode(persona.getUser().getPassword())).role(Role.ADMINISTRADOR).build();
		// Reasignamos este User al campo User dentro de Persona y se guardamos
		persona.setUser(user);
		var personaGuardada = personaService.guardar(persona);

		// Generamos el token de acceso
		var jwtToken = jwtService.generateToken(personaGuardada.getUser());
		var usuarioGuardado = personaGuardada.getUser();
		saveUserToken(usuarioGuardado, jwtToken);
		System.out.println("Registro exitoso");
		return AuthenticationResponse.builder().token(jwtToken).persona(persona).build();
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

//	public AuthenticationResponse register(RegisterRequest request) {
//		// Se construye el usuario
//		var user = User.builder().changePassword(false).email(request.getEmail())
//				.password(passwordEncoder.encode(request.getPassword())).role(Role.USUARIO).build();
//		// En vez de salvar solo el usuario, salvar toda la persona asociada pero con
//		// password encriptada
//		var savedUser = repository.save(user);
//		var jwtToken = jwtService.generateToken(user);
//		saveUserToken(savedUser, jwtToken);
//		return AuthenticationResponse.builder().token(jwtToken).build();
//	}
//
//	public AuthenticationResponse authenticate(AuthenticationRequest request) {
//		authenticationManager
//				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//		var user = repository.findByEmail(request.getEmail()).orElseThrow();
//		var jwtToken = jwtService.generateToken(user);
//		revokeAllUserTokens(user);
//		saveUserToken(user, jwtToken);
//		return AuthenticationResponse.builder().token(jwtToken).user(user).build();
//	}

	public AuthenticationResponse verify(String token) {
		String email = jwtService.extractUsername(token);
		var persona = this.personaService.buscarPorEmailDeUsuario(email);
		final Token TOKEN = this.tokenRepository.findByToken(token).orElseThrow();
		return jwtService.isTokenValid(token, persona.getUser())
				? AuthenticationResponse.builder().token(TOKEN.getToken()).persona(persona).build()
				: null;
	}

	private void saveUserToken(User user, String jwtToken) {
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
