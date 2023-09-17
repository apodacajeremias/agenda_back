package py.jere.agendate.security.auth;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import org.passay.AlphabeticalSequenceRule;
import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.NumericalSequenceRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.QwertySequenceRule;
import org.passay.RuleResult;
import org.passay.SpecialCharacterRule;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import py.jere.agendate.model.exceptions.UserAlreadyExistException;
import py.jere.agendate.security.config.JwtService;
import py.jere.agendate.security.token.ITokenService;
import py.jere.agendate.security.token.Token;
import py.jere.agendate.security.token.TokenType;
import py.jere.agendate.security.user.IUserService;
import py.jere.agendate.security.user.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final IUserService userService;
	private final ITokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private PasswordValidator validator = new PasswordValidator(
			Arrays.asList(new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1),
					new SpecialCharacterRule(1), new NumericalSequenceRule(3, false),
					new AlphabeticalSequenceRule(3, false), new QwertySequenceRule(3, false), new WhitespaceRule()));

	public AuthenticationResponse register(RegisterRequest request) throws Exception {
		RuleResult result = validator.validate(new PasswordData(request.getPassword()));
		if (!result.isValid()) {
			throw new Exception("Contrasena invalida: " + result.getDetails().toString());
		}
		if (!request.getPassword().equals(request.getMatchingPassword())) {
			throw new Exception("Las contraseñas no coinciden.");
		}
		if (userService.existeEmail(request.getEmail())) {
			throw new UserAlreadyExistException("El correo no esta disponible.");
		}
		var user = User.builder().email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.role(request.getRole()).enabled(true).lastPasswordChange(null).build();
		var savedUser = userService.registrar(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).user(user).build();
	}

	public void registrationConfirm(UUID idToken) throws Exception {
		Token v = tokenService.buscar(idToken);
		if (!v.type.equals(TokenType.VERIFICATION)) {
			throw new Exception("El tipo del Token no es correcto. Tipo -> " + v.getType());
		}
		if (!jwtService.isTokenValid(v.getToken(), v.getUser())) {
			throw new Exception("El Token no es valido.");
		}
		userService.activarCuenta(v.getUser().getId());
		revokeAllUserTokens(v.getUser(), TokenType.VERIFICATION);
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userService.buscarPorEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user, TokenType.BEARER);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).user(user).build();
	}

	public AuthenticationResponse findToken(String token) throws Exception {
		var t = tokenService.buscarPorToken(token);
		if (t.isExpired() || t.isRevoked()) {
			throw new Exception("Expirado o revocado.");
		}
		return AuthenticationResponse.builder().accessToken(t.getToken()).user(t.getUser()).build();
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.userService.buscarPorEmail(userEmail);
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user, TokenType.BEARER);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
						.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
	
	public User resetPassword(String email) {
		User user = this.userService.buscarPorEmail(email);
		revokeAllUserTokens(user, TokenType.PASSWORD_RESET);
		return user;
	}
	
	public Token validatePasswordResetToken(UUID idToken) throws Exception {
		Token t = tokenService.buscar(idToken);
		if (!t.type.equals(TokenType.PASSWORD_RESET)) {
			throw new Exception("El tipo del Token no es correcto. Tipo -> " + t.getType());
		}
		if (!jwtService.isTokenValid(t.getToken(), t.getUser())) {
			throw new Exception("El Token no es valido.");
		}
		return t;
	}
	
	public void savePassword(PasswordRequest request) throws Exception {
		RuleResult result = validator.validate(new PasswordData(request.getPassword()));
		if (!result.isValid()) {
			throw new Exception("Contrasena invalida: " + result.getDetails().toString());
		}
		if (!request.getPassword().equals(request.getMatchingPassword())) {
			throw new Exception("Las contraseñas no coinciden.");
		}
		Token t = validatePasswordResetToken(request.getToken());
		User user = t.getUser();
		user.setChangePassword(false);
		user.setLastPasswordChange(LocalDate.now());
		user.setPassword(passwordEncoder.encode(request.getMatchingPassword()));
		this.userService.registrar(user);
		revokeAllUserTokens(user, TokenType.PASSWORD_RESET);
	}

	/////

	private void saveUserToken(User user, String jwtToken) throws Exception {
		var token = Token.builder().user(user).token(jwtToken).type(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenService.registrar(token);
	}

	private void revokeAllUserTokens(User user, TokenType type) {
		tokenService.inactivarTodosLosTokensPorUserPorTipo(user.getId(), type);
	}

}
