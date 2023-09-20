package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.model.services.interfaces.IColaboradorService;
import py.jere.agendate.model.services.interfaces.IPersonaService;
import py.jere.agendate.model.services.repositories.PersonaRepository;

@Service
@Primary
public class PersonaServiceJPA implements IPersonaService {

	@Autowired
	private PersonaRepository repo;

	@Autowired
	IColaboradorService colaboradorService;

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
		return this.repo.save(registrar);
	}

	@Override
	public List<Persona> guardarTodos(List<Persona> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID id) {
		try {
			this.repo.deleteById(id);
			if (existe(id))
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
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
	public Persona buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Persona buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
