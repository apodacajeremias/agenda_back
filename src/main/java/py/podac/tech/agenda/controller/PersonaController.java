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
import py.podac.tech.agenda.model.entities.Persona;
import py.podac.tech.agenda.model.services.interfaces.IPersonaService;

@RestController
@RequestMapping("/personas")
@CrossOrigin
public class PersonaController {

	@Autowired
	IPersonaService service;

//	POST 	/orders (post/add a new order in the set of orders)
//	GET 	/orders (get a list of orders)
//	PUT 	/orders/{id} (replace an order)
//	DELETE 	/orders/{id} (delete an order)
//	GET 	/orders/{id} (get a single order)

	@PostMapping
	private ResponseEntity<Persona> guardar(@RequestBody Persona guardar) {
		System.err.println("Guardando Persona: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<Persona>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.err.println("Buscando Persona: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.err.println("Buscando Persona: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.err.println("Buscando Persona: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.err.println("Buscando Persona: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{ID}")
	private ResponseEntity<Persona> actualizar(@PathVariable UUID ID, @RequestBody Persona actualizar) {
		System.err.println("Actualizando Persona: " + ID + " -> " + actualizar + " ID " + actualizar.getID());
		Persona existente = service.buscar(actualizar.getID());
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.err.println("Eliminando Persona: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<Persona> buscar(@PathVariable UUID ID) {
		System.err.println("Buscando Persona: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
}