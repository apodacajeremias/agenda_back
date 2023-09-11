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
import py.jere.agendate.controller.utils.archivos.FileController;
import py.jere.agendate.controller.utils.archivos.UploadFileResponse;
import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.model.services.interfaces.IAgendaService;
import py.jere.agendate.model.services.interfaces.IPersonaService;
import py.jere.agendate.model.services.interfaces.ITransaccionService;

@RestController
@RequestMapping("/personas")
@CrossOrigin
public class PersonaController {

	@Autowired
	IPersonaService service;

	@Autowired
	IAgendaService agendaService;

	@Autowired
	ITransaccionService transaccionService;

	@Autowired
	private FileController storage;

	@PostMapping
	private ResponseEntity<?> guardar(Persona guardar,
			@RequestParam(name = "file", required = false) MultipartFile file) {
		if (file != null) {
			UploadFileResponse fileResponse = storage.uploadFile(file);
			System.out.println(fileResponse);
			guardar.setFotoPerfil(fileResponse.getFileName());
		}
		guardar.setActivo(true);
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
			UploadFileResponse fileResponse = storage.uploadFile(file);
			System.out.println(fileResponse);
			actualizar.setFotoPerfil(fileResponse.getFileName());
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
	
	@GetMapping("/{id}/agendas")
	private ResponseEntity<?> buscarAgendas(@PathVariable UUID id) {
		System.out.println("Buscando agendamientos de Persona: " + id);
		return ResponseEntity.ok(agendaService.buscarPorPersona(id));
	}

	@GetMapping("/{id}/transacciones")
	private ResponseEntity<?> buscarTransacciones(@PathVariable UUID id) {
		System.out.println("Buscando transacciones de Persona: " + id);
		return ResponseEntity.ok(transaccionService.buscarPorPersona(id));
	}

	
}
