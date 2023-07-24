package py.podac.tech.agenda.model.services.jpa;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.model.entities.Token;
import py.podac.tech.agenda.model.entities.Usuario;
import py.podac.tech.agenda.model.enums.Rol;
import py.podac.tech.agenda.model.enums.TokenType;
import py.podac.tech.agenda.model.services.interfaces.IUsuarioService;
import py.podac.tech.agenda.model.services.repositories.TokenRepository;
import py.podac.tech.agenda.security.auth.AuthenticationRequest;
import py.podac.tech.agenda.security.auth.AuthenticationResponse;
import py.podac.tech.agenda.security.config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceJPA {
	private final IUsuarioService service;
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
	public AuthenticationResponse registrar(Usuario usuario) throws Exception {
		// Verificar si posee informacion de Persona
		if (usuario.getPersona() == null)
			throw new Exception("El User pretendido para administrador no posee informacion de Persona");
		// Verificar si Persona posee informacion de Colaborador
		if (usuario.getPersona().getColaborador() == null)
			throw new Exception("La Persona pretendida para administrador no posee informacion de Colaborador");

		/*
		 * El campo ROLE debe ser Role.ADMINISTRADOR, debido al medio por el cual se
		 * registra
		 */
		usuario.setRol(Rol.ADMINISTRADOR);
		usuario.setEnabled(false); // Para habilitar por correo
		// Al guardar el User, se persiste la Persona y al Colaborador
		var usuarioGuardado = service.registrar(usuario);

		// Generamos el token de acceso
		var jwtToken = jwtService.generateToken(usuarioGuardado);
		saveUserToken(usuarioGuardado, jwtToken);
		System.out.println("Registro exitoso");
		return AuthenticationResponse.builder().token(jwtToken).usuario(usuarioGuardado).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		Usuario usuario = this.service.buscarPorEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(usuario);
		revokeAllUserTokens(usuario);
		saveUserToken(usuario, jwtToken);
		System.out.println("Autenticacion correcta.");
		return AuthenticationResponse.builder().token(jwtToken).usuario(usuario).build();
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
		return AuthenticationResponse.builder().token(encontrado.getToken()).usuario(encontrado.getUsuario()).build();
	}

	// Invalidar otros TOKEN
	private void saveUserToken(Usuario usuario, String jwtToken) {
		revokeAllUserTokens(usuario);
		var token = Token.builder().usuario(usuario).token(jwtToken).tokenType(TokenType.BEARER).expired(false)
				.revoked(false).build();
		tokenRepository.save(token);
	}

	private void revokeAllUserTokens(Usuario usuario) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(usuario.getID());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
}
