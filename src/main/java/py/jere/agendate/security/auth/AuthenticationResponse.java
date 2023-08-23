package py.jere.agendate.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.jere.agendate.model.entities.Empresa;
import py.jere.agendate.model.entities.Usuario;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

	private String token;

	private Usuario usuario;
	
	private Empresa empresa;
}
