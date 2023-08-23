package py.jere.agendate.controller;

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

import py.jere.agendate.controller.utils.Beans;
import py.jere.agendate.model.entities.Grupo;
import py.jere.agendate.model.services.interfaces.IGrupoService;

@RestController
@RequestMapping("/grupos")
@CrossOrigin
public class GrupoController {

	@Autowired
	IGrupoService service;

	@PostMapping
	private ResponseEntity<?> guardar(@RequestBody Grupo guardar) {
		System.out.println("Guardando Grupo: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(
			@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Grupo: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Grupo: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Grupo: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Grupo: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{ID}")
	private ResponseEntity<?> actualizar(@PathVariable UUID ID, @RequestBody Grupo actualizar) {
		System.out.println("Actualizando Grupo: " + ID + " -> " + actualizar);
		Grupo existente = service.buscar(ID);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando Grupo: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<?> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando Grupo: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
}
