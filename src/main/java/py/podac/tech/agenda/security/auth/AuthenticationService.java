package py.podac.tech.agenda.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.model.services.repositories.TokenRepository;
import py.podac.tech.agenda.security.config.JwtService;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.token.TokenType;
import py.podac.tech.agenda.security.user.Role;
import py.podac.tech.agenda.security.user.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final IUserService service;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
//	private final PasswordEncoder passwordEncoder;

	/**
	 * Se recibe un objeto User para guardar en la base de datos, estas
	 * informaciones se usan para registrar los datos de la persona que actua como
	 * administrador o dueno del establecimiento. Este Use debe ser autenticado por
	 * correo
	 * 
	 * @param persona
	 * @return
	 * @throws Exception
	 */
	public AuthenticationResponse registrar(User user) throws Exception {
		// Verificar si posee informacion de Persona
		if (user.getPersona() == null)
			throw new Exception("El User pretendido para administrador no posee informacion de Persona");
		// Verificar si Persona posee informacion de Colaborador
		if (user.getPersona().getColaborador() == null)
			throw new Exception("La Persona pretendida para administrador no posee informacion de Colaborador");

		/*
		 * El campo ROLE debe ser Role.ADMINISTRADOR, debido al medio por el cual se
		 * registra
		 */
		user.setRole(Role.ADMINISTRADOR);
		user.setEnabled(false); // Para habilitar por correo
		// Al guardar el User, se persiste la Persona y al Colaborador
		var userGuardado = service.registrar(user);

		// Generamos el token de acceso
		var jwtToken = jwtService.generateToken(userGuardado);
		saveUserToken(userGuardado, jwtToken);
		System.out.println("Registro exitoso");
		return AuthenticationResponse.builder().token(jwtToken).user(userGuardado).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = this.service.buscarPorEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder().token(jwtToken).user(user).build();
	}

	public AuthenticationResponse validate(String token) throws Exception {
		if (jwtService.isTokenExpired(token)) {
			return null;
		}
		final Token encontrado = this.tokenRepository.findByToken(token).orElse(null);
		if (token == null) {
			throw new Exception("El token indicado no ha sido encontrado");
		}
		if (encontrado.isRevoked() || encontrado.isExpired()) {
			throw new Exception("El token indicado ha sido revocado o esta expirado");
		}
		return AuthenticationResponse.builder().token(encontrado.getToken()).user(encontrado.getUser()).build();
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
