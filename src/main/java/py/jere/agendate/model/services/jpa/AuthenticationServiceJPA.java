package py.jere.agendate.model.services.jpa;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.jere.agendate.model.entities.Token;
import py.jere.agendate.model.entities.Usuario;
import py.jere.agendate.model.enums.Rol;
import py.jere.agendate.model.enums.TokenType;
import py.jere.agendate.model.services.interfaces.IEmpresaService;
import py.jere.agendate.model.services.interfaces.IUsuarioService;
import py.jere.agendate.model.services.repositories.TokenRepository;
import py.jere.agendate.security.auth.AuthenticationRequest;
import py.jere.agendate.security.auth.AuthenticationResponse;
import py.jere.agendate.security.config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceJPA {
	private final IUsuarioService service;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final IEmpresaService empresaService;

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
//		if (usuario.getPersona() == null)
//			throw new Exception("El User pretendido para administrador no posee informacion de Persona");
		// Verificar si Persona posee informacion de Colaborador
//		if (usuario.getPersona().getColaborador() == null)
//			throw new Exception("La Persona pretendida para administrador no posee informacion de Colaborador");

		/*
		 * El campo ROLE debe ser Role.ADMINISTRADOR, debido al medio por el cual se
		 * registra
		 */
		usuario.setRol(Rol.ADMINISTRADOR);
		usuario.setEnabled(true); // Para habilitar por correo
		// Al guardar el User, se persiste la Persona y al Colaborador
		var usuarioGuardado = service.registrar(usuario);
		
		// Generamos el token de acceso
		var jwtToken = jwtService.generateToken(usuarioGuardado);
		saveUserToken(usuarioGuardado, jwtToken);
		System.out.println("Registro exitoso.");
		return AuthenticationResponse.builder().token(jwtToken).usuario(usuarioGuardado).empresa(empresaService.buscarUltimo()).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		Usuario usuario = this.service.buscarPorEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(usuario);
		revokeAllUserTokens(usuario);
		saveUserToken(usuario, jwtToken);
		System.out.println("Autenticacion correcta.");
		return AuthenticationResponse.builder().token(jwtToken).usuario(usuario).empresa(empresaService.buscarUltimo()).build();
	}

	public ResponseEntity<AuthenticationResponse> validate(String token) throws Exception {
		final Token encontrado = this.tokenRepository.findByToken(token).orElse(null);
		if (token == null || encontrado == null) {
			return ResponseEntity.notFound().build();
		}
		if (encontrado.isRevoked() || encontrado.isExpired()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(AuthenticationResponse.builder().token(encontrado.getToken()).usuario(encontrado.getUsuario()).empresa(empresaService.buscarUltimo()).build());
	}


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
