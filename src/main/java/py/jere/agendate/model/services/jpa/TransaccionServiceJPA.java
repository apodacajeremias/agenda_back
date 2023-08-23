package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Transaccion;
import py.jere.agendate.model.services.interfaces.ITransaccionService;
import py.jere.agendate.model.services.repositories.TransaccionRepository;

@Service
@Primary
public class TransaccionServiceJPA implements ITransaccionService {

	@Autowired
	private TransaccionRepository repo;

	@Override
	public Transaccion registrar(Transaccion registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public Transaccion guardar(Transaccion guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Transaccion> guardarTodos(List<Transaccion> guardarTodos) {
		return this.repo.saveAll(guardarTodos);
	}

	@Override
	public boolean eliminar(UUID ID) {
		try {
			this.repo.deleteById(ID);
			return !this.existe(ID);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean existe(UUID ID) {
		return this.repo.existsById(ID);
	}

	@Override
	public List<Transaccion> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Transaccion> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Transaccion> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Transaccion buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Transaccion buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
