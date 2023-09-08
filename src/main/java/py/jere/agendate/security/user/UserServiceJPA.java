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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User guardar(User guardar) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> guardarTodos(List<User> guardarTodos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean eliminar(UUID id) {
		try {
			this.repo.deleteById(id);
			return !this.existe(id);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean existe(UUID id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> buscarActivos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> buscarInactivos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public User buscar(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User buscarUltimo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User buscarPorEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existeEmail(String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activarCuenta(UUID id) throws Exception {
		// TODO Auto-generated method stub

	}

}
