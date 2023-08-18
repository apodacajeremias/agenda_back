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
import org.springframework.web.bind.annotation.RestController;

import py.podac.tech.agenda.controller.utils.Beans;
import py.podac.tech.agenda.model.entities.Usuario;
import py.podac.tech.agenda.model.services.interfaces.IUsuarioService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	IUsuarioService service;

	@PostMapping
	private ResponseEntity<?> guardar(@RequestBody Usuario guardar) {
		System.out.println("Guardando Usuario: " + guardar.toString());
		return ResponseEntity.ok(service.guardar(guardar));
	}

	@GetMapping
	private ResponseEntity<List<?>> buscarActivos() {
		System.out.println("Buscando Usuarios: 	Todos");
		return ResponseEntity.ok(service.buscarActivos());
	}

	@PutMapping("/{ID}")
	private ResponseEntity<?> actualizar(@PathVariable UUID ID, @RequestBody Usuario actualizar) {
		System.out.println("Actualizando Usuario: " + ID + " -> " + actualizar + " ID " + actualizar.getID());
		Usuario existente = service.buscar(actualizar.getID());
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(service.guardar(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando Usuario: " + ID);
		return ResponseEntity.ok(service.eliminar(ID));
	}

	@GetMapping("/{ID}")
	private ResponseEntity<?> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando Usuario: " + ID);
		return ResponseEntity.ok(service.buscar(ID));
	}
	
	// Username = Email
	@GetMapping("/existeEmail/{email}")
	private ResponseEntity<Boolean> existeEmail(@PathVariable String email){
		System.out.println("Verificando disponibilidad de email: " + email);
		return ResponseEntity.ok(service.existeEmail(email));
	}
}
