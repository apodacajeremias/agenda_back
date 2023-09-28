package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.MovimientoDetalle;
import py.jere.agendate.model.services.interfaces.IMovimientoDetalleService;
import py.jere.agendate.model.services.repositories.MovimientoDetalleRepository;

@Service
@Primary
public class MovimientoDetalleServiceJPA implements IMovimientoDetalleService {

	@Autowired
	private MovimientoDetalleRepository repo;

	@Override
	public MovimientoDetalle registrar(MovimientoDetalle registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<MovimientoDetalle> guardarTodos(List<MovimientoDetalle> guardarTodos) {
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
	public boolean eliminar(UUID idMovimiento, UUID id) {
		try {
			this.repo.deleteByMovimientoIdAndId(idMovimiento, id);
			return !this.existe(idMovimiento, id);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean existe(UUID id) {
		return this.repo.existsById(id);
	}

	@Override
	public boolean existe(UUID idMovimiento, UUID id) {
		return this.repo.existsByMovimientoIdAndId(idMovimiento, id);
	}

	@Override
	public List<MovimientoDetalle> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<MovimientoDetalle> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<MovimientoDetalle> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public MovimientoDetalle buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public MovimientoDetalle buscar(UUID idMovimiento, UUID id) {
		return this.repo.findByMovimientoIdAndId(idMovimiento, id).orElseThrow();
	}

	@Override
	public MovimientoDetalle buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
