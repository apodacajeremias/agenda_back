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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import py.jere.agendate.controller.utils.Beans;
import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.model.services.interfaces.IPersonaService;

@RestController
@RequestMapping("/personas")
@CrossOrigin
public class PersonaController {

	@Autowired
	IPersonaService service;

	@PostMapping
	private ResponseEntity<?> guardar(Persona guardar,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		// Guardar archivo si llega
		if (file != null) {
			System.out.println(file.toString());
			System.out.println(file.getContentType());
			System.out.println(file.getName());
			System.out.println(file.getOriginalFilename());
			System.out.println(file.getSize());
		}
		System.out.println("Guardando Persona: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando Persona: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando Persona: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando Persona: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando Persona: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, Persona actualizar,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		if (file != null) {
			System.out.println(file.toString());
			System.out.println(file.getContentType());
			System.out.println(file.getName());
			System.out.println(file.getOriginalFilename()); // Captura de pantalla (1).png
			System.out.println(file.getSize());
		}
		System.out.println("Actualizando Persona: " + id + " -> " + actualizar);
		Persona existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando Persona: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando Persona: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}
}
