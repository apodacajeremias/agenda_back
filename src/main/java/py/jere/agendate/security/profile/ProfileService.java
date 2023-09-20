package py.jere.agendate.security.profile;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import py.jere.agendate.model.entities.Persona;
import py.jere.agendate.security.user.IUserService;
import py.jere.agendate.security.user.User;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private final IUserService userService;

	public void completeProfile(UUID idUser, Persona persona) throws Exception {
		System.out.println("Completando perfil -> " + idUser + " | " + persona);
		User user = this.userService.buscar(idUser);
		user.setPersona(persona);
		this.userService.registrar(user);
	}

	public void completeConfig() {

	}
}
