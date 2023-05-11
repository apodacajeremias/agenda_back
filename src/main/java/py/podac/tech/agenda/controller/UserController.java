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
import py.podac.tech.agenda.security.user.User;
import py.podac.tech.agenda.security.user.UserRepository;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	UserRepository repo;

//	POST 	/orders (post/add a new order in the set of orders)
//	GET 	/orders (get a list of orders)
//	PUT 	/orders/{id} (replace an order)
//	DELETE 	/orders/{id} (delete an order)
//	GET 	/orders/{id} (get a single order)

	@PostMapping
	private ResponseEntity<User> guardar(@RequestBody User guardar) {
		System.out.println("Guardando User: " + guardar.toString());
		return ResponseEntity.ok(repo.save(guardar));
	}

	@GetMapping
	private ResponseEntity<List<User>> buscarActivos() {
		System.out.println("Buscando User: 	Todos");
		return ResponseEntity.ok(repo.findAll());
	}

	@PutMapping("/{ID}")
	private ResponseEntity<User> actualizar(@PathVariable UUID ID, @RequestBody User actualizar) {
		System.out.println("Actualizando User: " + ID + " -> " + actualizar + " ID " + actualizar.getID());
		User existente = repo.findById(actualizar.getID()).orElseThrow();
		Beans.copyNonNullProperties(actualizar, existente); // Funde los datos
		return ResponseEntity.ok(repo.save(existente)); // Hibernate solo cambia datos modificados
	}

	@DeleteMapping("/{ID}")
	private ResponseEntity<Boolean> eliminar(@PathVariable UUID ID) {
		System.out.println("Eliminando User: " + ID);
		repo.deleteById(ID);
		return ResponseEntity.ok(Boolean.TRUE);
	}

	@GetMapping("/{ID}")
	private ResponseEntity<User> buscar(@PathVariable UUID ID) {
		System.out.println("Buscando User: " + ID);
		return ResponseEntity.ok(repo.findById(ID).orElseThrow());
	}
}
