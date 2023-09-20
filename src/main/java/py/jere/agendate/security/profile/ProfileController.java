package py.jere.agendate.security.profile;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.jere.agendate.model.entities.Empresa;
import py.jere.agendate.model.entities.Persona;

@RestController
@RequestMapping("/profiles")
@CrossOrigin
public class ProfileController {
	
	@Autowired
	private ProfileService service;
	
	@PostMapping("/complete/{id}")
	public void completeProfile(@PathVariable UUID id, Persona persona) throws Exception {
		service.completeProfile(id, persona);
	}

	@PostMapping("/profile/config")
	public void completeConfig(Empresa empresa) throws Exception {
	}

}
