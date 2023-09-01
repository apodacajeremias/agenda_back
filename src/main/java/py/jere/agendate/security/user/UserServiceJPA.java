package py.jere.agendate.security.user;
//package py.jere.agendate.security.user;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.List;
//import java.util.UUID;
//
//import org.passay.AlphabeticalSequenceRule;
//import org.passay.DigitCharacterRule;
//import org.passay.LengthRule;
//import org.passay.NumericalSequenceRule;
//import org.passay.PasswordData;
//import org.passay.PasswordValidator;
//import org.passay.QwertySequenceRule;
//import org.passay.RuleResult;
//import org.passay.SpecialCharacterRule;
//import org.passay.UppercaseCharacterRule;
//import org.passay.WhitespaceRule;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.domain.Sort;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import py.jere.agendate.model.entities.Usuario;
//import py.jere.agendate.model.exceptions.UserAlreadyExistException;
//import py.jere.agendate.model.services.repositories.UsuarioRepository;
//import py.jere.agendate.security.auth.PasswordResetToken;
//import py.jere.agendate.security.auth.PasswordResetTokenRepository;
//import py.jere.agendate.security.auth.VerificationToken;
//import py.jere.agendate.security.auth.VerificationTokenRepository;
//
//@Service
//@Primary
//public class UsuarioServiceJPA implements IUsuarioService {
//
//	@Autowired
//	private UsuarioRepository repo;
//
//	@Autowired
//	private VerificationTokenRepository verificationTokenRepository;
//
//	@Autowired
//	private PasswordResetTokenRepository passwordTokenRepository;
//
//	// WhitespaceRule no permite espacios
//	private PasswordValidator validator = new PasswordValidator(
//			Arrays.asList(new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1),
//					new SpecialCharacterRule(1), new NumericalSequenceRule(3, false),
//					new AlphabeticalSequenceRule(3, false), new QwertySequenceRule(3, false), new WhitespaceRule()));
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Override
//	public Usuario registrar(Usuario usuario) throws Exception {
//		// Verifica si correo ya existe
//		if (existeEmail(usuario.getEmail())) {
//			throw new UserAlreadyExistException("Ya existe una cuenta con ese correo:" + usuario.getEmail());
//		}
//
//		// Verificar si password tiene validez
//		RuleResult result = validator.validate(new PasswordData(usuario.getPassword()));
//		if (!result.isValid()) {
//			throw new Exception("Contrasena invalida -> " + result.getDetails().toString());
//		}
//
//		// Encriptar password
//		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
//		// Verificar si password y matchingPassword coinciden
//		if (!passwordEncoder.matches(usuario.getMatchingPassword(), usuario.getPassword())) {
//			throw new Exception("Confirmacion de contrasena no coincide con la contrasena");
//		}
//		usuario.setMatchingPassword(null);
//		return guardar(usuario);
//	}
//
//	@Override
//	public Usuario guardar(Usuario guardar) {
//		return this.repo.save(guardar);
//	}
//
//	@Override
//	public List<Usuario> guardarTodos(List<Usuario> guardarTodos) {
//		return this.repo.saveAll(guardarTodos);
//	}
//
//	@Override
//	public boolean eliminar(UUID id) {
//		try {
//			this.repo.deleteById(id);
//			if (existe(id))
//				return false;
//			else
//				return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	public boolean existe(UUID id) {
//		return this.repo.existsById(id);
//	}
//
//	@Override
//	public boolean existeEmail(String email) {
//		return this.repo.existsByEmail(email);
//	}
//
//	@Override
//	public List<Usuario> buscarActivos() {
//		return this.repo.findByEnabledIsTrue();
//	}
//
//	@Override
//	public List<Usuario> buscarInactivos() {
//		return this.repo.findByEnabledIsFalse();
//	}
//
//	@Override
//	public List<Usuario> buscarTodos() {
//		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
//	}
//
//	@Override
//	public Usuario buscar(UUID id) {
//		return this.repo.findById(id).orElse(null);
//	}
//
//	@Override
//	public Usuario buscarUltimo() {
//		System.err.println("FUNCIONA NO IMPLEMENTADA");
//		return null;
//	}
//
//	@Override
//	public Usuario buscarPorEmail(String email) {
//		return this.repo.findByEmail(email)
//				.orElseThrow(() -> new UsernameNotFoundException("Usuario not found by email" + email));
//	}
//
//	@Override
//	public Usuario getUsuario(String verificationToken) {
//		Usuario usuario = verificationTokenRepository.findByToken(verificationToken).getUsuario();
//		return usuario;
//	}
//
//	@Override
//	public VerificationToken getVerificationToken(String VerificationToken) {
//		return verificationTokenRepository.findByToken(VerificationToken);
//	}
//
//	@Override
//	public void saveRegisteredUsuario(Usuario usuario) {
//		this.repo.save(usuario);
//	}
//
//	@Override
//	public void createVerificationToken(Usuario usuario, String token) {
//		VerificationToken myToken = new VerificationToken(token, usuario);
//		verificationTokenRepository.save(myToken);
//	}
//
//	@Override
//	public void createPasswordResetTokenForUsuario(Usuario usuario, String token) {
//		PasswordResetToken myToken = new PasswordResetToken(token, usuario);
//		passwordTokenRepository.save(myToken);
//	}
//
//	@Override
//	public String validatePasswordResetToken(String token) {
//		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token).orElseThrow();
//		return !isTokenFound(passToken) ? "auth.message.invalidToken"
//				: isTokenExpired(passToken) ? "auth.message.expired" : null;
//	}
//
//	private boolean isTokenFound(PasswordResetToken passToken) {
//		return passToken != null;
//	}
//
//	private boolean isTokenExpired(PasswordResetToken passToken) {
//		final Calendar cal = Calendar.getInstance();
//		return passToken.getExpiryDate().before(cal.getTime());
//	}
//
//	@Override
//	public Usuario getUsuarioByPasswordResetToken(String token) {
//		return this.passwordTokenRepository.findByToken(token).orElseThrow().getUsuario();
//	}
//
//	@Override
//	public void changeUsuarioPassword(Usuario usuario, String password) throws Exception {
//		if (usuario == null || usuario.getID() == null) {
//			throw new Exception("No se ha encontrado el id del usuario para realizar el cambio de contrasena");
//		}
//		usuario.setChangePassword(false);
//		usuario.setLastPasswordChange(LocalDate.now());
////		user.setPassword(passwordEncoder.encode(password));
//		this.repo.save(usuario);
//	}
//
//	@Override
//	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
//		VerificationToken vToken = verificationTokenRepository.findByToken(existingVerificationToken);
//		vToken.updateToken(UUID.randomUUID().toString());
//		vToken = verificationTokenRepository.save(vToken);
//		return vToken;
//	}
//
//	@Override
//	public boolean checkIfValidOldPassword(final Usuario usuario, final String oldPassword) {
////		return passwordEncoder.matches(oldPassword, user.getPassword());
//		return false;
//	}
//
//	@Override
//	public void activarCuenta(UUID id) throws Exception {
//		this.repo.enableUsuario(id);
//	}
//
//}
