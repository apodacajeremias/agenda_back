package py.jere.agendate.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import py.jere.agendate.model.entities.Empresa;
import py.jere.agendate.security.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

	private String accessToken;
	private String refreshToken;
	private User user;
	private Empresa empresa;

}
