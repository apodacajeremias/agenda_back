package py.jere.agendate.model.services.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Agenda;
import py.jere.agendate.model.services.interfaces.IAgendaService;
import py.jere.agendate.model.services.repositories.AgendaRepository;

@Service
@Primary
public class AgendaServiceJPA implements IAgendaService {

	@Autowired
	private AgendaRepository repo;

	@Override
	public Agenda registrar(Agenda registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public Agenda guardar(Agenda guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Agenda> guardarTodos(List<Agenda> guardarTodos) {
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
	public List<Agenda> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Agenda> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Agenda> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Agenda buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Agenda buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}

	@Override
	public boolean horarioDisponible(LocalDateTime inicio, LocalDateTime fin) {
		return false;
	}
}
