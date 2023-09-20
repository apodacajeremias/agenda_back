package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Grupo;
import py.jere.agendate.model.services.interfaces.IGrupoService;
import py.jere.agendate.model.services.repositories.GrupoRepository;

@Service
@Primary
public class GrupoServiceJPA implements IGrupoService {

	@Autowired
	private GrupoRepository repo;

	@Override
	public Grupo registrar(Grupo registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<Grupo> guardarTodos(List<Grupo> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
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
		return this.repo.existsById(id);
	}

	@Override
	public List<Grupo> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Grupo> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Grupo> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Grupo buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Grupo buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
