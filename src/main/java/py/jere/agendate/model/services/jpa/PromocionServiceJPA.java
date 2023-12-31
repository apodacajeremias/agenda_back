package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Promocion;
import py.jere.agendate.model.services.interfaces.IPromocionService;
import py.jere.agendate.model.services.repositories.PromocionRepository;

@Service
@Primary
public class PromocionServiceJPA implements IPromocionService {

	@Autowired
	private PromocionRepository repo;

	@Override
	public Promocion registrar(Promocion registrar) throws Exception {
		return this.repo.save(registrar);
	}

	@Override
	public List<Promocion> guardarTodos(List<Promocion> guardarTodos) {
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
	public List<Promocion> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Promocion> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Promocion> buscarTodos() {
		return this.repo.findAll(Sort.by(Sort.Direction.DESC, "fechaCreacion"));
	}

	@Override
	public Promocion buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Promocion buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
