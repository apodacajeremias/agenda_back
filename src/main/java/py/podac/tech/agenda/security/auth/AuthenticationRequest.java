package py.podac.tech.agenda.security.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	@ToString.Exclude
	String password;
}
