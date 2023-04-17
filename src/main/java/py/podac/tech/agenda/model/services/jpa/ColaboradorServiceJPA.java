package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.Colaborador;
import py.podac.tech.agenda.model.services.interfaces.IColaboradorService;
import py.podac.tech.agenda.model.services.repositories.ColaboradorRepository;

@Service
@Primary
public class ColaboradorServiceJPA implements IColaboradorService {

	@Autowired
	private ColaboradorRepository repo;

	@Override
	public Colaborador guardar(Colaborador guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Colaborador> guardarTodos(List<Colaborador> guardarTodos) {
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
	public List<Colaborador> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Colaborador> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Colaborador> buscarTodos() {
		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
	}

	@Override
	public Colaborador buscar(UUID ID) {
		return this.repo.findById(ID).orElse(null);
	}

	@Override
	public Colaborador buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc();
	}

}
