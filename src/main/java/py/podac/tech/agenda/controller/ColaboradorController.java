package py.podac.tech.agenda.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.podac.tech.agenda.controller.utils.Beans;
import py.podac.tech.agenda.model.entities.Colaborador;
import py.podac.tech.agenda.model.services.interfaces.IColaboradorService;

@RestController
@RequestMapping("/colaboradores")
@CrossOrigin
public class ColaboradorController {

	@Autowired
	IColaboradorService service;

//	POST 	/orders (post/add a new order in the set of orders)
//	GET 	/orders (get a list of orders)
//	PUT 	/orders/{id} (replace an order)
//	DELETE 	/orders/{id} (delete an order)
//	GET 	/orders/{id} (get a single order)

	@PostMapping
	private ResponseEntity<Colaborador> guardar(@RequestBody Colaborador guardar) {
		System.err.println("Guardando Colaborador: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<Colaborador>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo) {
			System.err.println("Buscando Colaborador: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.err.println("Buscando Colaborador: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.err.println("Buscando Colaborador: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{ID}")
	private ResponseEntity<Colaborador> actualizar(@PathVariable UUID ID, @RequestBody Colaborador actualizar) {
		System.err.println("Actualizando Colaborador: " + ID + " -> " + actualizar + " ID " + actualizar.getID());
		Colaborador existente = service.buscar(actualizar.getID());
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.err.println("Eliminando Colaborador: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<Colaborador> buscar(@PathVariable UUID ID) {
		System.err.println("Buscando Colaborador: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
}
