package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.interfaces.IPersonaService;
import py.podac.tech.agenda.model.services.repositories.PersonaRepository;

@Service
@Primary
public class PersonaServiceJPA implements IPersonaService {

	@Autowired
	private PersonaRepository repo;

	@Override
	@Transactional()
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
		return this.repo.findById(ID).orElse(null);
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
