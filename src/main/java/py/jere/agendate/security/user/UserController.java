package py.jere.agendate.security.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.jere.agendate.controller.utils.Beans;


@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	IUserService service;

	@Secured(value = {"ROLE_ADMIN", "admin:create"})
	@PostMapping
	private ResponseEntity<?> guardar(User guardar) throws Exception {
		System.out.println("Guardando User: " + guardar.toString());
		return ResponseEntity.ok(service.registrar(guardar));
	}

	@Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER"})
	@GetMapping
	private ResponseEntity<List<?>> buscarPorEstado(@RequestParam(required = false) Boolean activo) {
		if (activo == null) {
			System.out.println("Buscando User: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		} else if (activo) {
			System.out.println("Buscando User: Solo activos");
			return ResponseEntity.ok(service.buscarActivos());
		} else if (!activo) {
			System.out.println("Buscando User: Solo inactivos");
			return ResponseEntity.ok(service.buscarInactivos());
		} else {
			System.out.println("Buscando User: Todos");
			return ResponseEntity.ok(service.buscarTodos());
		}
	}

	@Secured(value = {"ROLE_ADMIN", "ROLE_MANAGER"})
	@PutMapping("/{id}")
	private ResponseEntity<?> actualizar(@PathVariable UUID id, User actualizar) throws Exception {
		System.out.println("Actualizando User: " + id + " -> " + actualizar);
		User existente = service.buscar(id);
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.registrar(existente)); // Hibernate solo cambia datos modificados
	}

	@Secured(value = {"ROLE_ADMIN"})
	@DeleteMapping("/{id}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID id) {
		System.out.println("Eliminando User: " + id);
		return ResponseEntity.ok(service.eliminar(id));
	}

	@GetMapping("/{id}")
	private ResponseEntity<?> buscar(@PathVariable UUID id) {
		System.out.println("Buscando User: " + id);
		return ResponseEntity.ok(service.buscar(id));
	}
}
