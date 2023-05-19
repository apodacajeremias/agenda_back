package py.podac.tech.agenda.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.podac.tech.agenda.security.config.JwtService;
import py.podac.tech.agenda.security.token.Token;
import py.podac.tech.agenda.security.token.TokenRepository;
import py.podac.tech.agenda.security.token.TokenType;
import py.podac.tech.agenda.security.user.Role;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository repository;
	private final TokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;

	// TODO: Adaptar metodo para que reciba una Persona
	public AuthenticationResponse register(RegisterRequest request) {
		// Se construye el usuario
		var user = User.builder().changePassword(false).email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
		// En vez de salvar solo el usuario, salvar toda la persona asociada pero con
		// password encriptada
		var savedUser = repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		saveUserToken(savedUser, jwtToken);
		System.out.println("Registro exitoso -> " + request);
		return AuthenticationResponse.builder().token(jwtToken).user(user).build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = repository.findByEmail(request.getEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		System.out.println("Autenticacion exitosa -> " + request);
		return AuthenticationResponse.builder().token(jwtToken).user(user).build();
	}

	public AuthenticationResponse verify(String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtService.extractUsername(token));
		final Token TOKEN = this.tokenRepository.findByToken(token).orElseThrow();
		if (jwtService.isTokenValid(token, userDetails)) {
			return AuthenticationResponse.builder().token(TOKEN.getToken()).user(TOKEN.getUser()).build();
		}
		return null;
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
