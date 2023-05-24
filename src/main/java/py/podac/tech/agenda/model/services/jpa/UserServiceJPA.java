package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.exceptions.UserAlreadyExistException;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.model.services.repositories.UserRepository;
import py.podac.tech.agenda.model.services.repositories.VerificationTokenRepository;
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.VerificationToken;

@Service
@Primary
public class UserServiceJPA implements IUserService {

	@Autowired
	private UserRepository repo;

	@Autowired
	private VerificationTokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registrar(User user) throws Exception {
		if (existeEmail(user.getEmail())) {
			throw new UserAlreadyExistException("Ya existe una cuenta con ese correo:" + user.getEmail());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
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
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<User> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
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
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}

	@Override
	public User buscarPorEmail(String email) {
		return this.repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	@Override
	public User getUser(String verificationToken) {
		User user = tokenRepository.findByToken(verificationToken).getUser();
		return user;
	}

	@Override
	public VerificationToken getVerificationToken(String VerificationToken) {
		return tokenRepository.findByToken(VerificationToken);
	}

	@Override
	public void saveRegisteredUser(User user) {
		this.repo.save(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
		tokenRepository.save(myToken);
	}

}
