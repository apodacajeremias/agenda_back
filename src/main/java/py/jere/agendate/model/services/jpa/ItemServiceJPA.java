package py.jere.agendate.model.services.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import py.jere.agendate.model.entities.Item;
import py.jere.agendate.model.services.interfaces.IItemService;
import py.jere.agendate.model.services.repositories.ItemRepository;

@Service
@Primary
public class ItemServiceJPA implements IItemService {

	@Autowired
	private ItemRepository repo;

	@Override
	public Item registrar(Item registrar) throws Exception {
		return guardar(registrar);
	}

	@Override
	public Item guardar(Item guardar) {
		return this.repo.save(guardar);
	}

	@Override
	public List<Item> guardarTodos(List<Item> guardarTodos) {
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
	public List<Item> buscarActivos() {
		return this.repo.findByActivoIsTrue();
	}

	@Override
	public List<Item> buscarInactivos() {
		return this.repo.findByActivoIsFalse();
	}

	@Override
	public List<Item> buscarTodos() {
		return this.repo.findAll();
	}

	@Override
	public Item buscar(UUID ID) {
		return this.repo.findById(ID).orElseThrow();
	}

	@Override
	public Item buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
