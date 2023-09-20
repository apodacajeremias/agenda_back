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
		return this.repo.save(registrar);
	}

	@Override
	public List<Item> guardarTodos(List<Item> guardarTodos) {
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
	public Item buscar(UUID id) {
		return this.repo.findById(id).orElseThrow();
	}

	@Override
	public Item buscarUltimo() {
		return this.repo.findTopByOrderByFechaCreacionDesc().orElseThrow();
	}
}
