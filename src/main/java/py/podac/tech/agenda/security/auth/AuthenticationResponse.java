package py.podac.tech.agenda.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.podac.tech.agenda.model.entities.Persona;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	private String token;

	private Persona persona;

	// TODO: DEVOLVER AL FRONT SOLAMENTE LA PERSONA
//	private Persona persona;
//
//	public Persona getPersona() {
//		user.getPersona().setUser(null);
//		this.persona = user.getPersona();
//		return persona;
//	}
}
