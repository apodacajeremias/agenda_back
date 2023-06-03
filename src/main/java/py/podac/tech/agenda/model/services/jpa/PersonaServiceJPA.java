package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.interfaces.IColaboradorService;
import py.podac.tech.agenda.model.services.interfaces.IPersonaService;
import py.podac.tech.agenda.model.services.interfaces.IUserService;
import py.podac.tech.agenda.model.services.repositories.PersonaRepository;

@Service
@Primary
public class PersonaServiceJPA implements IPersonaService {

//	@Autowired
//	private Validator validator;

	@Autowired
	private PersonaRepository repo;

	@Autowired
	IColaboradorService colaboradorService;

	@Autowired
	IUserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public Persona registrar(Persona registrar) throws Exception {
//		Set<ConstraintViolation<Persona>> violations = validator.validate(registrar);
//		 Set<ConstraintViolation<Colaborador>> violations = validator.validate(registrar.getColaborador();
//		 Set<ConstraintViolation<User>> violations = validator.validate(registrar.getUser());
//		if (registrar.getColaborador() != null) {
//			var colaboradorGuardado = colaboradorService.registrar(registrar.getColaborador());
//			registrar.setColaborador(new Colaborador(colaboradorGuardado.getID()));
//		}
//		if (registrar.getUser() != null) {
//			registrar.getUser().setPassword(passwordEncoder.encode(registrar.getUser().getPassword()));
//			registrar.getUser().setMatchingPassword(passwordEncoder.encode(registrar.getUser().getMatchingPassword()));
//			var usuarioGuardado = userService.registrar(registrar.getUser());
//			registrar.setUser(new User(usuarioGuardado.getID()));
//
//		}
		return guardar(registrar);
	}

	@Override
	public Persona guardar(Persona guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Persona> guardarTodos(List<Persona> guardarTodos) {
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
	public List<Persona> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Persona> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Persona> buscarTodos() {
		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
	}

	@Override
	public Persona buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Persona buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}

	@Override
	public Persona buscarPorEmailDeUsuario(String email) {
		return this.repo.findByUserEmail(email).orElseThrow();
	}

}
