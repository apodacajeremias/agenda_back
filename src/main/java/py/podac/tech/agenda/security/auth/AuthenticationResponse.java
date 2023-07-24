package py.podac.tech.agenda.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.podac.tech.agenda.model.entities.Usuario;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	private String token;

	private Usuario usuario;
}
