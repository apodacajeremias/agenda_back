package py.podac.tech.agenda.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import py.podac.tech.agenda.model.entities.Promocion;
import py.podac.tech.agenda.model.services.interfaces.IPromocionService;
import py.podac.tech.agenda.model.services.repositories.PromocionRepository;

@Service
@Primary
public class PromocionServiceJPA implements IPromocionService {

	@Autowired
	private PromocionRepository repo;

	@Override
	public Promocion registrar(Promocion registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public Promocion guardar(Promocion guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Promocion> guardarTodos(List<Promocion> guardarTodos) {
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
	public Promocion buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Promocion buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
