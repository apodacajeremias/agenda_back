package py.jere.agendate.security.auth;

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
import py.jere.agendate.model.exceptions.InvalidOldPasswordException;
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
		TokenType type = TokenType.VERIFICATION;
		Token v = validateTokenById(idToken, type);
		userService.activarCuenta(v.getUser().getId());
		revokeAllUserTokens(v.getUser(), type);
	}

	public Token registrationResend(UUID existingToken) throws Exception {
		TokenType type = TokenType.VERIFICATION;
		Token v = validateTokenById(existingToken, type);
		revokeAllUserTokens(v.getUser(), type);
		return v;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
		System.out.println("Autenticando...");
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userService.buscarPorEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user, TokenType.BEARER);
		saveUserToken(user, jwtToken);
		System.out.println("Autenticado!");
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).user(user).build();
	}

	/**
	 * Se utiliza para validar token de tipo BEARER, solo para acceso
	 * 
	 * @param token
	 * @throws Exception
	 */
	public AuthenticationResponse validateToken(String token) throws Exception {
		System.out.println("Validando token...");
		Token t = validateTokenByValue(token, TokenType.BEARER);
		var user = t.getUser();
		var jwtToken = t.getToken();
		var refreshToken = jwtService.generateRefreshToken(user);
		System.out.println("Token válido!");
		return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).user(user).build();
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

	public void changePassword(UUID idToken) throws Exception {
		validateTokenById(idToken, TokenType.PASSWORD_RESET);
	}

	public void savePassword(UUID idToken, PasswordRequest request) throws Exception {
		RuleResult result = validator.validate(new PasswordData(request.getPassword()));
		if (!result.isValid()) {
			throw new Exception("Contrasena invalida: " + result.getDetails().toString());
		}
		if (!request.getPassword().equals(request.getMatchingPassword())) {
			throw new Exception("Las contraseñas no coinciden.");
		}
		final Token t = validateTokenById(idToken, TokenType.PASSWORD_RESET);
		final User user = t.getUser();
		final String encodedNewPassword = passwordEncoder.encode(request.getMatchingPassword());
		this.userService.cambiarContrasena(user, encodedNewPassword);
		revokeAllUserTokens(user, TokenType.PASSWORD_RESET);
	}

	public void updatePassword(UUID idUser, PasswordRequest request) throws Exception {
		RuleResult result = validator.validate(new PasswordData(request.getPassword()));
		User user = this.userService.buscar(idUser);
		if (!validateOldPassword(request.getOldPassword(), user)) {
			throw new InvalidOldPasswordException("Contraseña anterior no válida.");
		}
		if (!result.isValid()) {
			throw new Exception("Contrasena invalida: " + result.getDetails().toString());
		}
		if (!request.getPassword().equals(request.getMatchingPassword())) {
			throw new Exception("Las contraseñas no coinciden.");
		}

		final String encodedNewPassword = passwordEncoder.encode(request.getMatchingPassword());
		this.userService.cambiarContrasena(user, encodedNewPassword);
		revokeAllUserTokens(user, TokenType.PASSWORD_RESET);

	}

	/////

	/**
	 * Guardar el token de acceso generado y asocia el usuario. TokenType.BEARER por
	 * defecto
	 * 
	 * @param user
	 * @param jwtToken
	 * @throws Exception
	 */
	private void saveUserToken(User user, String jwtToken) throws Exception {
		var token = Token.builder().user(user).token(jwtToken).type(TokenType.BEARER).expired(false).revoked(false)
				.build();
		tokenService.registrar(token);
	}

	private void revokeAllUserTokens(User user, TokenType type) {
		tokenService.inactivarTodosLosTokensPorUserPorTipo(user.getId(), type);
	}

	/**
	 * Busca el token por ID
	 * 
	 * @param idToken
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private Token validateTokenById(UUID idToken, TokenType type) throws Exception {
		Token t = tokenService.buscar(idToken);
		if (!t.type.equals(type)) {
			throw new Exception("El tipo del Token no es correcto. Tipo -> " + t.getType());
		}
		if (!jwtService.isTokenValid(t.getToken(), t.getUser())) {
			throw new Exception("El Token no es valido.");
		}
		if (type.equals(TokenType.BEARER)) {
			if (t.isExpired() || t.isRevoked()) {
				throw new Exception("El Token es invalido.");
			}
		}
		return t;
	}

	/**
	 * Busca el token por su valor
	 * 
	 * @param token
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private Token validateTokenByValue(String token, TokenType type) throws Exception {
		Token t = tokenService.buscarPorToken(token);
		if (!t.type.equals(type)) {
			throw new Exception("El tipo del Token no es correcto. Tipo -> " + t.getType());
		}
		if (!jwtService.isTokenValid(t.getToken(), t.getUser())) {
			throw new Exception("El Token no es valido.");
		}
		if (type.equals(TokenType.BEARER)) {
			if (t.isExpired() || t.isRevoked()) {
				throw new Exception("El Token es invalido.");
			}
		}
		return t;
	}

	private boolean validateOldPassword(String oldPassword, User user) {
		return passwordEncoder.matches(oldPassword, user.getPassword());
	}

}
