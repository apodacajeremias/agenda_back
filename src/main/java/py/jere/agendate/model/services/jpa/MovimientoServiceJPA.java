package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Movimiento;
import py.jere.agendate.model.services.interfaces.IMovimientoService;
import py.jere.agendate.model.services.repositories.MovimientoRepository;

@Service
@Primary
public class MovimientoServiceJPA implements IMovimientoService {

	@Autowired
	private MovimientoRepository repo;

	@Override
	public Movimiento registrar(Movimiento registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<Movimiento> guardarTodos(List<Movimiento> guardarTodos) {
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
	public List<Movimiento> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Movimiento> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Movimiento> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Movimiento buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Movimiento buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}

	@Override
	public List<Movimiento> buscarPorPersona(UUID idPersona) {
		return this.repo.findByPersonaId(idPersona);
	}
}
