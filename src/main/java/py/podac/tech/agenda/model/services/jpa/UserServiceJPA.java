package py.podac.tech.agenda.model.services.jpa;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.exceptions.UserAlreadyExistException;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.model.services.repositories.PasswordResetTokenRepository;
import py.podac.tech.agenda.model.services.repositories.UserRepository;
import py.podac.tech.agenda.model.services.repositories.VerificationTokenRepository;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;
import py.podac.tech.agenda.security.user.reset.PasswordResetToken;

@Service
@Primary
public class UserServiceJPA implements IUserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;

	// TODO: CAMBIAR EL MODO EN EL QUE SE ENCRIPTA LA CONTRASENA, ESTO DEBE SER
	// FUERA DEL SERVICE
//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Override
	public User registrar(User user) throws Exception {
		if (existeEmail(user.getEmail())) {
			throw new UserAlreadyExistException("Ya existe una cuenta con ese correo:" + user.getEmail());
		}
		// LLAMAR AL ENCODER ACA GENERA REDUNDANCIA EN EL CONTEXTO DE SPRING
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return guardar(user);
	}

	@Override
	public User guardar(User guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<User> guardarTodos(List<User> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID ID) {
		try {
			this.repo.deleteById(ID);
			if (existe(ID))
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean existe(UUID ID) {
		return this.repo.existsById(ID);
	}

	@Override
	public boolean existeEmail(String email) {
		return this.repo.existsByEmail(email);
	}

	@Override
	public List<User> buscarActivos() {
		return this.repo.findByEnabledIsTrue();
	}

	@Override
	public List<User> buscarInactivos() {
		return this.repo.findByEnabledIsFalse();
	}

	@Override
	public List<User> buscarTodos() {
		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
	}

	@Override
	public User buscar(UUID ID) {
		return this.repo.findById(ID).orElse(null);
	}

	@Override
	public User buscarUltimo() {
		System.err.println("FUNCIONA NO IMPLEMENTADA");
		return null;
	}

	@Override
	public User buscarPorEmail(String email) {
		return this.repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found by email" + email));
	}

	@Override
	public User getUser(String verificationToken) {
		User user = verificationTokenRepository.findByToken(verificationToken).getUser();
		return user;
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return verificationTokenRepository.findByToken(VerificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		this.repo.save(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		verificationTokenRepository.save(myToken);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordTokenRepository.save(myToken);
	}

	@Override
	public String validatePasswordResetToken(String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token).orElseThrow();
		return !isTokenFound(passToken) ? "auth.message.invalidToken"
				: isTokenExpired(passToken) ? "auth.message.expired" : null;
	}

	private boolean isTokenFound(PasswordResetToken passToken) {
		return passToken != null;
	}

	private boolean isTokenExpired(PasswordResetToken passToken) {
		final Calendar cal = Calendar.getInstance();
		return passToken.getExpiryDate().before(cal.getTime());
	}

	@Override
	public User getUserByPasswordResetToken(String token) {
		return this.passwordTokenRepository.findByToken(token).orElseThrow().getUser();
	}

	@Override
	public void changeUserPassword(User user, String password) throws Exception {
		if (user == null || user.getID() == null) {
			throw new Exception("No se ha encontrado el ID del usuario para realizar el cambio de contrasena");
		}
		user.setChangePassword(false);
		user.setLastPasswordChange(LocalDate.now());
//		user.setPassword(passwordEncoder.encode(password));
		this.repo.save(user);
	}

	@Override
	public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
		VerificationToken vToken = verificationTokenRepository.findByToken(existingVerificationToken);
		vToken.updateToken(UUID.randomUUID().toString());
		vToken = verificationTokenRepository.save(vToken);
		return vToken;
	}

	@Override
	public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
//		return passwordEncoder.matches(oldPassword, user.getPassword());
		return false;
	}

	@Override
	public void activarCuenta(UUID ID) throws Exception {
		this.repo.enableUser(ID);
	}

}
