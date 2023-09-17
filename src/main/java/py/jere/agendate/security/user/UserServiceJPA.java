package py.jere.agendate.security.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserServiceJPA implements IUserService {

	@Autowired
	private UserRepository repo;

	@Override
	public User registrar(User registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public User guardar(User guardar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> guardarTodos(List<User> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID id) {
			this.repo.deleteById(id);
			return !this.existe(id);
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
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
		return this.repo.findAll();
	}

	@Override
	public User buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public User buscarUltimo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User buscarPorEmail(String email) {
		return this.repo.findByEmail(email).orElseThrow();
	}

	@Override
	public boolean existeEmail(String email) {
		return this.repo.existsByEmail(email);
	}

	@Override
	public void activarCuenta(UUID id) throws Exception {
		this.repo.enableUser(id);
	}

}
